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
import com.android.volley.*;
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
public class Help extends AppCompatActivity implements View.OnClickListener{
    private EditText edtTitle,edtDetails;
    private Button btnSend;
    private String UPLOAD_URL ="https://www.instrov.com/profile/feedback_upload.php";
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
        setContentView(R.layout.help_activity);

    }

    @Override
    public void onClick(View v) {

    }
}