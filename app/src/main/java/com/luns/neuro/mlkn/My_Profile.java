package com.luns.neuro.mlkn;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.luns.neuro.mlkn.library.ConnectionDetector;
import com.luns.neuro.mlkn.library.SharedPrefManager;
import com.luns.neuro.mlkn.library.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class My_Profile extends AppCompatActivity {
        private TextView tvUsername,tvEmail;
        private Button btnLogout;
        private String strRequestingUser;
        private String strFullName="",strIdNo="",strNextOfKinConctact="",strSpecialization="",strSecondProfessionalTitle="",strHighestEducationLevel="",strPrfImageUrl="";
        private String strNationality="",strCounty="",strPhysicalAddress="",strProfessionalTitle="",strThirdProfessionalTitle="";
        private String[] professionArr={},edcertlevelArr={},allLocationsArr={};
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.myprofile_activity);
            User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
            tvUsername=findViewById(R.id.tvUsername);
            tvEmail=findViewById(R.id.tvEmail);
            tvUsername.setText(user.getPhonenumber());
            tvEmail.setText(user.getUser_priviledge());
            strRequestingUser=user.getUser_priviledge()+"|"+user.getPhonenumber();
            getData();
            btnLogout=findViewById(R.id.btnLogout);
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    logOut();
                }
            });
        }
//        private ImageLoader imageLoader;
//        public void loadProfileImage(String strPrfImageUrl){
//            try {
//                nivSlctProfImage=(NetworkImageView) findViewById(R.id.slctProfImage);
//                imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext())
//                        .getImageLoader();
//                imageLoader.get(strPrfImageUrl, ImageLoader.getImageListener(nivSlctProfImage,
//                        R.color.colorPrimary, R.color.media));
//                nivSlctProfImage.setImageUrl(strPrfImageUrl, imageLoader);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        ConnectionDetector cd;
        Boolean isInternetPresent = false;
    public static final String DATA_URL = "https://www.homlie.co.ke/malakane_init/mlkn_get_profiledata.php?strgetprofiledata=";
        private void getData() {
            cd = new ConnectionDetector(getApplicationContext());
            isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {
                //progressBarMP.setVisibility(View.VISIBLE);
                String url = DATA_URL+ strRequestingUser.trim();
                StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                  //      progressBarMP.setVisibility(View.GONE);
                        showJSON(response);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                    //            progressBarMP.setVisibility(View.GONE);
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
                requestQueue.add(stringRequest);
            } else {
                //Snackbar.make(recyclerView, "No Internet connection, check settings and try again.", Snackbar.LENGTH_LONG)
                //      .setAction("Action", null).show();
                Snackbar snackbar = Snackbar
                        .make(tvUsername, "No internet connection! Check settings and try again.", Snackbar.LENGTH_INDEFINITE)
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
///                TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
 //               textView.setTextColor(Color.YELLOW);
                snackbar.show();

            }
        }

        private String KEY_PRFIMAGEURL="prfimageurl";
        private String KEY_UFNAME="strFullName";
        private String KEY_UIDNO= "strIdNo";
        private String KEY_UNEXTOFKINCONTACT="strNextOfKinConctact";
        private String KEY_USPECIALIZATION="strSpecialization";
        private String KEY_USECONDPROFESSIONALTITLE="strSecondProfessionalTitle";
        private String KEY_UHIGHESTEDUCATIONLEVEL="strHighestEducationLevel";
        private String KEY_UNATIONALITY="strNationality";
        private String KEY_UCOUNTY="strCounty";
        private String KEY_UPHYSICALADDRESS="strPhysicalAddress";
        private String KEY_UPROFESSIONALTITLE= "strProfessionalTitle";
        private String KEY_UTHIRDPROFESSIONALTITLE="strThirdProfessionalTitle";

        public static final String JSON_ARRAY1 = "userprofile";
        public static final String JSON_ARRAY2 = "fndtypes";
        public static final String JSON_ARRAY3 = "education_levels";
        public static final String JSON_ARRAY4 = "alllocations";
        private String strFndType="",strEDCertLevel="",strLctLocality="",strLctCounty="";
        ArrayList<String> fndTypesArray = new ArrayList<String>();
        ArrayList<String> edCertLevelArray = new ArrayList<String>();
        ArrayList<String> allLocationsArray = new ArrayList<String>();
        private void showJSON(String response){
            try {

                JSONObject jsonObject = new JSONObject(response);
                JSONArray fndtypes = jsonObject.getJSONArray(JSON_ARRAY2);
                for (int i = 0; i < fndtypes.length(); i++) {
                    JSONObject serverData = fndtypes.getJSONObject(i);
                    strFndType = serverData.getString("prftype");
                    fndTypesArray.add(strFndType);
                    //Log.e(TAG, "Serverdata: " + ""+serverData+"|||"+strFullName);
                }



                JSONArray education_levels = jsonObject.getJSONArray(JSON_ARRAY3);
                for (int i = 0; i < education_levels.length(); i++) {
                    JSONObject serverData = education_levels.getJSONObject(i);
                    strEDCertLevel = serverData.getString("ec_cert_level");
                    edCertLevelArray.add(strEDCertLevel);
                    //Log.e(TAG, "edCertLevelArray: " + ""+serverData+"|||"+strFullName);
                }

                JSONArray alllocations = jsonObject.getJSONArray(JSON_ARRAY4);
                for (int i = 0; i < alllocations.length(); i++) {
                    JSONObject serverData = alllocations.getJSONObject(i);
                    strLctLocality = serverData.getString("lct_locality");
                    strLctCounty = serverData.getString("lct_county");
                    String st=strLctLocality+"@@"+strLctCounty;
                    allLocationsArray.add(st);
                }


                JSONArray result = jsonObject.getJSONArray(JSON_ARRAY1);
                JSONObject serverData = result.getJSONObject(0);
                strPrfImageUrl = serverData.getString(KEY_PRFIMAGEURL);
                strFullName = serverData.getString(KEY_UFNAME);
                strIdNo = serverData.getString(KEY_UIDNO);
                strNextOfKinConctact = serverData.getString(KEY_UNEXTOFKINCONTACT);
                strSpecialization = serverData.getString(KEY_USPECIALIZATION);
                strSecondProfessionalTitle = serverData.getString(KEY_USECONDPROFESSIONALTITLE);
                strHighestEducationLevel = serverData.getString(KEY_UHIGHESTEDUCATIONLEVEL);
                strThirdProfessionalTitle = serverData.getString(KEY_UTHIRDPROFESSIONALTITLE);
                strProfessionalTitle = serverData.getString(KEY_UPROFESSIONALTITLE);
                strPhysicalAddress = serverData.getString(KEY_UPHYSICALADDRESS);
                strCounty = serverData.getString(KEY_UCOUNTY);
                strNationality = serverData.getString(KEY_UNATIONALITY);
                //loadProfileImage(strPrfImageUrl);
                //Log.e(TAG, "MTest: " + ""+serverData+"|||"+strFullName+"|"+strIdNo);
                professionArr = fndTypesArray.toArray(new String[fndTypesArray.size()]);

                //Log.e(TAG, "PPPPPPPTTTT: " + ""+ Arrays.toString(professionArr));

                allLocationsArr = allLocationsArray.toArray(new String[allLocationsArray.size()]);
                //Log.e(TAG, "allLocationsArray: " + Arrays.toString(allLocationsArr));
                edcertlevelArr = edCertLevelArray.toArray(new String[edCertLevelArray.size()]);
                //Log.e(TAG, "edcertlevelArr: " + Arrays.toString(edcertlevelArr));
                //tvSummaryProfession.setText(strProfessionalTitle+"\n"+strSecondProfessionalTitle+"\n"+strThirdProfessionalTitle);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // textViewResult.setText("Name:\t"+name+"\nAddress:\t" +address+ "\nVice Chancellor:\t"+ vc);
        }
        private void logOut() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Confirm sign out?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    // TODO Auto-generated method stub
                    finish();
                    SharedPrefManager.getInstance(getApplicationContext()).logout();
                    //starting login activity
                    // Intent xout=new Intent(getApplicationContext(),SignIn.class);
                    Intent i = new Intent(getApplicationContext(), SignIn.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    //startActivity(new Intent(getApplicationContext(), SignIn.class));
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
        @Override
        public void onBackPressed() {
            finish();
        }

    }
