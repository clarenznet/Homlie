package com.luns.neuro.mlkn;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.luns.neuro.mlkn.DataAdapter.DemographicsItems;
import com.luns.neuro.mlkn.DataAdapter.ServiceItems;
import com.luns.neuro.mlkn.library.ConnectionDetector;
import com.luns.neuro.mlkn.library.CustomVolleyRequest;
import com.luns.neuro.mlkn.library.SharedPrefManager;
import com.luns.neuro.mlkn.library.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Clarence on 9/4/2016.
 */
public class ServiceCalculator extends AppCompatActivity {
    private ProgressDialog loading;
    private static final String JSON_ARRAY_FT = "service_type";
    String strServiceRegion = "", strMyLocation = "", strMyPhoneNo = "";
    private List<ServiceItems> serviceitemList = new ArrayList<>();
    private List<DemographicsItems> demographicsList = new ArrayList<>();
    StringRequest stringRequest;
    public ImageLoader imageLoader;
    ArrayList<String> demographicsArray = new ArrayList<String>();
    private ImageButton btnSelectLocation;
    private Button btn_uploadOrder;
    private RecyclerView recyclerView, recycler_demographics;
    ArrayList<String> arrDeliveryOrderDetails = new ArrayList<String>();
    ArrayList<String> arrFinalCheckOut = new ArrayList<String>();
    LinearLayout llBottomSheet;
    BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView.Adapter serviceItemAdapter, demographicsItemAdapter;
    private int intCounterQuantity = 0;
    private TextView tvTotalPrice, tvOrderData;
    private String strUpl_ClientId = "", strUpl_PhoneNumber = "", strUpl_OrderData = "", strUpl_TotalPrice = "", strUpl_MyAddress = "", strUpl_MpesaCode = "";
    DatePicker datePickerInit;
    EditText edtSelectedDate;
    EditText edtSelectedTime;
    TimePicker timepickerInit;
    private String strMerchant = "", strOrderType = "";
    private TextView tvTaskLocation;
    private String strServerResponseResultData = "", strSelectedService = "";
    private ProgressBar progressBar;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    private String strFundiTypeR = "", strLatitudeR = "", strLongitudeR = "", strGeneralLocationR = "", strSpecificAddressR = "",
            strDescriptionR = "", strDateR = "", strTimeR = "", strTaskDetailsR = "", strTotalPriceR = "";
    private EditText edtTaskDescription, edtSpecificAddress;
    private String strUserPhonenumber = "", strUserFirebaseId = "", strUserEmailAddress = "", strUserAddress = "", strUserLatitude = "", strUserLongitude = "";
    //    private SQLiteManagerServiceCalculator sqLiteManagerServiceCalculator;
//    private SharedPreferences preferencesServiceCalculator;
    private String UPLOAD_URL = "https://www.instrov.com/malakane_init/mlkn_upload_request.php";
    //    private void showData() {
//
//        ArrayList cache = sqLiteManagerServiceCalculator.getData();
//        boolean isCacheExpire = false;
//        long cacheTime = preferencesServiceCalculator.getLong("cache", 0);
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
//        serviceitemList.clear();
//        serviceitemList = cache;
////        serviceItemAdapter.setFilter(serviceitemList);
////        this.stListFiltered.clear();
////        this.stListFiltered.addAll(data);
////        notifyDataSetChanged();
//
//        // progressBar.setVisibility(View.GONE);
//    }
    private String KEY_FRUSEREMAIL = "fr_useremail";
    private String KEY_FRUSERPHONE = "fr_userphone";
    private String KEY_FRFUNDITYPE = "fr_funditype";
    private String KEY_FRGENERALLOCATION = "fr_generallocation";
    private String KEY_FRLATITUDE = "fr_latitude";
    private String KEY_FRLONGITUDE = "fr_longitude";
    private String KEY_FRSPECIFICADDRESS = "fr_specificaddress";
    private String KEY_FRTASKDESCRIPTION = "fr_taskdescription";
    private String KEY_FRTASKDATE = "fr_taskdate";
    private String KEY_FRTASKTIME = "fr_tasktime";
    private String KEY_FRTASKDETAILS = "fr_taskdetail";
    private String KEY_FRTOTALPRICE = "fr_strtotalprice";
    private String strFtTitle = "", strId = "", strArticle = "", strDemographic = "", strPrice = "", strArticleIconUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.servicecalculator_container);
//        sqLiteManagerServiceCalculator = new SQLiteManagerServiceCalculator(this);
//        preferencesServiceCalculator = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            Bundle extras = getIntent().getExtras();
            strServerResponseResultData = extras.getString("strServerResponseResultData");
            strSelectedService = extras.getString("strSelectedService");
        } catch (NullPointerException f) {
            ///////////////////
        }
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(strSelectedService);
        recyclerView = findViewById(R.id.deliveryrecycler_view);
        //allordersList = new ArrayList<>();
        //deliveryOrdersAdapter = new DeliveryOrdersAdapter(this, deliveryOrdersList);
        serviceItemAdapter = new CardViewDataAdapter(serviceitemList);
        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(serviceItemAdapter);
        ///////////////////////////
        recycler_demographics = findViewById(R.id.recycler_demographics);
        demographicsItemAdapter = new CardViewDemographicsAdapter(demographicsList);
        RecyclerView.LayoutManager dLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_demographics.setLayoutManager(dLayoutManager);
        recycler_demographics.setItemAnimator(new DefaultItemAnimator());
        recycler_demographics.setAdapter(demographicsItemAdapter);
        recycler_demographics.addOnItemTouchListener(new ScrollingActivity.RecyclerTouchListener(getApplicationContext(), recycler_demographics, new ScrollingActivity.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                DemographicsItems dG = demographicsList.get(position);
                view.setSelected(true);
//                CardViewDataAdapter sm = new CardViewDataAdapter(serviceitemList);
////                sm.getFilter().filter(dG.getStrDemographic());
                //Toast.makeText(ServiceCalculator.this, dG.getStrDemographic(), Toast.LENGTH_SHORT).show();
                checkOut();
                showJSON(strServerResponseResultData, dG.getStrDemographic());
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        progressBar = findViewById(R.id.progressBar);
        arrFinalCheckOut = new ArrayList<String>();
        arrDeliveryOrderDetails = new ArrayList<String>();
        ////////////////////////////
        llBottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setPeekHeight(0);
        bottomSheetBehavior.setHideable(true);
//        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//            }
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//            }
//        });
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvOrderData = findViewById(R.id.tvOrderData);
        btn_uploadOrder = findViewById(R.id.btn_uploadOrder);
        btn_uploadOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmRequest();
            }
        });
        tvTaskLocation = findViewById(R.id.tvTaskLocation);
        edtTaskDescription = findViewById(R.id.edtTaskDescription);
        edtSpecificAddress = findViewById(R.id.edtSpecificAddress);
        edtSelectedTime = findViewById(R.id.edtSelectedTime);
        edtSelectedTime.setInputType(InputType.TYPE_NULL);
        edtSelectedTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View n, boolean hasFocus1) {
                if (hasFocus1)
                    selectTime();

            }
        });
        edtSelectedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime();

            }
        });
        edtSelectedDate = findViewById(R.id.edtSelectedDate);
        edtSelectedDate.setInputType(InputType.TYPE_NULL);
        edtSelectedDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus2) {
                if (hasFocus2)
                    selectDate();
            }
        });
        edtSelectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });
//        ArrayList cache = sqLiteManagerServiceCalculator.getData();
//        boolean isCacheExpire = false;
//        long cacheTime = preferencesServiceCalculator.getLong("cache", 0);
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
//            showJSON(strServerResponseResultData,"All Items");
//        }

        showJSON(strServerResponseResultData, "All Items");

        btnSelectLocation = findViewById(R.id.btnSelectLocation);
        btnSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmAddressSelection();
            }
        });
//////////////////////////////////////////////////////
        getSessionInfo();
//////////////////////////////////////////////////////

    }

    private void getSessionInfo() {
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        strUserPhonenumber = user.getPhonenumber();
        strUserFirebaseId = user.getFirebaseid();
        strUserEmailAddress = user.getUser_priviledge();
        strUserAddress = user.getUser_address();
        strUserLatitude = user.getUser_latitude();
        strUserLongitude = user.getUser_longitude();
        //Toast.makeText(this, ""+strUserAddress+"|"+strUserLatitude+"|"+strUserLongitude, Toast.LENGTH_LONG).show();
        tvTaskLocation.setText(strUserAddress + "Lat: " + strUserLatitude + "  Long: " + strUserLongitude);

    }

    private void confirmAddressSelection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Select new location address?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                }

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
            }
        });
        AlertDialog alert = builder.create();
        alert.getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        alert.show();
    }

    public String getCurrentTime() {
        String currentTime = timepickerInit.getCurrentHour() + ":" + timepickerInit.getCurrentMinute();
        return currentTime;
    }

    public String getCurrentDate() {
        StringBuilder builder = new StringBuilder();
        builder.append((datePickerInit.getMonth() + 1) + "/");//month is 0 based
        builder.append(datePickerInit.getDayOfMonth() + "/");
        builder.append(datePickerInit.getYear());
        return builder.toString();
    }

    public void selectTime() {
        final Dialog d = new Dialog(ServiceCalculator.this, android.R.style.DeviceDefault_Light_ButtonBar);
        d.setTitle("Select time");
        d.setContentView(R.layout.dialog_timepicker);
        timepickerInit = d.findViewById(R.id.timePickerInit);
        //Uncomment the below line of code for 24 hour view
        timepickerInit.setIs24HourView(true);
        edtSelectedTime.setText(getCurrentTime());
        Button btnDoneSelectTime = d.findViewById(R.id.btnDoneSelectTime);
        btnDoneSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtSelectedTime.setText(getCurrentTime());
                //Toast.makeText(ServiceCalculator.this, "Selected time: "+getCurrentTime(), Toast.LENGTH_LONG).show();
                d.cancel();
            }
        });
        d.show();
    }

    public void selectDate() {
        final Dialog d = new Dialog(ServiceCalculator.this, android.R.style.DeviceDefault_Light_ButtonBar);
        d.setTitle("Select date");
        d.setContentView(R.layout.dialog_datepicker);
        datePickerInit = d.findViewById(R.id.datePickerInit);
        edtSelectedDate.setText(getCurrentDate());
        Button btnDoneSelectDate = d.findViewById(R.id.btnDoneSelectDate);
        btnDoneSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtSelectedDate.setText(getCurrentDate());
                //Toast.makeText(ServiceCalculator.this, "Selected date: "+getCurrentDate(), Toast.LENGTH_LONG).show();
                d.cancel();
            }
        });
        d.show();
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    ////////////////////////////////////////////

    private void confirmRequest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm request?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                // TODO Auto-generated method stub
                strFundiTypeR = strSelectedService;
                strLatitudeR = strUserLatitude;
                strLongitudeR = strUserLongitude;
                strGeneralLocationR = tvTaskLocation.getText().toString().trim();
                strSpecificAddressR = edtSpecificAddress.getText().toString().trim();
                strDescriptionR = edtTaskDescription.getText().toString().trim();
                strDateR = edtSelectedDate.getText().toString().trim();
                strTimeR = edtSelectedTime.getText().toString().trim();
                strTaskDetailsR = tvOrderData.getText().toString().trim();
                strTotalPriceR = tvTotalPrice.getText().toString().trim();
                if (TextUtils.isEmpty(strGeneralLocationR)) {
                    tvTaskLocation.setError("Please select a geolocation");
                    tvTaskLocation.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(strSpecificAddressR)) {
                    edtSpecificAddress.setError("Please enter a specific location e.g house no or building");
                    edtSpecificAddress.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(strDateR)) {
                    edtSelectedDate.setError("Please select a date");
                    edtSelectedDate.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(strTimeR)) {
                    edtSelectedTime.setError("Please select time");
                    edtSelectedTime.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(strTaskDetailsR)) {
                    Snackbar.make(tvOrderData, "You have not selected any item.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                uploadData();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
            }
        });
        AlertDialog alert = builder.create();
        alert.getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        alert.show();
    }

    private void uploadData() {
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            loading = ProgressDialog.show(this, "Uploading", "Please wait...", false, false);
            stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            loading.dismiss();
                            Toast.makeText(ServiceCalculator.this, "ticket #" + s + " created successfully ", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), DetailsScreen.class);
                            intent.putExtra("strTicketCode", s);
                            startActivity(intent);
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //Dismissing the progress dialog
                            loading.dismiss();
                            ////////////////////
                            if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.error_network_timeout),
                                        Toast.LENGTH_LONG).show();
                            } else if (volleyError instanceof AuthFailureError) {
                                //
                                Toast.makeText(ServiceCalculator.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();

                            } else if (volleyError instanceof ServerError) {
                                //
                                Toast.makeText(ServiceCalculator.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();

                            } else if (volleyError instanceof NetworkError) {
                                //
                                Toast.makeText(ServiceCalculator.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();

                            } else if (volleyError instanceof ParseError) {
                                Toast.makeText(ServiceCalculator.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();

                            }

                            //stringRequest .setRetryPolicy(new DefaultRetryPolicy(
                            //  30000,
                            //    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            //      DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            //Showing toast
                            //Toast.makeText(Upload_Fraudster.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
                    Map<String, String> params = new Hashtable<String, String>();
                    //Adding parameters
                    params.put(KEY_FRUSEREMAIL, user.getUser_priviledge());
                    params.put(KEY_FRUSERPHONE, user.getPhonenumber());
                    params.put(KEY_FRFUNDITYPE, strFundiTypeR);
                    params.put(KEY_FRGENERALLOCATION, strGeneralLocationR);
                    params.put(KEY_FRLATITUDE, strLatitudeR);
                    params.put(KEY_FRLONGITUDE, strLongitudeR);
                    params.put(KEY_FRSPECIFICADDRESS, strSpecificAddressR);
                    params.put(KEY_FRTASKDESCRIPTION, strDescriptionR);
                    params.put(KEY_FRTASKDATE, strDateR);
                    params.put(KEY_FRTASKTIME, strTimeR);
                    params.put(KEY_FRTASKDETAILS, strTaskDetailsR);
                    params.put(KEY_FRTOTALPRICE, strTotalPriceR);


                    //returning parameters
                    return params;
                }
            };

            //Creating a Request Queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            //requestQueue.add(request);
            //DefaultRetryPolicy  retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            //requestQueue.setRetryPolicy(retryPolicy);
            //.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //Adding request to the queue
            requestQueue.add(stringRequest);
        } else {
            //Snackbar.make(recyclerView, "No Internet connection, check settings and try again.", Snackbar.LENGTH_LONG)
            //      .setAction("Action", null).show();
            Snackbar snackbar = Snackbar
                    .make(llBottomSheet, "No internet connection! Check settings and try again.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            uploadData();
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

    public void showJSON(String strServerResponseResultData, String strdg) {
        try {
            //sqLiteManagerServiceCalculator.deleteOldCache();
            serviceitemList.clear();
            JSONObject jsonObject = new JSONObject(strServerResponseResultData);
            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY_FT);
            //Log.e(TAG, "Array: " + ""+result);
            ///////////////////////////
            serviceitemList.clear();
            demographicsList.clear();
            demographicsArray.add(strdg);
            for (int i = 0; i < result.length(); i++) {
                JSONObject serverData = result.getJSONObject(i);
                strFtTitle = serverData.getString("jbfundititle");
                strId = serverData.getString("jbid");
                strArticle = serverData.getString("svc_article");
                strDemographic = serverData.getString("svc_demographic");
                strPrice = serverData.getString("svc_price");
                strArticleIconUrl = serverData.getString("svc_articleiconurl");
                if (strFtTitle.equals(strSelectedService)) {
                    boolean blnLooperCheck = false;
                    for (String strAddedItem : arrDeliveryOrderDetails) {
                        String[] separatorArray = strAddedItem.split("@!@");
                        if (strId.equals(separatorArray[0]) && strArticle.equals(separatorArray[1]) && strDemographic.equals(separatorArray[4]) && (strDemographic.equals(strdg) || strdg.equals("All Items"))) {
                            blnLooperCheck = true;
                            int intQVofItem = Integer.parseInt(separatorArray[2]);
                            ServiceItems sItems2 = new ServiceItems(strId, strArticle, strDemographic, strPrice, strArticleIconUrl, intQVofItem, false);
                            //sqLiteManagerServiceCalculator.addData(sItems2);
                            serviceitemList.add(sItems2);
                            serviceItemAdapter.notifyDataSetChanged();
                        }
                    }
                    if (!blnLooperCheck && (strDemographic.equals(strdg) || strdg.equals("All Items"))) {
                        ServiceItems sItems = new ServiceItems(strId, strArticle, strDemographic, strPrice, strArticleIconUrl, 0, false);
                        //sqLiteManagerServiceCalculator.addData(sItems);
                        serviceitemList.add(sItems);
                        serviceItemAdapter.notifyDataSetChanged();
                    }
                    CardViewDataAdapter cvd = new CardViewDataAdapter(serviceitemList);
                    cvd.notifyDataSetChanged();
                    demographicsArray.add(strDemographic);
                }
            }
            ////Cleaning Source Knowledge Elements
            Set<String> sourceHash = new HashSet<>();
            sourceHash.addAll(demographicsArray);
            TreeSet myTreeSet = new TreeSet();
            myTreeSet.addAll(sourceHash);
            demographicsArray.clear();
            demographicsArray.addAll(myTreeSet);
            int v = 1;
            for (String strDemographicElement : demographicsArray) {
                //String[] separatorArrayA = strServiceElement.split("@@");
                DemographicsItems dIs = new DemographicsItems("" + v, strDemographicElement);
                demographicsList.add(dIs);
                demographicsItemAdapter.notifyDataSetChanged();
                v++;
            }
            //preferencesServiceCalculator.edit().putLong("cache", new Date().getTime()).apply();
            //           serviceItemAdapter.setFilter(serviceitemList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //    @Override
    public void onOrderSelected(ServiceItems all) {
        //Toast.makeText(getApplicationContext(), "Selected: " + contact.getName() + ", " + contact.getPhone(), Toast.LENGTH_LONG).show();
    }

    private void checkOut() {
        String data = "", strTotlaPrice = "";
        int intTotlaPrice = 0;
        String strArrayOrder = "";
        List<ServiceItems> stList = ((CardViewDataAdapter) serviceItemAdapter).getSelectedUnitsist();
        for (int i = 0; i < stList.size(); i++) {
            ServiceItems singleOrder = stList.get(i);
            data = data + "\n" + singleOrder.getStrArticle() + " , " + singleOrder.getIntQuantityValue();
            //strArrayOrder= "Item: "+singleOrder.getStrArticle()+"   No: x"+ singleOrder.getIntQuantityValue()+"   @Ksh: "+ singleOrder.getStrPrice();
            for (int x = 0; x < arrDeliveryOrderDetails.size(); x++) {
                String[] separatorArray = arrDeliveryOrderDetails.get(x).split("@!@");
                Log.e("XQXQXQ", "Array: " + "" + arrDeliveryOrderDetails.get(x));
                if (separatorArray[0].contains(singleOrder.getStrId()) && separatorArray[1].contains(singleOrder.getStrArticle())
                        && separatorArray[3].contains(singleOrder.getStrPrice()) && separatorArray[4].contains(singleOrder.getStrDemographic())) {
                    arrDeliveryOrderDetails.remove(arrDeliveryOrderDetails.get(x));
                }
            }
            if (singleOrder.getIntQuantityValue() != 0) {

                strArrayOrder = singleOrder.getStrId() + "@!@" + singleOrder.getStrArticle() + "@!@" + singleOrder.getIntQuantityValue() + "@!@" + singleOrder.getStrPrice() + "@!@" + singleOrder.getStrDemographic();
                arrDeliveryOrderDetails.add(strArrayOrder);
                int price = Integer.valueOf(singleOrder.getStrPrice());
                int number = Integer.valueOf(singleOrder.getIntQuantityValue());
                intTotlaPrice = intTotlaPrice + price * number;
            }
        }
        String dataf = "";
        tvOrderData.setText("");
        ////Cleaning Source Knowledge Elements
        Set<String> sourceHash1 = new HashSet<>();
        sourceHash1.addAll(arrDeliveryOrderDetails);
        arrDeliveryOrderDetails.clear();
        arrDeliveryOrderDetails.addAll(sourceHash1);


        for (String strElement : arrDeliveryOrderDetails) {
            String[] separatorArray = strElement.split("@!@");
            String strItemUt = "Item:" + separatorArray[1] + " No: x" + separatorArray[2] + " @Ksh:" + separatorArray[3] + " Category:" + separatorArray[4];
            dataf = dataf + strItemUt + "\n";
            tvOrderData.setText(tvOrderData.getText() + strItemUt + "\n\n");
        }

        tvTotalPrice.setText("" + intTotlaPrice);
    }

    @Override
    public void onBackPressed() {
        finish();
        //overridePendingTransition( 0,R.anim.slideinright);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_checkout, menu);
        return true;
    }
    //////////////////////////////////////

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    ////////////////////////////////////////////
    /*Adapter for the RecycleView*/
    public class CardViewDataAdapter extends RecyclerView.Adapter<CardViewDataAdapter.ViewHolder> implements Filterable {
        private List<ServiceItems> stList;
        private List<ServiceItems> stListFiltered;
        // private CardViewDataAdapterListener listener;

        //        CardViewDataAdapterListener listener
        public CardViewDataAdapter(List<ServiceItems> stList) {
            this.stList = stList;
            //this.listener = listener;
            this.stListFiltered = stList;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_row, null);
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
            //final int pos = position;
            final ServiceItems sIt = stListFiltered.get(position);
            viewHolder.tv_articlename.setText(sIt.getStrArticle());
            viewHolder.tv_articleprice.setText("Ksh: " + sIt.getStrPrice());
            viewHolder.tv_articlecat.setText(sIt.getStrDemographic());
            imageLoader = CustomVolleyRequest.getInstance(getApplicationContext()).getImageLoader();
            //          imageLoader.get(sIt.getStrArticleIconUrl(), ImageLoader.getImageListener(viewHolder.nimv_article_image, R.drawable.placer, android.R.drawable.ic_dialog_alert));
            //viewHolder.nimv_article_image.setImageUrl(sIt.getStrArticleIconUrl(), imageLoader);
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.imagenotfound)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    .dontAnimate()
                    .dontTransform();

            Glide.with(getApplicationContext())
                    .load(sIt.getStrArticleIconUrl())
                    .apply(options)
                    .into(viewHolder.nimv_article_image);
            viewHolder.edtQuantity.setEnabled(false);
            viewHolder.edtQuantity.setText("" + sIt.getIntQuantityValue());
            viewHolder.btnSub.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intCounterQuantity = stListFiltered.get(position).getIntQuantityValue();
                    if (intCounterQuantity > 0) {
                        intCounterQuantity--;
                    }
                    stListFiltered.get(position).setIntQuantityValue(intCounterQuantity);
                    viewHolder.edtQuantity.setText("" + intCounterQuantity); // here in textView the percent will be shown
                    // checkOut();
                }
            });
            viewHolder.btnAdd.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intCounterQuantity = stListFiltered.get(position).getIntQuantityValue();
                    intCounterQuantity++;
                    stListFiltered.get(position).setIntQuantityValue(intCounterQuantity);
                    viewHolder.edtQuantity.setText("" + intCounterQuantity); // here in textView the percent will be shown
                    //   checkOut();
                }
            });
        }

        @Override
        public int getItemCount() {
            return stListFiltered.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    Log.e("Querys", "Array: " + "" + charString);
                    if (charString.isEmpty()) {
                        stListFiltered = stList;
                        Log.e("Empty", "Array: " + "" + stListFiltered);
                    } else {
                        List<ServiceItems> filteredList = new ArrayList<>();
                        for (ServiceItems row : stList) {
                            if (row.getStrDemographic().equals(charString)) {
                                filteredList.add(row);
                                Log.e("ForF", "Array: " + "" + row.getStrArticle() + "" + row.getStrPrice() + "" + row.getStrDemographic() + "////QueryS////" + charString + "////demographics////" + row.getStrDemographic());
                            }
                        }
                        stListFiltered = filteredList;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = stListFiltered;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    //serviceitemList.clear();
                    stListFiltered = (ArrayList<ServiceItems>) filterResults.values;
                    notifyDataSetChanged();//notifyDataSetChanged();
                }
            };
        }

        public void setFilter(List<ServiceItems> data) {
            this.stListFiltered.clear();
            this.stListFiltered.addAll(data);
            notifyDataSetChanged();
            //return true;
        }

        public List<ServiceItems> getSelectedUnitsist() {
            return stListFiltered;
        }

        public abstract class CardViewDataAdapterListener {
            abstract void onServiceItemSelected(ServiceItems all);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_articlename, tv_articleprice, tv_articlecat;
            public ImageView nimv_article_image;
            public EditText edtQuantity;
            public Button btnSub, btnAdd;

            public ViewHolder(View itemLayoutView) {
                super(itemLayoutView);
                tv_articlename = itemLayoutView.findViewById(R.id.tv_articlename);
                tv_articleprice = itemLayoutView.findViewById(R.id.tv_articleprice);
                tv_articlecat = itemLayoutView.findViewById(R.id.tv_articlecat);
                edtQuantity = itemLayoutView.findViewById(R.id.edtdeliveryNoOfItem);
                nimv_article_image = itemLayoutView.findViewById(R.id.nimv_article_image);
                btnSub = itemLayoutView.findViewById(R.id.btnSub);
                btnAdd = itemLayoutView.findViewById(R.id.btnAdd);
            }
        }

    }

    //////////////////////////////////////
    //@Override
//    public void onServiceItemSelected(ServiceItems all) {
//        //Toast.makeText(getApplicationContext(), "Selected: " + contact.getName() + ", " + contact.getPhone(), Toast.LENGTH_LONG).show();
//    }
    ////////////////////////////////////////////menu demographics
    /*Adapter for the RecycleView*/
    public class CardViewDemographicsAdapter extends RecyclerView.Adapter<CardViewDemographicsAdapter.ViewHolder> {
        private List<DemographicsItems> demographicsList;

        public CardViewDemographicsAdapter(List<DemographicsItems> demographicsUnits) {
            this.demographicsList = demographicsUnits;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemLayoutView2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_listrow, null);
            ViewHolder viewHolder2 = new ViewHolder(itemLayoutView2);
            return viewHolder2;
        }


        @Override
        public void onBindViewHolder(final ViewHolder viewHolder2, final int position) {
            final int pos = position;
            viewHolder2.btnDemographics.setText(demographicsList.get(position).getStrDemographic());
//           viewHolder2.btnDemographics.setOnClickListener(new Button.OnClickListener() {
//               @Override
//               public void onClick(View view) {
//                   //strSelectedDemographics=demographicsList.get(position).getStrDemographic();
//                   Toast.makeText(ServiceCalculator.this, ""+demographicsList.get(position).getStrDemographic(), Toast.LENGTH_SHORT).show();
////                   viewHolder2.btnDemographics.setBackgroundColor();
//                       // filter recycler view when query submitted
////                   ServiceCalculator.this.serviceItemAdapter.getFilter().filter(s);
////                   ftAdapter.getFilter().filter(query);
//                   CardViewDataAdapter sms = new CardViewDataAdapter(serviceitemList);
//                   sms.getFilter().filter(demographicsList.get(position).getStrDemographic());
//
//               }
//           });
        }

        @Override
        public int getItemCount() {
            return demographicsList.size();
        }

        public List<DemographicsItems> getSelectedDemographics() {
            return demographicsList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView btnDemographics;

            public ViewHolder(View itemLayoutView) {
                super(itemLayoutView);
                btnDemographics = itemLayoutView.findViewById(R.id.btnDemographics);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_checkout) {
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                tvTaskLocation.setText("");
                getSessionInfo();
                checkOut();
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
