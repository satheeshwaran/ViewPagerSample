package com.oozmakappa.oyeloans.utils;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by satheeshwaran on 9/11/16.
 */
public class FacebookHelperUtils {

    private static final String TAG_RESPONSE = "RESPONSE";

    public static void getFBProfilePictureForUserID(String userID,final FacebookHelperUtilsCallback callback){

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+userID+"/picture?width=1000&height=1000&redirect=false",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d(TAG_RESPONSE,response.getRawResponse());
                        try {
                            SharedDataManager.getInstance().userObject.fbProfilePicURL = response.getJSONObject().getJSONObject("data").getString("url");
                            callback.callCompleted(response.getJSONObject());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    public static void getRequiredFBDetails(final FacebookHelperUtilsCallback callback){

        final GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        Log.d(TAG_RESPONSE,object.toString());
                        callback.callCompleted(response.getJSONObject());
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", OyeConstants.fbMeRequestFields);
        request.setParameters(parameters);
        request.executeAsync();
    }
    public static void getMeRequestDetails(AccessToken accessToken,final SharedDataManager callback){

        GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject json, GraphResponse response) {
                        if (response.getError() != null) {
                            // handle error
                            System.out.println("ERROR");
                        } else {
                            System.out.println("Success");
                            try {

                                String jsonresult = String.valueOf(json);
                                System.out.println("JSON Result"+jsonresult);
                                String str_fb_acc_name = json.getString("name");
                                SharedDataManager.getInstance().userObject.fbUserName = str_fb_acc_name;
                                String str_id = json.getString("id");
                                SharedDataManager.getInstance().userObject.fbUserID = str_id;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }).executeAsync();
    }
}
