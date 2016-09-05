package com.oozmakappa.oyeloans.utils;

/**
 * Created by satheeshwaran on 9/5/16.
 */
public class FacebookHelperUtils {

    public static FacebookHelperUtils sharedObject = new FacebookHelperUtils();

    public static FacebookHelperUtils getInstance() {return sharedObject;}

    public boolean isFBLoggedIN = false;

    public String fbUserName = "";

    void checkFacebookLoginStatus(){

    }

}
