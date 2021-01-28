package com.luns.neuro.mlkn;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.luns.neuro.mlkn.library.PrefManager;

/**
 * Created by Clarence on 8/5/2016.
 */
public class TermsAndConditions extends AppCompatActivity {
    private Button btnWelcomeScreen,btnUpdate,btnGoToSite;
    String strMessageTitle;
    PrefManager prefm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.termsandconditions_activity);
        strMessageTitle="";

        try{
            Bundle extras = getIntent().getExtras();
            strMessageTitle = extras.getString("messageTitle");

        }catch (NullPointerException e) {
            // TODO: handle exception
        }
        if(strMessageTitle.equals("New Update")){
        btnUpdate.setVisibility(View.VISIBLE);
        }
        btnGoToSite = findViewById(R.id.btngotoPrivacyPolicy);
        btnGoToSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.homlie.co.ke/termsandconditions"));
                startActivity(i);            }
        });

        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           goToUrl("https://play.google.com/store/apps/details?id=com.pacific.smartbuyer");
            }
        });
////        btnWelcomeScreen= (Button) findViewById(R.id.btnWelcomeScreen);
//        btnWelcomeScreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               //prefm.openWelcome();
//                Intent in=new Intent(TermsAndConditions.this, WelcomeActivity.class);
//                in.putExtra("strActivity","Support");
//                startActivity(in);
//            }
//        });


    }
    public void goToUrl(String url){
        Uri uriUrl= Uri.parse(url);
        Intent laubrow=new Intent(Intent.ACTION_VIEW,uriUrl);
        startActivity(laubrow);
    }

    @Override
    public void onBackPressed() {
        finish();
        return;
    }

}
