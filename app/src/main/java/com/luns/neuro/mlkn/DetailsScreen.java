package com.luns.neuro.mlkn;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.luns.neuro.mlkn.library.ConnectionDetector;
import com.luns.neuro.mlkn.library.CustomVolleyRequest;
import com.luns.neuro.mlkn.library.SessionManager;
import com.luns.neuro.mlkn.library.SharedPrefManager;
import com.luns.neuro.mlkn.library.User;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class DetailsScreen extends AppCompatActivity implements OnMapReadyCallback {
    private Button btnAcceptRequest;
    private String strTicketId="",strParentClass="",strOrderSummary="", strOrderTyp="",strOrderTim="";
    private TextView tvOrderSummary,tvOrderTyp, tvTtlCost,tvSpecificLocation,tvGenLocation,tvDateTime,tvCreatedAt,tvToNote;
    private GoogleMap mMap;
    private float fLat=0,fLong=0;
    private AVLoadingIndicatorView avi;
    private ImageView btnCall,btnMessage;
    private TextView tvStatusDet,tvAgentName, tvAgentId,tvRating;
    private NetworkImageView  nivProfilePhoto;
    private View vw_agent_card,lytavi;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailsscreen);
//        String indicator=getIntent().getStringExtra("indicator");
//        avi= (AVLoadingIndicatorView) findViewById(R.id.avi);
//        avi.setIndicator(indicator);
//        avi.smoothToShow();
        try {
            Bundle extras = getIntent().getExtras();
            //strParentClass = extras.getString("parentclass");
            //strOrderSummary= extras.getString("strOrderSummary");
            //strOrderTyp = extras.getString("strOrderTyp");
            strTicketId = extras.getString("strTicketId");
        }catch (NullPointerException f){
            ///////////////////
        }
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("ticket details");
        btnAcceptRequest=findViewById(R.id.btnAcceptRequest);
        btnAcceptRequest.setText("Complete Ticket");
        //Toast.makeText(getApplicationContext(),strTicketId,Toast.LENGTH_LONG).show();
        btnAcceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // confirmPaymentMethod();
                Intent in = new Intent(getApplicationContext(), CreditCardPay.class);
                startActivity(in);
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
        mMap.addMarker(new MarkerOptions().position(sydney).title("Ticket Location"));
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

    public static final String DATA_URL = "https://www.instrov.com/malakane_init/mlkn_ticketdetails.php?strpostid=";
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
    public static final String AGENTSTATUS_URL= "https://www.instrov.com/malakane_init/mlkn_agentstatus.php?strpostid=";
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

                    if (response.equals("Error"))
//                        timeToCallBack();

                    //starting our task which update textview every 1000 ms
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
                                    Toast.makeText(getApplicationContext(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                            }catch (NullPointerException dsfsd){

                            }
                            }else if (volleyError instanceof ServerError){
                                try {
                                    Toast.makeText(getApplicationContext(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }catch (NullPointerException dsfsd){

                        }
                            }else if (volleyError instanceof NetworkError){
                                try {
                                    Toast.makeText(getApplicationContext(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
            }catch (NullPointerException dsfsd){

            }
                            }else if (volleyError instanceof ParseError){
                                try {
                                    Toast.makeText(getApplicationContext(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
        }catch (NullPointerException dsfsd){

        }
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
            tvAgentId.setText("Homlie ID: HA20Y00"+strFndId);
            tvRating.setText("Rating: "+strFndRatingScore);
            loadImage(strFndDp);
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
    I.putExtra("address", new String (strAgentPhoneNo));
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
                    Thread.sleep(10000);
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