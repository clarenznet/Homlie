package com.luns.neuro.mlkn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.stetho.Stetho;
import com.google.android.material.snackbar.Snackbar;
import com.luns.neuro.mlkn.DataAdapter.FundiTypes;
import com.luns.neuro.mlkn.DataAdapter.FundiTypesAdapter;
import com.luns.neuro.mlkn.library.ConnectionDetector;
import com.luns.neuro.mlkn.library.SQLiteManagerMainMenu;
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


public class LandingPage extends AppCompatActivity implements View.OnClickListener, FundiTypesAdapter.FundiTypesAdapterListener {

    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    public static final String INIT_DATA_URL = "https://www.instrov.com/malakane_init/mlkn_init_data.php";
    private static final String JSON_ARRAY_FT = "service_type";
    ArrayList<String> servicesArray = new ArrayList<String>();
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private String strServerResponseResultData = "";
    private RecyclerView init_ft_recyclerview;
    private FundiTypesAdapter ftAdapter;
    private SQLiteManagerMainMenu sqLiteManagerMainMenu;
    private SharedPreferences preferencesMainMenu;
    private ImageButton imgBtnMyorders, imgBtnChefServices, imgBtnIroning, imgBtnLaundry, imgBtnRoomCleaning, imgBtnHelp, imgBtnMessages, imgBtnSettings, imgBtnProfile;
    private View tvMainHeader;
    private String strFtId = "", strFtTitle = "", strFtDescription = "", strFtUrl = "";
    private List<FundiTypes> ftList = new ArrayList<>();


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
            setContentView(R.layout.activity_landingpage);
            sqLiteManagerMainMenu = new SQLiteManagerMainMenu(this);
            preferencesMainMenu = PreferenceManager.getDefaultSharedPreferences(this);
            User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
            imgBtnMyorders = findViewById(R.id.imgBtnMyorders);
            imgBtnMyorders.setOnClickListener(this); // calling onClick() method
            imgBtnChefServices = findViewById(R.id.imgBtnChefServices);
            imgBtnChefServices.setOnClickListener(this); // calling onClick() method
            imgBtnIroning = findViewById(R.id.imgBtnIroning);
            imgBtnIroning.setOnClickListener(this); // calling onClick() method
            imgBtnLaundry = findViewById(R.id.imgBtnLaundry);
            imgBtnLaundry.setOnClickListener(this); // calling onClick() method
            imgBtnRoomCleaning = findViewById(R.id.imgBtnRoomCleaning);
            imgBtnRoomCleaning.setOnClickListener(this); // calling onClick() method
            imgBtnHelp = findViewById(R.id.imgBtnHelp);
            imgBtnHelp.setOnClickListener(this); // calling onClick() method
            imgBtnMessages = findViewById(R.id.imgBtnMessages);
            imgBtnMessages.setOnClickListener(this); // calling onClick() method
            imgBtnSettings = findViewById(R.id.imgBtnSettings);
            imgBtnSettings.setOnClickListener(this); // calling onClick() method
            imgBtnProfile = findViewById(R.id.imgBtnProfile);
            imgBtnProfile.setOnClickListener(this); // calling onClick() method
            tvMainHeader = findViewById(R.id.tvMainHeader);
            ftList = new ArrayList<>();
            ftAdapter = new FundiTypesAdapter(this, ftList, this);

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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtnMyorders:
                Intent inMyOrders = new Intent(LandingPage.this, ScrollingActivity.class);
                startActivity(inMyOrders);
                break;
            case R.id.imgBtnHelp:
                Intent inHelp = new Intent(LandingPage.this, Help.class);
                startActivity(inHelp);
                break;
            case R.id.imgBtnMessages:
                Intent inMessages = new Intent(LandingPage.this, Messages.class);
                startActivity(inMessages);
                break;
            case R.id.imgBtnProfile:
                Intent inProfile = new Intent(LandingPage.this, My_Profile.class);
                startActivity(inProfile);
                break;
            case R.id.imgBtnSettings:
                Intent in = new Intent(LandingPage.this, TermsAndConditions.class);
                startActivity(in);
                break;
            case R.id.imgBtnLaundry:
                Intent inLaundry = new Intent(LandingPage.this, TermsAndConditions.class);
                startActivity(inLaundry);
                break;
            case R.id.imgBtnChefServices:
                Intent inChef = new Intent(LandingPage.this, TermsAndConditions.class);
                startActivity(inChef);
                break;
            case R.id.imgBtnRoomCleaning:
                Intent inCleaning = new Intent(LandingPage.this, TermsAndConditions.class);
                startActivity(inCleaning);
                break;
            case R.id.imgBtnIroning:
                Intent inIroning = new Intent(LandingPage.this, TermsAndConditions.class);
                startActivity(inIroning);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFundiTypeSelected(FundiTypes all) {
        //Toast.makeText(getApplicationContext(), "Selected: " + contact.getName() + ", " + contact.getPhone(), Toast.LENGTH_LONG).show();
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
//        swpRefresh.setRefreshing(false);
        // progressBar.setVisibility(View.GONE);
    }


    private void getInitData() {
        //swpRefresh.setRefreshing(true);
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
                    .make(tvMainHeader, "No internet connection! Check settings and try again.", Snackbar.LENGTH_LONG)
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

                if (v == 1) {
                    try {

                        RequestOptions options = new RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.imagenotfound)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .priority(Priority.HIGH)
                                .dontAnimate()
                                .dontTransform();
                        getApplicationContext();
                        Glide.with(this)
                                .load(separatorArrayA[2])
                                .apply(options)
                                .into(imgBtnChefServices);
                    } catch (NullPointerException df) {

                    }
                }
                if (v == 2) {
                    try {
                        //Toast.makeText(getApplicationContext(), "separatorArrayA[2]", Toast.LENGTH_LONG).show();
                        RequestOptions options = new RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.imagenotfound)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .priority(Priority.HIGH)
                                .dontAnimate()
                                .dontTransform();
                        getApplicationContext();
                        Glide.with(this)
                                .load(separatorArrayA[2])
                                .apply(options)
                                .into(imgBtnIroning);
                    } catch (NullPointerException df) {

                    }
                }
                if (v == 2) {
                    try {
                        //Toast.makeText(getApplicationContext(), "separatorArrayA[2]", Toast.LENGTH_LONG).show();
                        RequestOptions options = new RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.imagenotfound)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .priority(Priority.HIGH)
                                .dontAnimate()
                                .dontTransform();
                        getApplicationContext();
                        Glide.with(this)
                                .load(separatorArrayA[2])
                                .apply(options)
                                .into(imgBtnLaundry);
                    } catch (NullPointerException df) {

                    }
                }
                if (v == 3) {
                    try {
                        //Toast.makeText(getApplicationContext(), "separatorArrayA[2]", Toast.LENGTH_LONG).show();
                        RequestOptions options = new RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.imagenotfound)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .priority(Priority.HIGH)
                                .dontAnimate()
                                .dontTransform();
                        getApplicationContext();
                        Glide.with(this)
                                .load(separatorArrayA[2])
                                .apply(options)
                                .into(imgBtnRoomCleaning);
                    } catch (NullPointerException df) {

                    }
                }
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
    public void onBackPressed() {
        finish();
    }

}