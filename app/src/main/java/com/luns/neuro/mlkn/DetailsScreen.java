package com.luns.neuro.mlkn;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.luns.neuro.mlkn.Model.AccessToken;
import com.luns.neuro.mlkn.Model.STKPush;
import com.luns.neuro.mlkn.Services.DarajaApiClient;
import com.luns.neuro.mlkn.Services.Utils;
import com.luns.neuro.mlkn.library.ConnectionDetector;
import com.luns.neuro.mlkn.library.CustomVolleyRequest;
import com.luns.neuro.mlkn.library.SessionManager;
import com.luns.neuro.mlkn.library.SharedPrefManager;
import com.luns.neuro.mlkn.library.User;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import timber.log.Timber;

import static com.luns.neuro.mlkn.Interceptor.Constants.BUSINESS_SHORT_CODE;
import static com.luns.neuro.mlkn.Interceptor.Constants.CALLBACKURL;
import static com.luns.neuro.mlkn.Interceptor.Constants.PARTYB;
import static com.luns.neuro.mlkn.Interceptor.Constants.PASSKEY;
import static com.luns.neuro.mlkn.Interceptor.Constants.TRANSACTION_TYPE;

public class DetailsScreen extends AppCompatActivity implements OnMapReadyCallback {
    ///start of mpesa privates

    private DarajaApiClient mApiClient;
    private ProgressDialog mProgressDialog;
    //@BindView(R.id.etAmount)
    //EditText mAmount;
    //@BindView(R.id.etPhone)EditText mPhone;
  //  @BindView(R.id.btnPay)
//    Button mPay;

    ///end of mpesa privates
    private Button btnAcceptRequest;
    private TextView tvTicketCompleted;
    private String strTicketId="",strParentClass="",strOrderSummary="", strOrderTyp="",strOrderTim="";
    private TextView tvOrderSummary,tvOrderTyp, tvTtlCost,tvSpecificLocation,tvGenLocation,tvDateTime,tvCreatedAt,tvToNote;
    private GoogleMap mMap;
    private float fLat=0,fLong=0;
    private AVLoadingIndicatorView avi;
    private ImageView btnCall,btnMessage;
    private TextView tvStatusDet,tvAgentName, tvAgentId,tvRating;
    private NetworkImageView  nivProfilePhoto;
    private View vw_agent_card,lytavi;
    LinearLayout llBottomSheetPayment;
    BottomSheetBehavior bottomSheetBehavior;
    private EditText edtPayingPhoneNumnber;
    private TextView tvInvoiceTotal,tvPymntTicketNo;
    private Button btnEditMyPhoneNumber;
    private RatingBar ratingbar;
    private Button btnSendPayment;
    private String strUpldPymntTicketNo="",strUpldPymntInvoiceTotal="",strUpldPymntPhoneNumber="",strUpldPymntRating="";

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_screen_container);
//        String indicator=getIntent().getStringExtra("indicator");
//        avi= (AVLoadingIndicatorView) findViewById(R.id.avi);
//        avi.setIndicator(indicator);
//        avi.smoothToShow();
        try {
            Bundle extras = getIntent().getExtras();
            //strParentClass = extras.getString("parentclass");
            //strOrderSummary= extras.getString("strOrderSummary");
            //strOrderTyp = extras.getString("strOrderTyp");
            strTicketId = extras.getString("strTicketCode");
        }catch (NullPointerException f){
            ///////////////////
        }
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("ticket details");
        ////////////////////////////
        llBottomSheetPayment = findViewById(R.id.bottom_sheet_payment);
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheetPayment);
        bottomSheetBehavior.setPeekHeight(0);
        bottomSheetBehavior.setHideable(true);

        tvTicketCompleted=findViewById(R.id.tvTicketCompleted);
        btnAcceptRequest=findViewById(R.id.btnAcceptRequest);
        btnAcceptRequest.setText("Complete Ticket");
        //Toast.makeText(getApplicationContext(),strTicketId,Toast.LENGTH_LONG).show();
        btnAcceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // confirmPaymentMethod();
                tvInvoiceTotal.setText(strTcktTtlCost);
                tvPymntTicketNo.setText(strTcktCode);
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

//                Intent in = new Intent(getApplicationContext(), CreditCardPay.class);
  //              startActivity(in);
            }
        });
        tvOrderSummary= findViewById(R.id.tvOrderSummary);
        //tvOrderSummary.setText(strOrderSummary);
        tvOrderTyp= findViewById(R.id.tvOrderTyp);
        //tvOrderTyp.setText(strOrderTyp);
        tvTtlCost = findViewById(R.id.tvOrderTime);
        //tvTtlCost.setText(strOrderTim);
        tvSpecificLocation=findViewById(R.id.tvSpecificLocation);
        tvGenLocation = findViewById(R.id.tvGenLocation);
        tvDateTime = findViewById(R.id.tvDateTime);
        tvCreatedAt= findViewById(R.id.tvCreatedAt);
        tvToNote= findViewById(R.id.tvToNote);

        tvStatusDet = findViewById(R.id.tvStatusDet);
        nivProfilePhoto= findViewById(R.id.nivProfilePhoto);
        tvAgentName= findViewById(R.id.tvAgentName);
        tvAgentId= findViewById(R.id.tvAgentId);
        tvRating= findViewById(R.id.tvRating);
        btnMessage=findViewById(R.id.btnMessage);
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textAgent(strTcktAgntPhoneNo);
            }
        });

        btnCall=findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAgent(strTcktAgntPhoneNo);
            }
        });
        vw_agent_card = findViewById(R.id.vw_agent_card);
        lytavi = findViewById(R.id.lytavi);

        getTicketDetails();
        //avi.show();
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        edtPayingPhoneNumnber=findViewById(R.id.edtPayingPhoneNumnber);
        edtPayingPhoneNumnber.setEnabled(false);
        edtPayingPhoneNumnber.setText(user.getPhonenumber().substring(1));

        tvInvoiceTotal=findViewById(R.id.tvInvoiceTotal);
        tvPymntTicketNo=findViewById(R.id.tvPymntTicketNo);
        btnEditMyPhoneNumber=findViewById(R.id.btnEditMyPhoneNumber);
        btnEditMyPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPhonenumberDialog();
            }
        });
        ratingbar = findViewById(R.id.rtbAgent);
        btnSendPayment = findViewById(R.id.btnSendPayment);
        btnSendPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmTransactionDialog();
            }
        });
        ///some mpesa api stuff
        mpesaStuffOncreate();
    }

    private void edtPhonenumberDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("do you want to edit the phone number used to pay this ticket?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                // TODO Auto-generated method stub
                edtPayingPhoneNumnber.setEnabled(true);
                edtPayingPhoneNumnber.setSelection(12);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub

            }
        });
        AlertDialog alert = builder.create();
        //alert.getWindow().setBackgroundDrawableResource(R.color.btn_bg);
        alert.show();
    }
    private void confirmTransactionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm transaction...");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                strUpldPymntTicketNo=tvPymntTicketNo.getText().toString();
                strUpldPymntInvoiceTotal=tvInvoiceTotal.getText().toString();
                strUpldPymntPhoneNumber=edtPayingPhoneNumnber.getText().toString();
                strUpldPymntRating=String.valueOf(ratingbar.getRating());
                postPaymentData();
                //               Toast.makeText(getApplicationContext(), rating, Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub

            }
        });
        AlertDialog alert = builder.create();
        //alert.getWindow().setBackgroundDrawableResource(R.color.btn_bg);
        alert.show();

    }

    public void hideClick(View view) {
        avi.hide();
        // or avi.smoothToHide();
    }

    public void showClick(View view) {
//*        avi.show();*/

        avi.smoothToShow();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(fLat,fLong);
        mMap.addMarker(new MarkerOptions().position(sydney).title(strTcktGenLoc));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(fLat,fLong), 15));

       // mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(15));
    }
    private void confirmPaymentMethod(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Select payment method?");
        builder.setCancelable(false);
        AlertDialog alert = builder.create();

        alert.getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        alert.show();
    }

    public static final String DATA_URL = "https://www.homlie.co.ke/malakane_init/mlkn_ticketdetails.php?strpostid=";
    public static final String JSON_ARRAY = "result";
    public static final String KEY_TCKTID= "strTcktId";
    public static final String KEY_TCKTUSREMAIL= "strTcktUsrEmail";
    public static final String KEY_TCKTUSRPHONENUMBER = "strTcktUsrPhoneNumber";
    public static final String KEY_TCKTTYPE = "strTcktType";
    public static final String KEY_TCKTGENLOC = "strTcktGenLoc";
    public static final String KEY_TCKTSPCFADDR = "strTcktSpcfAddr";
    public static final String KEY_TCKTLAT = "strTcktLat";
    public static final String KEY_TCKTLONG = "strTcktLong";
    public static final String KEY_TCKTNOTES = "strTcktNotes";
    public static final String KEY_TCKTDETAILS = "strTcktDetails";
    public static final String KEY_TCKTDATE = "strTcktDate";
    public static final String KEY_TCKTTIME = "strTcktTime";
    public static final String KEY_TCKTTTLCOST = "strTcktTtlCost";
    public static final String KEY_TCKTCREATEDAT = "strTcktCreatedAt";
    public static final String KEY_TCKTSTATUS = "strTcktStatus";
    public static final String KEY_TCKTCODE = "strTcktCode";


    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    SessionManager session;
    private String strEmail="",strTcktStatus="",strTcktCode="";
    private  String strTcktId="",strTcktUsrEmail="",strTcktUsrPhoneNumber="",strTcktType="",strTcktGenLoc="",strTcktSpcfAddr="",
            strTcktLat="",strTcktLong="",strTcktNotes="",strTcktDetails="",strTcktDate="",strTcktTime="",strTcktTtlCost="",strTcktCreatedAt="";

    private void getTicketDetails() {
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
            String strUserPhonenumber = user.getPhonenumber();
            strEmail =user.getUser_priviledge();
            String strBulk=strEmail+","+ strTicketId;
            String url = DATA_URL+strBulk;
            StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //loading.dismiss();
                    //Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                    showJSON(response);
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //loading.dismiss();

                            //Showing toast
                            if(volleyError instanceof TimeoutError ||volleyError instanceof NoConnectionError){
                                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.error_network_timeout),
                                        Toast.LENGTH_LONG).show();
                            }else if (volleyError instanceof AuthFailureError){
                                //
                                Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();

                            }else if (volleyError instanceof ServerError){
                                //
                                Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();

                            }else if (volleyError instanceof NetworkError){
                                //
                                Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();

                            }else if (volleyError instanceof ParseError){
                                Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();

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
                    .make(tvOrderSummary, "No internet connection! Check settings and try again.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            getTicketDetails();
                        }
                    });

// Changing message text color
            snackbar.setActionTextColor(Color.RED);

// Changing action button text color
            View sbView = snackbar.getView();
//            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
  //          textView.setTextColor(Color.YELLOW);
            snackbar.show();

        }
    }

    private void showJSON(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);
            //JSONObject serverData = result.getJSONObject(0);
            // looping through All Products
            //for (int i = 0; i < result.length(); i++) {
            // JSONObject c = searchResultsArray.getJSONObject(i);
            JSONObject serverData = result.getJSONObject(0);

            strTcktId = serverData.getString(KEY_TCKTID);
            strTcktCode = serverData.getString(KEY_TCKTCODE);
            strTcktUsrEmail = serverData.getString(KEY_TCKTUSREMAIL);
            strTcktUsrPhoneNumber = serverData.getString(KEY_TCKTUSRPHONENUMBER);
            strTcktType = serverData.getString(KEY_TCKTTYPE);
            strTcktGenLoc = serverData.getString(KEY_TCKTGENLOC);

            strTcktSpcfAddr= serverData.getString(KEY_TCKTSPCFADDR);
            strTcktLat = serverData.getString(KEY_TCKTLAT);
            strTcktLong = serverData.getString(KEY_TCKTLONG);
            strTcktNotes = serverData.getString(KEY_TCKTNOTES);
            strTcktDetails = serverData.getString(KEY_TCKTDETAILS);
            strTcktDate = serverData.getString(KEY_TCKTDATE);
            strTcktTime=serverData.getString(KEY_TCKTTIME);
            strTcktTtlCost=serverData.getString(KEY_TCKTTTLCOST);
            strTcktCreatedAt = serverData.getString(KEY_TCKTCREATEDAT);
            strTcktStatus = serverData.getString(KEY_TCKTSTATUS);

            tvOrderSummary.setText(strTcktDetails);
            tvTtlCost.setText(strTcktTtlCost);
            tvSpecificLocation.setText(strTcktSpcfAddr);
            tvGenLocation.setText(strTcktGenLoc);
            tvDateTime.setText(strTcktDate +" "+ strTcktTime);
            tvOrderTyp.setText(strTcktType +" ticket: "+strTcktCode);
            tvCreatedAt.setText(strTcktCreatedAt);
            tvToNote.setText(strTcktNotes);
            fLat = Float.parseFloat(strTcktLat);
            fLong = Float.parseFloat(strTcktLong);
            tvStatusDet.setText(strTcktStatus);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            getAgentStatus();
      } catch (JSONException e) {
            e.printStackTrace();
        }
        // textViewResult.setText("Name:\t"+name+"\nAddress:\t" +address+ "\nVice Chancellor:\t"+ vc);
    }

    public static final String AGENTSTATUS_URL = "https://www.homlie.co.ke/malakane_init/mlkn_agentstatus.php?strpostid=";
    public static final String JSON_ARRAY2 = "result";
    public static final String KEY_ATCKTSTATUS = "strTcktStatus";
    public static final String KEY_ATCKTAGENTPHONENO = "strTcktAgntPhoneNo";
    public static final String KEY_ATCKTAGENTEMAIL = "strTcktAgntEmail";
    public static final String KEY_ATCKTAGENTTAKENAT = "strTcktAgntTakenAt";
    public static final String KEY_ATCKTFULLNAME = "strFndFullName";
    public static final String KEY_ATCKTFNDID = "strFndId";
    public static final String KEY_ATCKTFNDIDNO = "strFndIdno";
    public static final String KEY_ATCKTFNDDP = "strFndDp";
    public static final String KEY_ATCKTRATINGSCORE = "strFndRatingScore";
    public static final String KEY_ATCKTFNDABOUT = "strFndAbout";


    //nhjg
    private void getAgentStatus(){
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            String strBulk=strTcktId+","+ strTcktCode;
            String url = AGENTSTATUS_URL+strBulk;
            StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //loading.dismiss();
                    //Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_SHORT).show();

                    if (response.trim().equals("Error"))
//                        timeToCallBack();

                        //starting our task which update textview every 2000 ms
                        new RefreshTask().execute();

                    else
                        showJSONAResponse(response);
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //loading.dismiss();
                            //Showing toast
                            if(volleyError instanceof TimeoutError ||volleyError instanceof NoConnectionError){
                                try {
                                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.error_network_timeout),
                                            Toast.LENGTH_LONG).show();
                                }catch (NullPointerException dsfsd){

                                }
                            }else if (volleyError instanceof AuthFailureError){
                                try {
                                    Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                                }catch (NullPointerException dsfsd){

                                }
                            }else if (volleyError instanceof ServerError){
                                try {
                                    Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                                }catch (NullPointerException dsfsd){

                                }
                            }else if (volleyError instanceof NetworkError){
                                try {
                                    Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                                }catch (NullPointerException dsfsd){

                                }
                            }else if (volleyError instanceof ParseError){
                                try {
                                    Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                                }catch (NullPointerException dsfsd){

                                }
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
                    .make(tvOrderSummary, "No internet connection! Check settings and try again.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            getAgentStatus();
                        }
                    });
// Changing message text color
            snackbar.setActionTextColor(Color.RED);

// Changing action button text color
            View sbView = snackbar.getView();
//            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            //          textView.setTextColor(Color.YELLOW);
            snackbar.show();

        }
    }
    private String strATcktStatus="",strTcktAgntPhoneNo="",strTcktAgntEmail="",strTcktAgntTakenAt="",strFndFullName="",strFndId="",
            strFndIdno="",strFndDp="",strFndRatingScore="",strFndAbout="";
    private void showJSONAResponse(String response){
        try {
            vw_agent_card.setVisibility(View.VISIBLE);
            lytavi.setVisibility(View.GONE);
            btnAcceptRequest.setVisibility(View.VISIBLE);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY2);
            //JSONObject serverData = result.getJSONObject(0);
            // looping through All Products
            //for (int i = 0; i < result.length(); i++) {
            // JSONObject c = searchResultsArray.getJSONObject(i);
            JSONObject serverData = result.getJSONObject(0);
            strATcktStatus= serverData.getString(KEY_ATCKTSTATUS);
            strTcktAgntPhoneNo= serverData.getString(KEY_ATCKTAGENTPHONENO);
            strTcktAgntEmail= serverData.getString(KEY_ATCKTAGENTEMAIL);
            strTcktAgntTakenAt= serverData.getString(KEY_ATCKTAGENTTAKENAT);
            strFndFullName= serverData.getString(KEY_ATCKTFULLNAME);
            strFndId= serverData.getString(KEY_ATCKTFNDID);
            strFndIdno= serverData.getString(KEY_ATCKTFNDIDNO);
            strFndDp= serverData.getString(KEY_ATCKTFNDDP);
            strFndRatingScore= serverData.getString(KEY_ATCKTRATINGSCORE);
            strFndAbout= serverData.getString(KEY_ATCKTFNDABOUT);
            tvStatusDet.setText(strATcktStatus);
            tvAgentName.setText(strFndFullName);
            tvAgentId.setText("Homlie ID:" + strFndId);
            tvRating.setText("Rating: "+strFndRatingScore);
            loadImage(strFndDp);
            getPaymentStatus();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // textViewResult.setText("Name:\t"+name+"\nAddress:\t" +address+ "\nVice Chancellor:\t"+ vc);
    }

    //Image Loader
private ImageLoader imageLoader;
private void  loadImage(String strFndDpUrl){
    try {
        String url = strFndDpUrl.trim();
        imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext())
                .getImageLoader();
        imageLoader.get(url, ImageLoader.getImageListener(nivProfilePhoto,
                R.color.btn_bg, R.color.btn_bg));
        nivProfilePhoto.setImageUrl(url, imageLoader);

        //    Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
    } catch (Exception e) {
        e.printStackTrace();
    }

}


private void callAgent(String strAgentPhoneNo){
    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", strAgentPhoneNo, null));
    startActivity(intent);
}

private void textAgent(String strAgentPhoneNo){
    Intent I =new Intent(Intent.ACTION_VIEW);

    I.setData(Uri.parse("smsto:"));
    I.setType("vnd.android-dir/mms-sms");
    I.putExtra("address", strAgentPhoneNo);
    I.putExtra("sms_body","");
    startActivity(I);
}

    class RefreshTask extends AsyncTask {

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            String text = String.valueOf(System.currentTimeMillis());
            //myTextView.setText(text);
            getAgentStatus();
        }

        @Override
        protected Object doInBackground(Object... params) {
            //while(someCondition) {
                try {
                    //sleep for 1s in background...
                    Thread.sleep(20000);
                    //and update textview in ui thread
                    publishProgress();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            //}
        }
    }

    private String UPLOAD_URL = "https://www.homlie.co.ke/malakane_init/mlkn_uppymnt_data.php";
    private String KEY_UPPYMNTTICKETNO="uppymnt_ticketno";
    private String KEY_UPPYMNTINVOICETOTAL="uppymnt_invoicetotal";
    private String KEY_UPPYMNTPHONENUMBER="uppymnt_phonenumber";
    private String KEY_UPPYMNTRATING= "uppymnt_rating";
    StringRequest stringRequest;
    private ProgressDialog loading;

    private void postPaymentData(){
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            loading = ProgressDialog.show(this,"Sending","Please wait...",false,false);
            stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            loading.dismiss();
                            //Toast.makeText(DetailsScreen.this, ""+s +" done", Toast.LENGTH_LONG).show();
                            //String phone_number = mPhone.getText().toString();
                            //String amount = mAmount.getText().toString();
                            if (s.trim().equals("Success")) {
                                performSTKPush(strUpldPymntPhoneNumber, strUpldPymntInvoiceTotal);
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            } else {
                                Toast.makeText(DetailsScreen.this, "Error!! please try again", Toast.LENGTH_LONG).show();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //Dismissing the progress dialog
                            loading.dismiss();
                            ////////////////////
                            if(volleyError instanceof TimeoutError ||volleyError instanceof NoConnectionError){
                                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.error_network_timeout),
                                        Toast.LENGTH_LONG).show();
                            }else if (volleyError instanceof AuthFailureError){
                                //
                                Toast.makeText(DetailsScreen.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();

                            }else if (volleyError instanceof ServerError){
                                //
                                Toast.makeText(DetailsScreen.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();

                            }else if (volleyError instanceof NetworkError){
                                //
                                Toast.makeText(DetailsScreen.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();

                            }else if (volleyError instanceof ParseError){
                                Toast.makeText(DetailsScreen.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();

                            }

                            //stringRequest .setRetryPolicy(new DefaultRetryPolicy(
                            //  30000,
                            //    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            //      DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            //Showing toast
                            //Toast.makeText(Upload_Fraudster.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
                    Map<String,String> params = new Hashtable<String, String>();
                    //Adding parameters
                    strUpldPymntTicketNo=tvPymntTicketNo.getText().toString();
                    strUpldPymntInvoiceTotal=tvInvoiceTotal.getText().toString();
                    params.put(KEY_UPPYMNTRATING, strUpldPymntRating);
                    params.put(KEY_UPPYMNTPHONENUMBER,  strUpldPymntPhoneNumber);
                    params.put(KEY_UPPYMNTINVOICETOTAL, strUpldPymntInvoiceTotal);
                    params.put(KEY_UPPYMNTTICKETNO, strUpldPymntTicketNo);
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
            stringRequest.setShouldCache(false);
            requestQueue.add(stringRequest);
        } else {
            //Snackbar.make(recyclerView, "No Internet connection, check settings and try again.", Snackbar.LENGTH_LONG)
            //      .setAction("Action", null).show();
            Snackbar snackbar = Snackbar
                    .make(llBottomSheetPayment, "No internet connection! Check settings and try again.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            postPaymentData();                 }
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

    ////begining of mpesa initiate payment stuff

    private void mpesaStuffOncreate(){
        ButterKnife.bind(this);
        mProgressDialog = new ProgressDialog(this);
        mApiClient = new DarajaApiClient();
        mApiClient.setIsDebug(true); //Set True to enable logging, false to disable.
        //mPay.setOnClickListener(this);
        getAccessToken();
    }
    public void getAccessToken() {
        mApiClient.setGetAccessToken(true);
        mApiClient.mpesaService().getAccessToken().enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull retrofit2.Response<AccessToken> response) {

                if (response.isSuccessful()) {
                    mApiClient.setAuthToken(response.body().accessToken);
                }
            }
            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {

            }
        });
    }

    public void performSTKPush(String phone_number,String amount) {
        mProgressDialog.setMessage("Processing your request");
        mProgressDialog.setTitle("Please Wait...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        String timestamp = Utils.getTimestamp();
        STKPush stkPush = new STKPush(
                BUSINESS_SHORT_CODE,
                Utils.getPassword(BUSINESS_SHORT_CODE, PASSKEY, timestamp),
                timestamp,
                TRANSACTION_TYPE,
                String.valueOf(amount),
                Utils.sanitizePhoneNumber(phone_number),
                PARTYB,
                Utils.sanitizePhoneNumber(phone_number),
                CALLBACKURL + strUpldPymntTicketNo,
                strUpldPymntTicketNo, //Account reference
                "clientapp" //Transaction description

        );

        mApiClient.setGetAccessToken(false);

        //Sending the data to the Mpesa API, remember to remove the logging when in production.
        mApiClient.mpesaService().sendPush(stkPush).enqueue(new Callback<STKPush>() {
            @Override
            public void onResponse(@NonNull Call<STKPush> call, @NonNull retrofit2.Response<STKPush> response) {
                mProgressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        Timber.d("post submitted to API. %s", response.body());
                    } else {
                        Timber.e("Response %s", response.errorBody().string());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<STKPush> call, @NonNull Throwable t) {
                mProgressDialog.dismiss();
                Timber.e(t);
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    ////////////////////polling for payment confirmation if made


    public static final String PYMNTSTATUS_URL = "https://www.homlie.co.ke/malakane_init/mlkn_paymentstatus.php?strpostid=";
    public static final String JSON_ARRAY_PYMNT = "result";
    public static final String KEY_PYMNTSTATUS = "strTcktStatus";


    private void getPaymentStatus(){
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            String strBulk=strTcktCode;
            String url = PYMNTSTATUS_URL+strBulk;
            StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //loading.dismiss();
                    //Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_SHORT).show();

                    if (response.trim().equals("Error"))
                        new RefreshTaskPayment().execute();
                    else
                        showJSONAPYMNTResponse(response);
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //loading.dismiss();
                            //Showing toast
                            if(volleyError instanceof TimeoutError ||volleyError instanceof NoConnectionError){
                                try {
                                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.error_network_timeout),
                                            Toast.LENGTH_LONG).show();
                                }catch (NullPointerException dsfsd){

                                }
                            }else if (volleyError instanceof AuthFailureError){
                                try {
                                    Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                                }catch (NullPointerException dsfsd){

                                }
                            }else if (volleyError instanceof ServerError){
                                try {
                                    Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                                }catch (NullPointerException dsfsd){

                                }
                            }else if (volleyError instanceof NetworkError){
                                try {
                                    Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                                }catch (NullPointerException dsfsd){

                                }
                            }else if (volleyError instanceof ParseError){
                                try {
                                    Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                                }catch (NullPointerException dsfsd){

                                }
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
                    .make(tvOrderSummary, "No internet connection! Check settings and try again.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            getPaymentStatus();
                        }
                    });
// Changing message text color
            snackbar.setActionTextColor(Color.RED);

// Changing action button text color
            View sbView = snackbar.getView();
//            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            //          textView.setTextColor(Color.YELLOW);
            snackbar.show();

        }
    }
    private void showJSONAPYMNTResponse(String response){
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        btnAcceptRequest.setVisibility(View.GONE);
            tvTicketCompleted.setVisibility(View.VISIBLE);

        //} catch (JSONException e) {
          //  e.printStackTrace();
        //}
        // textViewResult.setText("Name:\t"+name+"\nAddress:\t" +address+ "\nVice Chancellor:\t"+ vc);
    }
    class RefreshTaskPayment extends AsyncTask {

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            String text = String.valueOf(System.currentTimeMillis());
            //myTextView.setText(text);
            getPaymentStatus();
        }

        @Override
        protected Object doInBackground(Object... params) {
            //while(someCondition) {
            try {
                //sleep for 1s in background...
                Thread.sleep(20000);
                //and update textview in ui thread
                publishProgress();
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
            return null;
            //}
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
       // overridePendingTransition( 0,R.anim.slideinright);
    }
}
































//////////////////////*

//"BallPulseIndicator",
//        "BallGridPulseIndicator",
//        "BallClipRotateIndicator",
//        "BallClipRotatePulseIndicator",
//        "SquareSpinIndicator",
//        "BallClipRotateMultipleIndicator",
//        "BallPulseRiseIndicator",
//        "BallRotateIndicator",
//        "CubeTransitionIndicator",
//        "BallZigZagIndicator",
//        "BallZigZagDeflectIndicator",
//        "BallTrianglePathIndicator",
//        "BallScaleIndicator",
//        "LineScaleIndicator",
//        "LineScalePartyIndicator",
//        "BallScaleMultipleIndicator",
//        "BallPulseSyncIndicator",
//        "BallBeatIndicator",
//        "LineScalePulseOutIndicator",
//        "LineScalePulseOutRapidIndicator",
//        "BallScaleRippleIndicator",
//        "BallScaleRippleMultipleIndicator",
//        "BallSpinFadeLoaderIndicator",
//        "LineSpinFadeLoaderIndicator",
//        "TriangleSkewSpinIndicator",
//        "PacmanIndicator",
//        "BallGridBeatIndicator",
//        "SemiCircleSpinIndicator",
//        "com.wang.avi.sample.MyCustomIndicator"
//*//