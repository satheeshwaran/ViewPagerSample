package com.oozmakappa.oyeloans.Services;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.oozmakappa.oyeloans.Models.SuccessModel;
import com.oozmakappa.oyeloans.helper.WebServiceCallHelper;
import com.oozmakappa.oyeloans.utils.OyeConstants;
import com.oozmakappa.oyeloans.utils.SharedDataManager;

/**
 * Created by satheeshwaran on 9/18/16.
 */
public class OyeLoansFCMNotificationReceiver extends FirebaseInstanceIdService{

    private static final String TAG = "FirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        // Save to SharedPreferences
        editor.putString(OyeConstants.FCM_REGISTRATION_TOKEN, refreshedToken);
        editor.apply();


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.

        if (SharedDataManager.getInstance().userObject.emailID != null && SharedDataManager.getInstance().userObject.emailID.length() > 0) {
            WebServiceCallHelper webServiceHelper = new WebServiceCallHelper(new WebServiceCallHelper.OnWebServiceRequestCompletedListener() {
                @Override
                public void onRequestCompleted(SuccessModel model, String errorMessage) {
                    if (model.getStatus().equals("success")) {
                    }
                }
            });
            webServiceHelper.registerFCMToken(token, SharedDataManager.getInstance().userObject.emailID);
        }
    }
}
