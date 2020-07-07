package com.luns.neuro.mlkn.library;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.luns.neuro.mlkn.SignIn;

import java.util.HashMap;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;
	
	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;
	
	// Shared pref mode
	int PRIVATE_MODE = 0;
	
	// Sharedpref file name
	private static final String PREF_NAME = "Pacific";
	
	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";
	
	
	// Email address (make variable public to access from outside)
	public static final String KEY_EMAIL = "email";
	// Email address (make variable public to access from outside)
		public static final String KEY_USER = "user";
		public static final String KEY_PROFICON = "icon";
	    public static final String KEY_SERVICEREGION = "serviceregion";
	public static final String KEY_DESTINATIONDATA = "destinationdata";
	public static final String KEY_CATEGORY = "category";
///////////////////

	//testMainActivity m;
	// Constructor
	public SessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	/**
	 * Create login session
	 * */
	public void createLoginSession(String strName, String strEmail){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);
		
		// Storing email in pref
		editor.putString(KEY_EMAIL, strEmail);
		// Storing email in pref
	    editor.putString(KEY_USER, strName);
		//editor.putString(KEY_PROFICON, strProfileIcon);
		//editor.putString(KEY_SERVICEREGION, strServiceRegion);
		// commit changes
		editor.commit();
	}	
	/**
	 * Create login session
	 * */
	public void createSessionData( String strDestinationData,String strCategory){
		// Storing login value as TRUE
		//editor.putBoolean(IS_LOGIN, true);
		
		// Storing email in pref
		editor.putString(KEY_DESTINATIONDATA,strDestinationData);
		// Storing email in pref
	    editor.putString(KEY_CATEGORY,strCategory);
				
		// commit changes
		editor.commit();
	}	
	
	/**
	 * Check login method wil check user login status
	 * If false it will redirect user to login page
	 * Else won't do anything
	 * */
	public void checkLogin(){
		// Check login status
		if(!this.isLoggedIn()){
			// user is not logged in redirect him to Login Activity

			Intent i = new Intent(_context, SignIn.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			_context.startActivity(i);
		}
		
	}
	public void checkStartSignin(){
		// Check login status
		if(!this.isLoggedIn()){
			// user is not logged in redirect him to Login Activity
			Intent i = new Intent(_context, SignIn.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			_context.startActivity(i);
		}

	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		// user email id
		user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
		user.put(KEY_USER, pref.getString(KEY_USER, null));
		//user.put(KEY_PROFICON, pref.getString(KEY_PROFICON, null));
		//user.put(KEY_SERVICEREGION, pref.getString(KEY_SERVICEREGION, null));
		// return user
		return user;
	}
	
	/**
	 * Clear session details
	 * */
	public void logoutUser(){
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();
		
		// After logout redirect user to Loing Activity
		Intent i = new Intent(_context, SignIn.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		_context.startActivity(i);

	}

	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn(){
		return pref.getBoolean(IS_LOGIN, true);
	}


	}
