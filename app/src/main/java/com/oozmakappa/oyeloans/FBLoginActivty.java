package com.oozmakappa.oyeloans;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.messaging.FirebaseMessaging;
import com.oozmakappa.oyeloans.DataExtraction.AppController;
import com.oozmakappa.oyeloans.HowitWorks.HowItWorksActivity;
import com.oozmakappa.oyeloans.Models.LoanUser;
import com.oozmakappa.oyeloans.Models.SuccessModel;
import com.oozmakappa.oyeloans.Offers.OffersBrain;
import com.oozmakappa.oyeloans.helper.WebServiceCallHelper;
import com.oozmakappa.oyeloans.utils.FacebookHelperUtils;
import com.oozmakappa.oyeloans.utils.FacebookHelperUtilsCallback;
import com.oozmakappa.oyeloans.utils.OyeConstants;
import com.oozmakappa.oyeloans.utils.SharedDataManager;
import com.oozmakappa.oyeloans.utils.Utils;

import org.json.JSONObject;

public class FBLoginActivty extends AppCompatActivity {

    private static final String TAG_CANCEL = "CANCELLED";
    private static final String TAG_ERROR = "ERROR";
    private static final String TAG_RESPONSE = "REPSONSE";
    private static final int PERMISSION_REQUEST_CODE = 1;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fblogin_activty);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Utils.showLoading(FBLoginActivty.this,"Building spaceships...");
                onFacebookLogin();
            }
            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        Button button = (Button) findViewById(R.id.HowItWorks);
        button.setPaintFlags(button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FBLoginActivty.this,HowItWorksActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        Tracker t = ((AppController) this.getApplication()).getDefaultTracker();
        t.setScreenName("Login screen");
        t.send(new HitBuilders.ScreenViewBuilder().build());
        t.enableAutoActivityTracking(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(!checkPermission())
                requestPermission();
            else
                OffersBrain.getInstance(this);
        }else
            OffersBrain.getInstance(this);

    }

    void onFacebookLogin(){
        LoginManager.getInstance().logInWithReadPermissions(this, OyeConstants.permissionNeeds);

        // Set permissions
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject json, GraphResponse response) {
                                        if (response.getError() != null) {
                                            // handle error
                                            System.out.println("ERROR");
                                        } else {
                                            System.out.println("Success");

                                            FacebookHelperUtils.getRequiredFBDetails(new FacebookHelperUtilsCallback() {
                                                @Override
                                                public void callCompleted(JSONObject responseObject) {
                                                    SharedDataManager.getInstance().userObject = LoanUser.loanUserFromJSONObject(responseObject);
                                                    //goToAccountSummaryPage();
                                                    WebServiceCallHelper webServiceHelper = new WebServiceCallHelper(new WebServiceCallHelper.OnWebServiceRequestCompletedListener(){
                                                        @Override
                                                        public void onRequestCompleted(SuccessModel model, String errorMessage){
                                                            if (model.getStatus().equals("success")) {
                                                                FirebaseMessaging.getInstance().subscribeToTopic("loan_info");
                                                                Utils.removeLoading();
                                                                goToProfileEditPage(!FacebookHelperUtils.checkIfValidFBProfile(SharedDataManager.getInstance().userObject));
                                                            }
                                                        }
                                                    });
                                                    webServiceHelper.makeFacebookServiceCall(SharedDataManager.getInstance().userObject);
                                                    //To be reomoved after setting up single box.
                                                    Utils.removeLoading();
                                                    goToProfileEditPage(!FacebookHelperUtils.checkIfValidFBProfile(SharedDataManager.getInstance().userObject));
                                                }
                                            });
                                        }
                                    }

                                }).executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG_CANCEL,"On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG_ERROR,error.toString());
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        Tracker t = ((AppController) this.getApplication()).getDefaultTracker();
        t.setScreenName("FBlogin");
        t.send(new HitBuilders.ScreenViewBuilder().build());
        t.enableAutoActivityTracking(true);
    }


    void goToProfileEditPage(Boolean status){
        registerForNotifications();
        Intent editProfileIntent = new Intent(this,MyProfilePage.class);
        editProfileIntent.putExtra("AllEdit",true);
        editProfileIntent.putExtra("ShowInsufficientInformation",status);
        startActivity(editProfileIntent);
        finish();
    }

    void registerForNotifications() {
        try {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

            String token = preferences.getString(OyeConstants.FCM_REGISTRATION_TOKEN, null);

            if (token != null && token.length() >0) {
                WebServiceCallHelper webServiceHelper = new WebServiceCallHelper(new WebServiceCallHelper.OnWebServiceRequestCompletedListener() {
                    @Override
                    public void onRequestCompleted(SuccessModel model, String errorMessage) {
                        if (model.getStatus().equals("success")) {
                            Log.i(TAG_RESPONSE,"push notification service completed.");
                        }
                    }
                });
                webServiceHelper.registerFCMToken(token, SharedDataManager.getInstance().userObject.emailID);
            }else {
                FirebaseMessaging.getInstance().subscribeToTopic("loan_info");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION)){


        } else {

            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    OffersBrain.getInstance(this);

                } else {

                }
                break;
        }
    }
}
