package com.luns.neuro.mlkn;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.luns.neuro.mlkn.library.ConnectionDetector;
import com.luns.neuro.mlkn.library.SessionManager;
import com.luns.neuro.mlkn.library.SharedPrefManager;
import com.luns.neuro.mlkn.library.User;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Clarence on 9/9/2016.
 */
public class Feedback extends AppCompatActivity implements View.OnClickListener{
    private EditText edtTitle,edtDetails;
    private Button btnSend;
    private String UPLOAD_URL = "https://www.homlie.co.ke/malakane_init/feedback_upload.php";
    //private String UPLOAD_URL ="http://10.0.2.2/smartbuyer/bid_upload.php";
    private String KEY_TITLE = "strtitle";
    private String KEY_USER_EMAILADDRESS = "struseremail";
    private String KEY_USER_PHONENUMBER = "struserphone";
    private String KEY_DETAILS = "strdetails";

    SessionManager session;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    View llyfeedback;
    private String strEmail,strFTitle,strFDetails;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        btnSend = findViewById(R.id.btnFSend);
        btnSend.setOnClickListener(this);
        edtTitle = findViewById(R.id.edtFTitle);
        edtDetails = findViewById(R.id.edtFDetails);
        llyfeedback=findViewById(R.id.llyfeedback);
    }
    private void uploadFeedBack(){

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            //Showing the progress dialog
            final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String strResp) {
                            //Disimissing the progress dialog
                            loading.dismiss();
                            Toast.makeText(getApplicationContext(), strResp, Toast.LENGTH_LONG).show();
                            if (strResp.trim().equalsIgnoreCase("Success")) {
                                Snackbar.make(llyfeedback, "Success, thanks for your feedback.", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                closeFeedback();

                            }else{
                                Snackbar.make(llyfeedback, "Error, feedback not submitted successfully.", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //Dismissing the progress dialog
                            loading.dismiss();

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
                    }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
                    String strUserPhonenumber = user.getPhonenumber();
                    String strUserEmailAddress = user.getUser_priviledge();


                    //String strPriceRange= edtPriceRange.getText().toString().trim();
                    //Creating parameters
                    Map<String,String> params = new Hashtable<String, String>();
                    //Adding parameters

                    params.put(KEY_USER_PHONENUMBER, strUserPhonenumber);
                    params.put(KEY_USER_EMAILADDRESS, strUserEmailAddress);
                    params.put(KEY_TITLE, strFTitle);
                    params.put(KEY_DETAILS, strFDetails);
                    return params;
                }
            };

            //Creating a Request Queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            stringRequest.setShouldCache(false);
            //Adding request to the queue
            requestQueue.add(stringRequest);
        } else {
            //Snackbar.make(recyclerView, "No Internet connection, check settings and try again.", Snackbar.LENGTH_LONG)
            //      .setAction("Action", null).show();
            Snackbar snackbar = Snackbar
                    .make(llyfeedback, "No internet connection! Check settings and try again.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            uploadFeedBack();            }
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
    public void closeFeedback(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Feedback successfully logged for review.");
        builder.setCancelable(false);
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                // TODO Auto-generated method stub
                // loading.onBackPressed();
                finish();
                // session.logoutUser();
            }
        });
        AlertDialog alert = builder.create();
        //alert.getWindow().setBackgroundDrawableResource(R.color.btn_bg);
        alert.show();

    }

    @Override
    public void onClick(View v) {
        if (v == btnSend) {
            strFTitle = edtTitle.getText().toString().trim();
            strFDetails = edtDetails.getText().toString().trim();

            if (strFTitle != "" && strFDetails != "" && !strFTitle.isEmpty() && !strFDetails.isEmpty()) {
                uploadFeedBack();

            } else {
                Snackbar.make(llyfeedback, "Please fill in all fields.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }

        }
    }
}