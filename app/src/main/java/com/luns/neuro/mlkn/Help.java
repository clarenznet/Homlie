package com.luns.neuro.mlkn;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.luns.neuro.mlkn.DataAdapter.HelpItems;
import com.luns.neuro.mlkn.DataAdapter.HelpItemsAdapter;
import com.luns.neuro.mlkn.library.ConnectionDetector;
import com.luns.neuro.mlkn.library.SQLiteManagerHelp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Clarence on 9/9/2016.
 */
public class Help extends AppCompatActivity implements View.OnClickListener {
    private ProgressBar progressBar;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    private List<HelpItems> hlpList = new ArrayList<>();
    private RecyclerView recyclerView;//,assigned_recycler_view;
    private HelpItemsAdapter hlpAdapter;
    public static final String DATA_URL = "https://www.homlie.co.ke/malakane_init/mlkn_gethelp.php";
    public static final String KEY_HELPID = "hlp_id";
    public static final String KEY_HELPCATEGORY = "hlp_category";
    public static final String KEY_HELPISSUE = "hlp_issue";
    public static final String KEY_HELPDESCRIPTION = "hlp_description";

    public static final String JSON_ARRAY = "result";
    String strHlpId = "", strHlpCategory = "", strHlpIssue = "", strHlpDescription = "";

    private SQLiteManagerHelp sqLiteManagerHelp;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        sqLiteManagerHelp = new SQLiteManagerHelp(this);
        progressBar = findViewById(R.id.pBarHelp);
        recyclerView = findViewById(R.id.help_recycler_view);
        hlpAdapter = new HelpItemsAdapter(hlpList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //  recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(hlpAdapter);
        recyclerView.addOnItemTouchListener(new ScrollingActivity.RecyclerTouchListener(getApplicationContext(), recyclerView, new ScrollingActivity.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                cd = new ConnectionDetector(getApplicationContext());
                isInternetPresent = cd.isConnectingToInternet();
                //if (isInternetPresent) {
                HelpItems hlpitems = hlpList.get(position);
//                    strOrderTim = hlpitems.getStrRequestTime();
//                    String strTicketId = hlpitems.getStrRequestTcktCode();
//                    strOrderTyp = hlpitems.getStrRequestTitle();
//                    strOrderSummary = hlpitems.getStrRequestBody();
                //}
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        getData();
        ArrayList cache = sqLiteManagerHelp.getData();
        boolean isCacheExpire = false;
        long cacheTime = preferences.getLong("cache", 0);
        if (cacheTime > 0) {
            long currentTime = new Date().getTime();
            long difference = currentTime - cacheTime;
            long seconds = difference / 1000;

            if (seconds > 30) {
                isCacheExpire = true;
            }
        }
        showData();
        //           showDataMainMenu();

        //if (cache.size() > 0 && !isCacheExpire) {
        if (cache.size() == 0 || isCacheExpire) {
//                myrequestsList = cache;
//                showData();
//            } else {
            //Toast.makeText(getApplicationContext(),"geting Data from Server",Toast.LENGTH_LONG).show();
            getData();
        }

    }


    private void showData() {
        ArrayList cache = sqLiteManagerHelp.getData();
        boolean isCacheExpire = false;
        long cacheTime = preferences.getLong("cache", 0);
        if (cacheTime > 0) {
            long currentTime = new Date().getTime();
            long difference = currentTime - cacheTime;
            long seconds = difference / 1000;
            if (seconds > 30) {
                isCacheExpire = true;
            }
        }
        hlpList.clear();
        hlpList = cache;
        hlpAdapter.setFilter(hlpList);
//        assignedRequestsList.clear();
//        assignedRequestsList = cache;
//        assignedRequestsAdapter.setFilter(assignedRequestsList);
        //swpRefresh.setRefreshing(false);
//        idBcgdAnim.setVisibility(View.GONE);
//        tvExpl1.setVisibility(View.GONE);
//        tvExpl2.setVisibility(View.GONE);

        // progressBar.setVisibility(View.GONE);
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ScrollingActivity.ClickListener clickListener;


        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ScrollingActivity.ClickListener clickListener) {
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

    private void getData() {
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            String url = DATA_URL;
            StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    showJSON(response);
                }

            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //swpRefresh.setRefreshing(false);
                            //Showing toast
                            if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                                Toast.makeText(getApplicationContext(), "T" + getApplicationContext().getString(R.string.error_network_timeout),
                                        Toast.LENGTH_LONG).show();
                            } else if (volleyError instanceof AuthFailureError) {
                                //
                                Toast.makeText(getApplicationContext(), "A" + volleyError.getMessage(), Toast.LENGTH_LONG).show();

                            } else if (volleyError instanceof ServerError) {
                                //
                                Toast.makeText(getApplicationContext(), "S" + volleyError.getMessage(), Toast.LENGTH_LONG).show();

                            } else if (volleyError instanceof NetworkError) {
                                //
                                Toast.makeText(getApplicationContext(), "N" + volleyError.getMessage(), Toast.LENGTH_LONG).show();

                            } else if (volleyError instanceof ParseError) {
                                Toast.makeText(getApplicationContext(), "P" + volleyError.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            stringRequest.setShouldCache(false);
            requestQueue.add(stringRequest);
        } else {

            //Snackbar.make(recyclerView, "No Internet connection, check settings and try again.", Snackbar.LENGTH_LONG)
            //      .setAction("Action", null).show();
            Snackbar snackbar = Snackbar
                    .make(recyclerView, "No internet connection! Check settings and try again.", Snackbar.LENGTH_LONG)
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
            TextView textView = sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();

        }

    }

    private void showJSON(String response) {
        try {
            sqLiteManagerHelp.deleteOldCache();
            hlpList.clear();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);
//            strRawDat = jsonObject.getString(KEY_RESTYPE);

            // looping through All Products
            for (int i = 0; i < result.length(); i++) {
                // JSONObject c = searchResultsArray.getJSONObject(i);
                JSONObject collegeData = result.getJSONObject(i);
                strHlpId = collegeData.getString(KEY_HELPID);
                strHlpCategory = collegeData.getString(KEY_HELPCATEGORY);
                strHlpIssue = collegeData.getString(KEY_HELPISSUE);
                strHlpDescription = collegeData.getString(KEY_HELPDESCRIPTION);
                HelpItems ordrs = new HelpItems(strHlpId, strHlpCategory, strHlpIssue, strHlpDescription);
                sqLiteManagerHelp.addData(ordrs);
                hlpList.add(ordrs);
                hlpAdapter.notifyDataSetChanged();
            }
            ///Toast.makeText(getApplicationContext(),strRawDat,Toast.LENGTH_LONG).show();
            preferences.edit().putLong("cache", new Date().getTime()).apply();
            hlpAdapter.setFilter(hlpList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // textViewResult.setText("Name:\t"+name+"\nAddress:\t" +address+ "\nVice Chancellor:\t"+ vc);
    }

    @Override
    public void onClick(View v) {

    }
}