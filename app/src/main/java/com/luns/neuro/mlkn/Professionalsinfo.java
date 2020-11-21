package com.luns.neuro.mlkn;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Professionalsinfo extends AppCompatActivity {
    private String strServerResponseResultData="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.professionalsinfo);
        try {
            Bundle extras = getIntent().getExtras();
            strServerResponseResultData = extras.getString("strServerResponseResultData");

        }catch (NullPointerException f){
            ///////////////////
        }
        Toast.makeText(this, ""+strServerResponseResultData, Toast.LENGTH_SHORT).show();
    }
}
