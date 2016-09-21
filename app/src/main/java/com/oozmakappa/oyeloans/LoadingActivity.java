package com.oozmakappa.oyeloans;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.messaging.FirebaseMessaging;
import com.oozmakappa.oyeloans.DataExtraction.AppController;
import com.oozmakappa.oyeloans.Models.LoanUser;
import com.oozmakappa.oyeloans.Models.SuccessModel;
import com.oozmakappa.oyeloans.helper.WebServiceCallHelper;
import com.oozmakappa.oyeloans.utils.FacebookHelperUtils;
import com.oozmakappa.oyeloans.utils.SharedDataManager;
import com.oozmakappa.oyeloans.utils.FacebookHelperUtilsCallback;
import com.oozmakappa.oyeloans.utils.OyeConstants;
import com.oozmakappa.oyeloans.utils.Utils;

import org.json.JSONObject;

public class LoadingActivity extends AppCompatActivity {

    CallbackManager callbackManager;

    private static final String TAG_CANCEL = "CANCELLED";
    private static final String TAG_ERROR = "ERROR";
    private static final String TAG_RESPONSE = "REPONSE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();


        Utils.showLoading(this, "Loading...");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedDataManager.getInstance().isFBLoggedIn()) {
                    onFacebookLogin();
                } else {
                    //go to account login page...
                    Utils.removeLoading();
                    goToLoginPage();
                }
            }
        }, 2000);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Tracker t = ((AppController) this.getApplication()).getDefaultTracker();
        t.setScreenName("Loading screen");
        t.send(new HitBuilders.ScreenViewBuilder().build());
        t.enableAutoActivityTracking(true);
    }

    void onFacebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, OyeConstants.permissionNeeds);

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("Success");

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

                                                    registerForNotifications();
                                                    //SharedDataManager.getInstance().userObject.fbUserName = responseObject.getString("name");
                                                    WebServiceCallHelper webServiceHelper = new WebServiceCallHelper(new WebServiceCallHelper.OnWebServiceRequestCompletedListener() {
                                                        @Override
                                                        public void onRequestCompleted(SuccessModel model, String errorMessage) {
                                                            if (model.getStatus().equals("success")) {
                                                                Utils.removeLoading();
                                                                goToAccountSummaryPage();
                                                            }
                                                        }
                                                    });
                                                    webServiceHelper.makeFacebookServiceCall(SharedDataManager.getInstance().userObject);
                                                    //To be reomoved after setting up single box.
                                                    goToAccountSummaryPage();
                                                }
                                            });
                                        }
                                    }

                                }).executeAsync();
                    }

                    @Override
                    public void onCancel() {

                        Utils.removeLoading();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoadingActivity.this);
                        alertDialogBuilder.setMessage("You need to accept all the permissions before proceeding to submit a loan, would you like to retry?");

                        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Utils.showLoading(LoadingActivity.this, "Loading...");
                                onFacebookLogin();
                            }
                        });

                        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        // Toast.makeText(getApplicationContext(),"Action Cancelled", Toast.LENGTH_SHORT).show();
                        Log.d(TAG_CANCEL, "On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {

                        Utils.removeLoading();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoadingActivity.this);
                        alertDialogBuilder.setMessage("Something wrong has happened");
                        alertDialogBuilder.setTitle("Oops!!");

                        alertDialogBuilder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Utils.showLoading(LoadingActivity.this, "Loading...");
                                onFacebookLogin();
                            }
                        });

                        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        //Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG_ERROR, error.toString());
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    void goToProfilePage() {
        Intent profilePage = new Intent(this, MyProfilePage.class);
        startActivity(profilePage);
    }


    void goToAccountSummaryPage(){
            Intent accSummaryIntent = new Intent(this,DashboardActivity.class);
            startActivity(accSummaryIntent);
        }

    void goToLoginPage() {
        Intent loginIntent = new Intent(this, FBLoginActivty.class);
        startActivity(loginIntent);
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
}
