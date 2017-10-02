package com.example.tanay.babble;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class ApplicationSettings {

    private static ApplicationSettings applicationSettings = null;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String TAG = "sharedPref";
    private static final String KEY_IS_AUTHENTICATED = "KEY_IS_AUTHENTICATED";
    private static final String CURRENT_USER = "CURRENT_USER";

    private ApplicationSettings(Context context){

        sharedPreferences =
                context.getSharedPreferences("com.example.tanay.babble.SHARED_PREFERENCES",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    public static ApplicationSettings getInstance(Context context) {

        if(applicationSettings == null) {
            applicationSettings = new ApplicationSettings(context);
        }

        return applicationSettings;
    }

    public boolean isUserAuthenticated(){

        final boolean result = sharedPreferences.getBoolean(KEY_IS_AUTHENTICATED, false);
        Log.d(TAG, "isUserAuthenticated: "+ result);
        return result;
    }

    public void setUserAuthentication(boolean isAuthenticated){

        editor.putBoolean(KEY_IS_AUTHENTICATED, isAuthenticated);
        editor.commit();

    }

    public void setCurrentUser(String currentUser) {

        editor.putString(CURRENT_USER, currentUser);
        editor.commit();

    }

    public String getCurrentUser(){

        final String user = sharedPreferences.getString(CURRENT_USER, null);
        Log.d(TAG, "isUserAuthenticated: "+ user);
        return user;

    }

}