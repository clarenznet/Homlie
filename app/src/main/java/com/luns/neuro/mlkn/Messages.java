package com.luns.neuro.mlkn;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.luns.neuro.mlkn.DataAdapter.Notifs;
import com.luns.neuro.mlkn.DataAdapter.NotifsAdapter;
import com.luns.neuro.mlkn.library.ConnectionDetector;
import com.luns.neuro.mlkn.library.SharedPrefManager;
import com.luns.neuro.mlkn.library.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Messages extends AppCompatActivity implements View.OnClickListener {
    //public static final String DATA_URL = "http://10.0.2.2/smartbuyer/getmyorders.php?struserid=";
    public static final String DATA_URL = "https://www.instrov.com/malakane_init/mlknget_notifications.php?struserid=";
    public static final String KEY_NOTIFID = "notif_id";
    public static final String KEY_NOTIFTITLE = "notif_title";
    public static final String KEY_NOTIFBODY = "notif_body";
    public static final String KEY_NOTIFREQUESTID = "notif_requestid";
    public static final String KEY_NOTIFCOLOR = "notif_color";
    public static final String KEY_CREATEDAT = "notif_time";
    public static final String JSON_ARRAY = "result";
    private ProgressDialog loading;
    private List<Notifs> notifsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotifsAdapter notifsAdapter;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    private ProgressBar progressBar;
    private TextView tvNullPlaceHolder;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_activity);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.notifications_recycler_view);
        notifsAdapter = new NotifsAdapter(notifsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //  recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(notifsAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                cd = new ConnectionDetector(getApplicationContext());
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
//                    Notifs myorders = notifsList.get(position);
//                    Intent in = new Intent(getApplicationContext(),DetailsScreen.class);
//                    in.putExtra("parentclass","notifications");
//                    startActivity(in);
                } else {
                    Snackbar.make(recyclerView, "No Internet connection, check settings and try again.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        getData();

    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private Messages.ClickListener clickListener;


        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final Messages.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });

        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
    String strEmail="";
    private void getData() {
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
//            swipeRefreshLayout.setRefreshing(true);
            progressBar.setVisibility(View.VISIBLE);
            User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
            String strUserPhonenumber = user.getPhonenumber();
            //
            //     }
            String url = DATA_URL+strUserPhonenumber.trim();
            StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //       swipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                    if (response.equals("Empty")) {
                        tvNullPlaceHolder.setText("No notifications yet");
                        tvNullPlaceHolder.setVisibility(View.VISIBLE);
                    }else{
                        showJSON(response);
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            progressBar.setVisibility(View.GONE);
//                            swipeRefreshLayout.setRefreshing(false);
                            //Showing toast
                            if(volleyError instanceof TimeoutError ||volleyError instanceof NoConnectionError){
                                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.error_network_timeout),
                                        Toast.LENGTH_LONG).show();
                            }else if (volleyError instanceof AuthFailureError){
                                //
                                Toast.makeText(getApplicationContext(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();

                            }else if (volleyError instanceof ServerError){
                                //
                                Toast.makeText(getApplicationContext(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();

                            }else if (volleyError instanceof NetworkError){
                                //
                                Toast.makeText(getApplicationContext(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();

                            }else if (volleyError instanceof ParseError){
                                Toast.makeText(getApplicationContext(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();

                            }
                        }
                    });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);

            requestQueue.add(stringRequest);
        } else {
            //Snackbar.make(recyclerView, "No Internet connection, check settings and try again.", Snackbar.LENGTH_LONG)
            //      .setAction("Action", null).show();
            Snackbar snackbar = Snackbar
                    .make(recyclerView, "No internet connection! Check settings and try again.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            getData();
                        }
                    });
// Changing message text color
            snackbar.setActionTextColor(Color.RED);
// Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();

        }

    }
    String strNotifId= "",strNotifTime="",strNotifTitle="",strNotifBody="",strNotifPostId="",strNotifColor="";

    private void showJSON(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);
            //JSONObject collegeData = result.getJSONObject(0);
            // looping through All Products
            for (int i = 0; i < result.length(); i++) {
                // JSONObject c = searchResultsArray.getJSONObject(i);
                JSONObject collegeData = result.getJSONObject(i);
                strNotifId = collegeData.getString(KEY_NOTIFID);
                strNotifTime =collegeData.getString(KEY_CREATEDAT);
                strNotifTitle = collegeData.getString(KEY_NOTIFTITLE);
                strNotifBody = collegeData.getString(KEY_NOTIFBODY);
                strNotifPostId = collegeData.getString(KEY_NOTIFREQUESTID);
                strNotifColor = collegeData.getString(KEY_NOTIFCOLOR);
                Notifs ordrs = new Notifs(strNotifId,strNotifTitle,strNotifBody,strNotifTime,strNotifPostId,strNotifColor);
                notifsList.add(ordrs);
                notifsAdapter.notifyDataSetChanged();
                //Toast.makeText(getApplicationContext(),strTitle+","+strPriceRange+","+strOrderId,Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // textViewResult.setText("Name:\t"+name+"\nAddress:\t" +address+ "\nVice Chancellor:\t"+ vc);
    }
    @Override
    public void onClick(View v) {
        //if(v == buttonGet){
        //getData();
        //}
    }
    @Override
    public void onBackPressed() {
        //loading.dismiss();
        finish();
        return;
    }

}
