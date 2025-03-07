package com.luns.neuro.mlkn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.stetho.Stetho;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.luns.neuro.mlkn.DataAdapter.FundiTypes;
import com.luns.neuro.mlkn.DataAdapter.FundiTypesAdapter;
import com.luns.neuro.mlkn.DataAdapter.MyRequests;
import com.luns.neuro.mlkn.DataAdapter.MyRequestsAdapter;
import com.luns.neuro.mlkn.library.ConnectionDetector;
import com.luns.neuro.mlkn.library.SQLiteManagerMainMenu;
import com.luns.neuro.mlkn.library.SQLiteManagerMyRequests;
import com.luns.neuro.mlkn.library.SharedPrefManager;
import com.luns.neuro.mlkn.library.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ScrollingActivity extends AppCompatActivity implements FundiTypesAdapter.FundiTypesAdapterListener {
    public static final String INIT_DATA_URL = "https://www.homlie.co.ke/malakane_init/mlkn_init_data.php";
    private static final String JSON_ARRAY_FT = "service_type";
    ////////bottom sheet
    LinearLayout llBottomSheetMainMenu;
    BottomSheetBehavior bottomSheetBehavior;
    ArrayList<String> servicesArray = new ArrayList<String>();
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private String strServerResponseResultData = "";
    private RecyclerView init_ft_recyclerview;
    private FundiTypesAdapter ftAdapter;

    /////////////////////////////////////////////////////////////////////////////////////////////////////


    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    private List<MyRequests> myrequestsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyRequestsAdapter myrequestsAdapter;
    public static final String DATA_URL = "https://www.homlie.co.ke/malakane_init/mlkn_getmyrequests.php?struserid=";
    public static final String KEY_REQUESTID = "fr_id";
    public static final String KEY_REQUESTTCKTCODE = "fr_tcktcode";
    public static final String KEY_REQUESTTITLE = "fr_funditype";
    public static final String KEY_REQUESTBODY = "fr_taskdetail";
    public static final String KEY_REQUESTTIME = "fr_taskdatetime";
    public static final String KEY_REQUESTCOLOR = "fr_requestcolor";
    public static final String KEY_CREATEDAT = "fr_createdat";
    public static final String KEY_REQUESTSTATUS = "fr_status";
    public static final String JSON_ARRAY = "result";
    public static final String JSON_ARRAYMAJOR = "res_type";

    public static final String KEY_RESTYPE = "res_type";
    private SwipeRefreshLayout swpRefresh;
    String strEmail = "";
    String strRequestId = "", strRequestTcktCode = "", strRequestTitle = "", strRequestBody = "", strRequestTime = "", strRequestColor = "", strRequestStatus = "", strCtreatedAt = "";
    //Image Loader
    private ImageLoader imageLoader;
    public static final String BCKDROP_URL = "https://www.homlie.co.ke/malakane_init/appcovers/cover.png";

    private ImageView idBcgdAnim;
    String strRawDat = "";
    private String strTicketId = "", strOrderSummary = "", strOrderTyp = "", strOrderTim = "";
    private ImageView backdrop;
    private TextView tvExpl1, tvExpl2;
    private SQLiteManagerMyRequests sqLiteManagerMyRequests;
    private SharedPreferences preferences;
    private List<FundiTypes> ftList = new ArrayList<>();
    private String strActionSummary = "", strSelectedService = "";
    private String strUserPhonenumber = "", strUserFirebaseId = "", strUserEmailAddress = "";
    private SQLiteManagerMainMenu sqLiteManagerMainMenu;

    //    private void backDropImage(){
//        try {
//            String url = BCKDROP_URL.trim();
//            imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext())
//                    .getImageLoader();
//            imageLoader.get(url, ImageLoader.getImageListener(backdrop,
//                    R.color.btn_bg, R.color.btn_bg));
//            backdrop.setImageUrl(url, imageLoader);
//
//            //    Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    private void backDropImage() {
        //Initialize ImageView
        //ImageView imageView = (ImageView) findViewById(R.id.imageView);

        //Loading image from below url into imageView
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.imagenotfound)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .dontAnimate()
                .dontTransform();

        Glide.with(this)
                .load(BCKDROP_URL.trim())
                .apply(options)
                .into(backdrop);
    }

    private SharedPreferences preferencesMainMenu;

    private void showJSON(String response) {
        try {
            sqLiteManagerMyRequests.deleteOldCache();
            myrequestsList.clear();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);
            strRawDat = jsonObject.getString(KEY_RESTYPE);

            // looping through All Products
            for (int i = 0; i < result.length(); i++) {
                // JSONObject c = searchResultsArray.getJSONObject(i);
                JSONObject collegeData = result.getJSONObject(i);
                strRequestId = collegeData.getString(KEY_REQUESTID);
                strRequestTcktCode = collegeData.getString(KEY_REQUESTTCKTCODE);
                strRequestTitle = collegeData.getString(KEY_REQUESTTITLE);
                strRequestBody = collegeData.getString(KEY_REQUESTBODY);
                strRequestTime = collegeData.getString(KEY_REQUESTTIME);
                strRequestColor = collegeData.getString(KEY_REQUESTCOLOR);
                strRequestStatus = collegeData.getString(KEY_REQUESTSTATUS);
                strCtreatedAt = collegeData.getString(KEY_CREATEDAT);
                strRequestTcktCode = collegeData.getString(KEY_REQUESTTCKTCODE);
                MyRequests ordrs = new MyRequests(strRequestId, strRequestTcktCode, strRequestTitle, strRequestBody, strRequestTime, strRequestColor, strRequestStatus, strCtreatedAt);
                sqLiteManagerMyRequests.addData(ordrs);
                myrequestsList.add(ordrs);
                myrequestsAdapter.notifyDataSetChanged();
            }
            ///Toast.makeText(getApplicationContext(),strRawDat,Toast.LENGTH_LONG).show();
            preferences.edit().putLong("cache", new Date().getTime()).apply();
            myrequestsAdapter.setFilter(myrequestsList);
            if (strRawDat.equals("success")) {
                idBcgdAnim.setVisibility(View.GONE);
                tvExpl1.setVisibility(View.GONE);
                tvExpl2.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // textViewResult.setText("Name:\t"+name+"\nAddress:\t" +address+ "\nVice Chancellor:\t"+ vc);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notifications) {
            Intent intent = new Intent(ScrollingActivity.this, Messages.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_profile) {
            Intent intent = new Intent(ScrollingActivity.this, My_Profile.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_help) {
            Intent in = new Intent(ScrollingActivity.this, Help.class);
            startActivity(in);
            return true;
        }
        if (id == R.id.action_share) {
            shareText();
            return true;
        }
        if (id == R.id.action_feedback) {
            Intent in = new Intent(ScrollingActivity.this, Feedback.class);
            startActivity(in);
            return true;
        }
        if (id == R.id.action_terms) {
            Intent in = new Intent(ScrollingActivity.this, TermsAndConditions.class);
            startActivity(in);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void shareText() {
        Intent shrIntnt = new Intent(Intent.ACTION_SEND);
        shrIntnt.setType("text/plain");
        shrIntnt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shrIntnt.putExtra(Intent.EXTRA_SUBJECT, "Get your house chores done at the comfort of your home.'");
        shrIntnt.putExtra(Intent.EXTRA_TEXT, "Follow the link to download app from playstore https://play.google.com/store/apps/details?id=com.luns.neuro.mlkn");
        startActivity(Intent.createChooser(shrIntnt, "Share link!"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
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

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    private String strFtId = "", strFtTitle = "", strFtDescription = "", strFtUrl = "";
    private Button btnSRequestService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /////remove stetho ooooh
        Stetho.initializeWithDefaults(this);
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            Intent i = new Intent(getApplicationContext(), SignIn.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            setContentView(R.layout.activity_scrolling);
            // in onCreate
            sqLiteManagerMyRequests = new SQLiteManagerMyRequests(this);
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            sqLiteManagerMainMenu = new SQLiteManagerMainMenu(this);
            preferencesMainMenu = PreferenceManager.getDefaultSharedPreferences(this);
            Toolbar toolbar = findViewById(R.id.toolbar);
            //toolbar.setTitle(R.string.app_name);
            toolbar.setTitleTextColor(getResources().getColor(R.color.blue));
            toolbar.setTitle("HOMLIE service");
            toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.overflowdots));
            // setSupportActionBar(toolbar);
            setSupportActionBar(toolbar);
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                    } else {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }
            });
            btnSRequestService = findViewById(R.id.btnSRequestService);
            btnSRequestService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                    } else {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }
            });
            backdrop = findViewById(R.id.backdrop);
            backDropImage();

//            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
//                    R.anim.myanimation);
//            fab.startAnimation(animation);
//            Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(),
//                    R.anim.clockwise);
//            fab.startAnimation(animation2);

            // background animaton

////////////////////////main menu bottom sheet
            llBottomSheetMainMenu = findViewById(R.id.bottom_sheet_mainmenu);
            bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheetMainMenu);
            bottomSheetBehavior.setPeekHeight(0);
            bottomSheetBehavior.setHideable(true);


            idBcgdAnim = findViewById(R.id.idBcgdAnim);
            tvExpl1 = findViewById(R.id.tvExpl1);
            tvExpl2 = findViewById(R.id.tvExpl2);
            //retrieve user shared preferences data
            User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
            recyclerView = findViewById(R.id.dashboard_recycler_view);
            myrequestsAdapter = new MyRequestsAdapter(myrequestsList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            //recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            recyclerView.setLayoutManager(mLayoutManager);
//            recyclerView.setNestedScrollingEnabled(true);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            //  recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(myrequestsAdapter);
            recyclerView.addOnItemTouchListener(new ScrollingActivity.RecyclerTouchListener(getApplicationContext(), recyclerView, new ScrollingActivity.ClickListener() {
                @Override
                public void onClick(View view, int position) {

                    cd = new ConnectionDetector(getApplicationContext());
                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {
                        MyRequests myreq = myrequestsList.get(position);
                        strOrderTim = myreq.getStrRequestTime();
                        strTicketId = myreq.getStrRequestTcktCode();
                        strOrderTyp = myreq.getStrRequestTitle();
                        strOrderSummary = myreq.getStrRequestBody();
                        Intent in = new Intent(getApplicationContext(), DetailsScreen.class);
                        in.putExtra("strTicketCode", strTicketId);
//                    in.putExtra("strOrderSummary", strOrderSummary);
//                    in.putExtra("strOrderTyp", strOrderTyp);
//                    in.putExtra("strOrderTim", strOrderTim);
                        //Bundle bndlAnimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slideinleft, R.anim.slideinright).toBundle();
                        startActivity(in);
                    } else {
                        Snackbar.make(recyclerView, "No Internet connection, check settings and try again.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
            swpRefresh = findViewById(R.id.swpRefresh);
            // Setup refresh listener which triggers new data loading
            swpRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Your code to refresh the list here.
                    // Make sure you call swipeContainer.setRefreshing(false)
                    // once the network request has completed successfully.
                    getData();
                    backDropImage();
                }
            });
            // Configure the refreshing colors
            swpRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_green_light,
                    android.R.color.holo_red_light);

            // getData();
            ArrayList cache = sqLiteManagerMyRequests.getData();
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

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////
            init_ft_recyclerview = findViewById(R.id.ft_recycler_view);
            ftList = new ArrayList<>();
            ftAdapter = new FundiTypesAdapter(this, ftList, this);
            RecyclerView.LayoutManager ftLayoutManager = new LinearLayoutManager(this);
            //RecyclerView.LayoutManager ftLayoutManager = new GridLayoutManager(this,4);
            init_ft_recyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
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
                    //if (isInternetPresent) {
                    FundiTypes ft = ftList.get(position);
                    strSelectedService = ft.getStrFtTitle();
                    strServerResponseResultData = ft.getStrServerResponseResultData();
//                String strCacheData = preferencesMainMenu.getString("strResponseJson",null);

                    //                   Toast.makeText(getApplicationContext(), ""+strCacheData, Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(getApplicationContext(), ServiceCalculator.class);
//                    if (strServerResponseResultData)
                    in.putExtra("strServerResponseResultData", strServerResponseResultData);
                    in.putExtra("strSelectedService", strSelectedService);
                    // Bundle bndlAnimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slideinleft, R.anim.slideinright).toBundle();
                    startActivity(in);
//
//                    }else {
//                        Snackbar.make(init_ft_recyclerview, "No Internet connection, check settings and try again.", Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();
//                    }
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));


            ArrayList cacheMainMenu = sqLiteManagerMainMenu.getData();
            boolean isCacheExpireMainMenu = false;
            long cacheTimeMainMenu = preferencesMainMenu.getLong("cache", 0);
            if (cacheTimeMainMenu > 0) {
                long currentTimeMainMenu = new Date().getTime();
                long differenceMainMenu = currentTimeMainMenu - cacheTimeMainMenu;
                long secondsMainMenu = differenceMainMenu / 1000;

                if (secondsMainMenu > 30) {
                    isCacheExpireMainMenu = true;
                }
            }
            showDataMainMenu();
            //if (cache.size() > 0 && !isCacheExpire) {
            if (cacheMainMenu.size() == 0 || isCacheExpireMainMenu) {
//                myrequestsList = cache;
//                showData();
//            } else {
                //Toast.makeText(getApplicationContext(),"geting Data from Server",Toast.LENGTH_LONG).show();
                getInitData();
            }

        }
    }

    private void showData() {
        ArrayList cache = sqLiteManagerMyRequests.getData();
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
        myrequestsList.clear();
        myrequestsList = cache;
        myrequestsAdapter.setFilter(myrequestsList);
        swpRefresh.setRefreshing(false);
        idBcgdAnim.setVisibility(View.GONE);
        tvExpl1.setVisibility(View.GONE);
        tvExpl2.setVisibility(View.GONE);

        // progressBar.setVisibility(View.GONE);
    }

    private void getData() {

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
//            swipeRefreshLayoutDashboard.setRefreshing(true);
            User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
            String strUserPhonenumber = user.getPhonenumber();
            //
            //     }
            String url = DATA_URL + strUserPhonenumber.trim();
            StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    swpRefresh.setRefreshing(false);
                    //Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                    //if (response.length() == 0 || response.isEmpty() || response.equals("Error") || response=="null"|| response=="{\"result\":[]}") {
//                        tvNullPlaceHolder.setText("No notifications yet");
//                        tvNullPlaceHolder.setVisibility(View.VISIBLE);
                    //myrequestsList.clear();
//                        myrequestsAdapter.notifyDataSetChanged();
                    idBcgdAnim.setVisibility(View.VISIBLE);
                    tvExpl1.setVisibility(View.VISIBLE);
                    tvExpl2.setVisibility(View.VISIBLE);
//                    }
//                    else{
                    //Toast.makeText(ScrollingActivity.this, ""+response, Toast.LENGTH_LONG).show();
                    //myrequestsList.clear();
                    myrequestsAdapter.notifyDataSetChanged();
                    showJSON(response);
                    //preferences.edit().putLong("cache", new Date().getTime()).apply();
//                    idBcgdAnim.setVisibility(View.GONE);
//                    tvExpl1.setVisibility(View.GONE);
//                    tvExpl2.setVisibility(View.GONE);

                    //}
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            swpRefresh.setRefreshing(false);
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

    private void showDataMainMenu() {

        ArrayList cacheMainMenu = sqLiteManagerMainMenu.getData();
        boolean isCacheExpireMainMenu = false;
        long cacheTimeMainMenu = preferencesMainMenu.getLong("cache", 0);

        if (cacheTimeMainMenu > 0) {
            long currentTimeMainMenu = new Date().getTime();
            long differenceMainMenu = currentTimeMainMenu - cacheTimeMainMenu;
            long secondsMainMenu = differenceMainMenu / 1000;

            if (secondsMainMenu > 30) {
                isCacheExpireMainMenu = true;
            }
        }
        ftList.clear();
        ftList = cacheMainMenu;
        ftAdapter.setFilter(ftList);
        swpRefresh.setRefreshing(false);

        // progressBar.setVisibility(View.GONE);
    }

    private void getInitData() {
        swpRefresh.setRefreshing(true);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            //blnLL=true;
            //      lazyLoader(blnLL);
            StringRequest stringRequest = new StringRequest(INIT_DATA_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //   blnLL=false;
//                lazyLoader(blnLL);
                    ftList.clear();
                    ftAdapter.notifyDataSetChanged();
                    showJSONInitData(response);
                    strServerResponseResultData = response;
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                                Toast.makeText(getApplicationContext(), "T2" + getApplicationContext().getString(R.string.error_network_timeout),
                                        Toast.LENGTH_LONG).show();
                            } else if (volleyError instanceof AuthFailureError) {
                                Toast.makeText(getApplicationContext(), "A2" + volleyError.getMessage(), Toast.LENGTH_LONG).show();
                            } else if (volleyError instanceof ServerError) {
                                Toast.makeText(getApplicationContext(), "S2" + volleyError.getMessage(), Toast.LENGTH_LONG).show();
                            } else if (volleyError instanceof NetworkError) {
                                Toast.makeText(getApplicationContext(), "N2" + volleyError.getMessage(), Toast.LENGTH_LONG).show();
                            } else if (volleyError instanceof ParseError) {
                                Toast.makeText(getApplicationContext(), "P2" + volleyError.getMessage(), Toast.LENGTH_LONG).show();
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

    public void showJSONInitData(String response) {
        try {
            getData();
            sqLiteManagerMainMenu.deleteOldCache();
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
                servicesArray.add(strFtTitle + "@@" + strFtDescription + "@@" + strFtUrl);
            }
            ////Cleaning Source Knowledge Elements
            Set<String> sourceHash = new HashSet<>();
            sourceHash.addAll(servicesArray);
            TreeSet myTreeSet = new TreeSet();
            myTreeSet.addAll(sourceHash);
            servicesArray.clear();
            servicesArray.addAll(myTreeSet);
            int v = 1;
            for (String strServiceElement : servicesArray) {
                String[] separatorArrayA = strServiceElement.split("@@");
                FundiTypes fts = new FundiTypes("" + v, separatorArrayA[0], separatorArrayA[1], separatorArrayA[2], response);
                sqLiteManagerMainMenu.addData(fts);
                ftList.add(fts);
                ftAdapter.notifyDataSetChanged();
                v++;
            }
            preferencesMainMenu.edit().putLong("cache", new Date().getTime()).apply();
//            SharedPreferences settings = this.getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
//            settings.edit().remove("strResponseJson").commit();
//            preferencesMainMenu.edit().putString("strResponseJson",  strServerResponseResultData).commit();

            ftAdapter.setFilter(ftList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFundiTypeSelected(FundiTypes all) {
        //Toast.makeText(getApplicationContext(), "Selected: " + contact.getName() + ", " + contact.getPhone(), Toast.LENGTH_LONG).show();
    }

    ////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onBackPressed() {
        finish();
    }
}