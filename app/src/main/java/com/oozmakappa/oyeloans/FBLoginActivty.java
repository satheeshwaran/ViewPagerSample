package com.oozmakappa.oyeloans;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.oozmakappa.oyeloans.Models.LoanUser;
import com.oozmakappa.oyeloans.utils.FacebookHelperUtils;
import com.oozmakappa.oyeloans.utils.FacebookHelperUtilsCallback;
import com.oozmakappa.oyeloans.utils.OyeConstants;
import com.oozmakappa.oyeloans.utils.SharedDataManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class FBLoginActivty extends AppCompatActivity {

    private static final String TAG_CANCEL = "CANCELLED";
    private static final String TAG_ERROR = "ERROR";

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fblogin_activty);
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
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
                                                    goToProfileEditPage();
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


    void goToProfileEditPage(){
        Intent accSummaryIntent = new Intent(this,EditMyProfilePage.class);
        startActivity(accSummaryIntent);
    }
}
