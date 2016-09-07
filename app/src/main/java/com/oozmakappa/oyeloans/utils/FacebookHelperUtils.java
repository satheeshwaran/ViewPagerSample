package com.oozmakappa.oyeloans.utils;

import com.facebook.AccessToken;

/**
 * Created by satheeshwaran on 9/5/16.
 */
public class FacebookHelperUtils {

    public static FacebookHelperUtils sharedObject = new FacebookHelperUtils();

    public static FacebookHelperUtils getInstance() {return sharedObject;}

    public String fbUserName = "";

    public boolean isFBLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }


}
