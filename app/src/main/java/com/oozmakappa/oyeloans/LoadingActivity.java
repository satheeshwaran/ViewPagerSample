        package com.oozmakappa.oyeloans;

        import android.content.Intent;
        import android.os.Build;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.Toast;

        import com.facebook.CallbackManager;
        import com.facebook.FacebookCallback;
        import com.facebook.FacebookException;
        import com.facebook.FacebookSdk;
        import com.facebook.GraphRequest;
        import com.facebook.GraphResponse;
        import com.facebook.appevents.AppEventsLogger;
        import com.facebook.login.LoginManager;
        import com.facebook.login.LoginResult;
        import com.oozmakappa.oyeloans.Models.LoanUser;
        import com.oozmakappa.oyeloans.Models.SuccessModel;
        import com.oozmakappa.oyeloans.helper.WebServiceCallHelper;
        import com.oozmakappa.oyeloans.utils.FacebookHelperUtils;
        import com.oozmakappa.oyeloans.utils.SharedDataManager;
        import com.oozmakappa.oyeloans.utils.FacebookHelperUtilsCallback;
        import com.oozmakappa.oyeloans.utils.OyeConstants;

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

                if (SharedDataManager.getInstance().isFBLoggedIn()){
                    onFacebookLogin();
                }else{
                    //go to account login page...
                    goToLoginPage();
                }

                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_loading);
            }

            void onFacebookLogin(){
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
                                                                //SharedDataManager.getInstance().userObject.fbUserName = responseObject.getString("name");
                                                            WebServiceCallHelper webServiceHelper = new WebServiceCallHelper(new WebServiceCallHelper.OnWebServiceRequestCompletedListener(){
                                                                @Override
                                                                public void onRequestCompleted(SuccessModel model, String errorMessage){
                                                                    if (model.getStatus().equals("success")) {
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
                                Toast.makeText(getApplicationContext(),"Action Cancelled", Toast.LENGTH_SHORT).show();
                                Log.d(TAG_CANCEL,"On cancel");
                            }

                            @Override
                            public void onError(FacebookException error) {
                                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_SHORT).show();
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
                Intent profilePage = new Intent(this,EditMyProfilePage.class);
                startActivity(profilePage);
            }

            void goToLoginPage(){
                Intent loginIntent = new Intent(this,FBLoginActivty.class);
                startActivity(loginIntent);
            }
        }
