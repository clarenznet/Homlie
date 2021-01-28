package com.luns.neuro.mlkn.library;

/**
 * Created by neuro on 7/16/2017.
 */

public class Config {
    //URL to our login.php file
    //public static final String LOGIN_URL = "http://10.0.2.2/sharere/user_login.php";
    public static final String LOGIN_URL = "https://www.admin.homlie.co.ke/sharere/user_login.php";

    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_PHONENUMBER = "ur_phonenumber";
    public static final String KEY_VERIFICATIONID = "ur_verificationid";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";

    //Keys for Sharedpreferences
    //This would be the name of our shared

    public static final String SHARED_PREF_NAME = "smartbuyerbob";

    //This would be used to store the email of current logged in user
    public static final String USERPHONENUMBER_SHARED_PREF = "username";
    //This would be used to store the email of current logged in user
    public static final String USERID_SHARED_PREF = "userid";
    public static final String SHARERE_SHARED_WALLPAPER = "sharere_shared_wallpaper";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";

    // New instance of Config with firebase messaging constants
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_smartbuyer";

}