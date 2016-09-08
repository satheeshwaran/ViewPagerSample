    package com.oozmakappa.oyeloans;

    import android.content.Intent;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.util.Log;

    import com.facebook.AccessToken;
    import com.facebook.CallbackManager;
    import com.facebook.FacebookCallback;
    import com.facebook.FacebookException;
    import com.facebook.FacebookSdk;
    import com.facebook.GraphRequest;
    import com.facebook.GraphResponse;
    import com.facebook.HttpMethod;
    import com.facebook.appevents.AppEventsLogger;
    import com.facebook.login.LoginManager;
    import com.facebook.login.LoginResult;
    import com.facebook.login.widget.LoginButton;
    import com.oozmakappa.oyeloans.utils.FacebookHelperUtils;
    import com.oozmakappa.oyeloans.utils.OyeConstants;

    import org.json.JSONException;
    import org.json.JSONObject;

    import java.util.Arrays;
    import java.util.List;

    public class LoadingActivity extends AppCompatActivity {

        CallbackManager callbackManager;
        List<String> permissionNeeds= Arrays.asList("public_profile", "email", "user_birthday", "user_hometown");

        private static final String TAG_CANCEL = "CANCELLED";
        private static final String TAG_ERROR = "ERROR";
        private static final String TAG_RESPONSE = "REPONSE";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            FacebookSdk.sdkInitialize(getApplicationContext());
            AppEventsLogger.activateApp(this);
            callbackManager = CallbackManager.Factory.create();

            if (FacebookHelperUtils.getInstance().isFBLoggedIn()){
                onFacebookLogin();
            }else{
                //go to account login page...
                goToLoginPage();
            }

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_loading);

        }

        void onFacebookLogin(){
            LoginManager.getInstance().logInWithReadPermissions(this,permissionNeeds);

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
                                                try {

                                                    goToAccountSummaryPage();

                                                    String jsonresult = String.valueOf(json);
                                                    System.out.println("JSON Result"+jsonresult);

                                                    String str_fb_acc_name = json.getString("name");
                                                    FacebookHelperUtils.getInstance().userObject.fbUserName = str_fb_acc_name;
                                                    String str_id = json.getString("id");
                                                    FacebookHelperUtils.getInstance().userObject.fbUserID = str_id;

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
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

        void goToAccountSummaryPage(){
            Intent accSummaryIntent = new Intent(this,AccountSummaryActivity.class);
            startActivity(accSummaryIntent);

        }

        void goToProfilePage(){
            Intent profilePage = new Intent(this,MyProfilePage.class);
            startActivity(profilePage);
        }

        void goToLoginPage(){
            Intent loginIntent = new Intent(this,FBLoginActivty.class);
            startActivity(loginIntent);
        }

    }
