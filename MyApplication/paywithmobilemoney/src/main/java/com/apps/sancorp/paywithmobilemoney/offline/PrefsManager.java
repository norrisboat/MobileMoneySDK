package com.apps.sancorp.paywithmobilemoney.offline;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.apps.sancorp.paywithmobilemoney.utils.Constants;

/**
 * Created by norrisboateng on 1/3/17.
 */

public class PrefsManager {
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String THIRD_PARTY_ID = "third_party_id";

    @SuppressLint("CommitPrefEdits")
    public PrefsManager(Context context) {
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(Constants.LOG_TAG, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setUser(String username,String password){
        editor.putString(KEY_USERNAME,username);
        editor.putString(KEY_PASSWORD,password);
        editor.apply();
    }

    public void setThirdPartyId(String thirdPartyId){
        editor.putString(THIRD_PARTY_ID,thirdPartyId);
        editor.apply();
    }

    public String getUsername(){
        return pref.getString(KEY_USERNAME,"");
    }

    public String getPassword(){
        return pref.getString(KEY_PASSWORD,"");
    }

    public String getThirdPartyId(){
        return pref.getString(THIRD_PARTY_ID,"3444777");
    }
}
