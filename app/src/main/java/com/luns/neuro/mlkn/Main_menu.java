package com.luns.neuro.mlkn;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.luns.neuro.mlkn.DataAdapter.FundiTypes;
import com.luns.neuro.mlkn.DataAdapter.FundiTypesAdapter;
import com.luns.neuro.mlkn.library.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Main_menu extends AppCompatActivity implements FundiTypesAdapter.FundiTypesAdapterListener {
    private String strServerResponseResultData="";
    private RecyclerView init_ft_recyclerview;
    private FundiTypesAdapter ftAdapter;
    private List<FundiTypes> ftList = new ArrayList<>();
    private ProgressBar progressBar;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    private String strActionSummary="",strSelectedService="";
    private String strUserPhonenumber="",strUserFirebaseId="",strUserEmailAddress="";

//    private SQLiteManagerMainMenu sqLiteManagerMainMenu;
//    private SharedPreferences preferencesMainMenu;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);
//        sqLiteManagerMainMenu = new SQLiteManagerMainMenu(this);
//        preferencesMainMenu = PreferenceManager.getDefaultSharedPreferences(this);

        progressBar =  findViewById(R.id.progressBar1);
        init_ft_recyclerview = findViewById(R.id.ft_recycler_view);
        ftList = new ArrayList<>();
        ftAdapter = new FundiTypesAdapter(this, ftList,this);
        RecyclerView.LayoutManager ftLayoutManager = new GridLayoutManager(this, 2);
        //init_ft_recyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        //init_ft_recyclerview.setLayoutManager(new GridLayoutManager(this, 4));
        init_ft_recyclerview.setLayoutManager(ftLayoutManager);
        init_ft_recyclerview.setNestedScrollingEnabled(false);
        init_ft_recyclerview.setItemAnimator(new DefaultItemAnimator());
        init_ft_recyclerview.setAdapter(ftAdapter);
        init_ft_recyclerview.addOnItemTouchListener(new Main_menu.RecyclerTouchListener(getApplicationContext(), init_ft_recyclerview, new Main_menu.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                cd = new ConnectionDetector(getApplicationContext());
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    FundiTypes ft = ftList.get(position);
                    strSelectedService=ft.getStrFtTitle();
//                String strCacheData = preferencesMainMenu.getString("strResponseJson",null);

                    //                   Toast.makeText(getApplicationContext(), ""+strCacheData, Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(getApplicationContext(), ServiceCalculator.class);
//                    if (strServerResponseResultData)
                    in.putExtra("strServerResponseResultData",strServerResponseResultData);
                    in.putExtra("strSelectedService",strSelectedService);
                   // Bundle bndlAnimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slideinleft, R.anim.slideinright).toBundle();
                    startActivity(in);

//                    if (intCurrent ==1) {
//                        intCurrent++;
//                        llyt1.setVisibility(View.GONE);
//                        llyt2.setVisibility(View.VISIBLE);
//                        llyt3.setVisibility(View.GONE);
//                        btnNext.setVisibility(View.VISIBLE);
//                        btnNext.setText("Next");
//                        btnBack.setVisibility(View.VISIBLE);
//                        addBottomDots(intCurrent);
//                    }

                }else {
                    Snackbar.make(init_ft_recyclerview, "No Internet connection, check settings and try again.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));
//        ArrayList cache = sqLiteManagerMainMenu.getData();
//        boolean isCacheExpire = false;
//        long cacheTime = preferencesMainMenu.getLong("cache", 0);
//
//        if (cacheTime > 0) {
//            long currentTime = new Date().getTime();
//            long difference = currentTime - cacheTime;
//            long seconds = difference / 1000;
//
//            if (seconds > 30) {
//                isCacheExpire = true;
//            }
//        }
//        showData();
//        //if (cache.size() > 0 && !isCacheExpire) {
//        if (cache.size() == 0 || isCacheExpire) {
////                myrequestsList = cache;
////                showData();
////            } else {
//            //Toast.makeText(getApplicationContext(),"geting Data from Server",Toast.LENGTH_LONG).show();
//            getInitData();
//        }


        getInitData();

    }

//    private void showData() {
//
//        ArrayList cache = sqLiteManagerMainMenu.getData();
//        boolean isCacheExpire = false;
//        long cacheTime = preferencesMainMenu.getLong("cache", 0);
//
//        if (cacheTime > 0) {
//            long currentTime = new Date().getTime();
//            long difference = currentTime - cacheTime;
//            long seconds = difference / 1000;
//
//            if (seconds > 30) {
//                isCacheExpire = true;
//            }
//        }
//        ftList.clear();
//        ftList = cache;
//        ftAdapter.setFilter(ftList);
//        // progressBar.setVisibility(View.GONE);
//    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private Main_menu.ClickListener clickListener;
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final Main_menu.ClickListener clickListener) {
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
        public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent e) {

            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, recyclerView.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView recyclerView, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

    }
    ////////////////////////////////////////////////
    public static final String INIT_DATA_URL = "https://www.homlie.co.ke/malakane_init/mlkn_init_data.php";
    private void getInitData() {
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            //blnLL=true;
            //      lazyLoader(blnLL);
            progressBar.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(INIT_DATA_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressBar.setVisibility(View.GONE);
                 //   blnLL=false;
//                lazyLoader(blnLL);
                    ftList.clear();
                    ftAdapter.notifyDataSetChanged();

                    showJSONInitData(response);
                    strServerResponseResultData=response;
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.error_network_timeout),
                                        Toast.LENGTH_LONG).show();
                            } else if (volleyError instanceof AuthFailureError) {
                                Toast.makeText(getApplicationContext(), "" + volleyError.getMessage(), Toast.LENGTH_LONG).show();
                            } else if (volleyError instanceof ServerError) {
                                Toast.makeText(getApplicationContext(), "" + volleyError.getMessage(), Toast.LENGTH_LONG).show();
                            } else if (volleyError instanceof NetworkError) {
                                Toast.makeText(getApplicationContext(), "" + volleyError.getMessage(), Toast.LENGTH_LONG).show();
                            } else if (volleyError instanceof ParseError) {
                                Toast.makeText(getApplicationContext(), "" + volleyError.getMessage(), Toast.LENGTH_LONG).show();
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
                    .make(init_ft_recyclerview, "No internet connection! Check settings and try again.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            getInitData();
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

    private String strFtId="", strFtTitle= "",strFtDescription= "",strFtUrl="";
    private static final String JSON_ARRAY_FT = "service_type";
    ArrayList<String> servicesArray = new ArrayList<String>();
    public void showJSONInitData(String response){
        try {
//            sqLiteManagerMainMenu.deleteOldCache();
            ftList.clear();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY_FT);
            //   Log.e(TAG, "Array: " + ""+result);
            ///////////////////////////
            for (int i = 0; i < result.length(); i++) {
                JSONObject serverData = result.getJSONObject(i);
                strFtId = serverData.getString("jbid");
                strFtTitle = serverData.getString("jbfundititle");
                strFtDescription = serverData.getString("jbdescription");
                strFtUrl = serverData.getString("jburl");
                servicesArray.add(strFtTitle+"@@"+strFtDescription+"@@"+strFtUrl);
            }
            ////Cleaning Source Knowledge Elements
            Set<String> sourceHash=new HashSet<>();
            sourceHash.addAll(servicesArray);
            TreeSet myTreeSet = new TreeSet();
            myTreeSet.addAll(sourceHash);
            servicesArray.clear();
            servicesArray.addAll(myTreeSet);
            int v=1;
            for (String strServiceElement : servicesArray) {

                String[] separatorArrayA = strServiceElement.split("@@");
                FundiTypes fts = new FundiTypes("" + v, separatorArrayA[0], separatorArrayA[1], separatorArrayA[2], "");
                //              sqLiteManagerMainMenu.addData(fts);
                ftList.add(fts);
                ftAdapter.notifyDataSetChanged();
                v++;
            }
//            preferencesMainMenu.edit().putLong("cache", new Date().getTime()).apply();
//
//            SharedPreferences settings = this.getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
//            settings.edit().remove("strResponseJson").commit();
//            preferencesMainMenu.edit().putString("strResponseJson",  strServerResponseResultData).commit();
//
//            ftAdapter.setFilter(ftList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onFundiTypeSelected(FundiTypes all) {
        //Toast.makeText(getApplicationContext(), "Selected: " + contact.getName() + ", " + contact.getPhone(), Toast.LENGTH_LONG).show();
    }
    ////////////////////
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //overridePendingTransition( 0,R.anim.slideinright);
    }
}
