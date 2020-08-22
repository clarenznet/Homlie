package com.luns.neuro.mlkn.library;

/**
 * Created by neuro on 2/24/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.luns.neuro.mlkn.SignIn;


/**
 * Created by Belal on 9/5/2017.
 */

//here for this class we are using a singleton pattern

public class SharedPrefManager {

    //the constants
    private static final String SHARED_PREF_NAME = "malakane";
    private static final String KEY_FIREBASEID = "user_firebase_id";
    private static final String KEY_EMAIL = "user_email";
    private static final String KEY_USERPRIVILEDGE = "user_priviledge";
    private static final String KEY_USERFULLNAME = "user_fullname";
    private static final String KEY_USERADDRESS = "user_address";
    private static final String KEY_USERLATITUDE = "user_latitude";
    private static final String KEY_USERLONGITUDE = "user_longitude";
    private static final String KEY_USERLOCATION = "user_location";
    private static final String KEY_USERSERVICEREGION = "user_service_region";
    private static final String KEY_USERPHONENO = "user_phoneno";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERFULLNAME, user.getUser_fullname());
        editor.putString(KEY_EMAIL, user.getPhonenumber());
        editor.putString(KEY_FIREBASEID, user.getFirebaseid());
        editor.putString(KEY_USERPRIVILEDGE, user.getUser_priviledge());
        editor.putString(KEY_USERADDRESS, user.getUser_address());
        editor.putString(KEY_USERLATITUDE, user.getUser_latitude());
        editor.putString(KEY_USERLONGITUDE, user.getUser_longitude());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL, null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(KEY_EMAIL,null ),
                sharedPreferences.getString(KEY_FIREBASEID, null),
                sharedPreferences.getString(KEY_USERPRIVILEDGE, null),
                sharedPreferences.getString(KEY_USERFULLNAME, null),
                sharedPreferences.getString(KEY_USERADDRESS, null),
                sharedPreferences.getString(KEY_USERLATITUDE, null),
                sharedPreferences.getString(KEY_USERLONGITUDE, null)
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, SignIn.class));
    }

    ///////////////////////////////////////////////Data sharing for my address ACTIVITY
    public void myJsonData(MainDataPreference mainDataPreference) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERLOCATION, mainDataPreference.getStrResponse());
        editor.putString(KEY_USERSERVICEREGION, mainDataPreference.getStrMyServiceRegion());
        editor.putString(KEY_USERPHONENO, mainDataPreference.getStrMyPhoneNo());
        editor.apply();
    }
    //this method will give the logged in user
    public MainDataPreference getMainDataJson(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new MainDataPreference(
                sharedPreferences.getString(KEY_USERLOCATION,"0" ),
                sharedPreferences.getString(KEY_USERSERVICEREGION,"0" ),
                sharedPreferences.getString(KEY_USERPHONENO,"0" )

        );
    }

}