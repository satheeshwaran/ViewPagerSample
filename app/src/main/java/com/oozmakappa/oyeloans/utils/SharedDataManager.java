package com.oozmakappa.oyeloans.utils;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.oozmakappa.oyeloans.Models.LoanUser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by satheeshwaran on 9/5/16.
 */
public class SharedDataManager {

    public static SharedDataManager sharedObject = new SharedDataManager();

    public static SharedDataManager getInstance() {  return sharedObject;}

    public LoanUser userObject = new LoanUser();

    public boolean isFBLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
}
