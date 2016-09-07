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
public class FacebookHelperUtils {

    public static FacebookHelperUtils sharedObject = new FacebookHelperUtils();

    public static FacebookHelperUtils getInstance() { sharedObject.userObject = new LoanUser(); return sharedObject;}

    public LoanUser userObject;

    private static final String TAG_RESPONSE = "RESPONSE";

    public boolean isFBLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public static void getFBProfilePicture(){

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+FacebookHelperUtils.getInstance().userObject.fbUserID+"/picture?width=1000&height=1000&redirect=false",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d(TAG_RESPONSE,response.getRawResponse());
                        try {
                            FacebookHelperUtils.getInstance().userObject.fbProfilePicURL = response.getJSONObject().getJSONObject("data").getString("url");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    public static void getOtherFBDetails(){

        final GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        Log.d(TAG_RESPONSE,object.toString());
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", OyeConstants.fbMeRequestFields);
        request.setParameters(parameters);
        request.executeAsync();
    }
}
