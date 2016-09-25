package com.oozmakappa.oyeloans;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.oozmakappa.oyeloans.DataExtraction.AppController;
import com.oozmakappa.oyeloans.Models.SuccessModel;
import com.oozmakappa.oyeloans.helper.WebServiceCallHelper;
import com.oozmakappa.oyeloans.utils.OyeConstants;
import com.oozmakappa.oyeloans.utils.SharedDataManager;
import com.oozmakappa.oyeloans.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class ReferFriendActivity extends AppCompatActivity {

    String referallCode = "";
     final static int referallIntentCode = 23042;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referralpage);
        getSupportActionBar().setTitle("Refer a friend");
        Button sendInvite = (Button)findViewById(R.id.inviteFriendsButton);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.NavBarColor));
        }


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);

        sendInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
                txtIntent .setType("text/plain");
                txtIntent .putExtra(android.content.Intent.EXTRA_SUBJECT, "Oye Referral Code");
                txtIntent .putExtra(android.content.Intent.EXTRA_TEXT, "Please use this referral code " + referallCode + " for applying a loan on Oye Loans. Download the app here https://play.google.com/store/apps/details?id=com.facebook.orca&hl=en");
                startActivityForResult(Intent.createChooser(txtIntent ,"Share"),referallIntentCode);
            }

        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ReferFriendActivity.this);
        referallCode =  preferences.getString(OyeConstants.REFERAL_CODE,null);
        if (referallCode == null)
            getReferalCode();
        else
            ((TextView)findViewById(R.id.referal_code_textview)).setText(referallCode);

    }

    @Override
    public void onStart() {
        super.onStart();
        Tracker t = ((AppController) this.getApplication()).getDefaultTracker();
        t.setScreenName("Refer a Friend screen");
        t.send(new HitBuilders.ScreenViewBuilder().build());
        t.enableAutoActivityTracking(true);
    }


    public void getReferalCode(){
        Utils.showLoading(this,"Getting Your Referral code...");
        WebServiceCallHelper webServiceHelper = new WebServiceCallHelper(new WebServiceCallHelper.OnWebServiceRequestCompletedListener() {
            @Override
            public void onRequestCompleted(SuccessModel model, String errorMessage) {
                if (model.response != null && model.response.length() >0) {
                    Utils.removeLoading();
                    try {
                        JSONObject responseObject = new JSONObject(model.response);
                        referallCode = responseObject.getString("referral_code");
                        ((TextView)findViewById(R.id.referal_code_textview)).setText(referallCode);

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ReferFriendActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();

                        // Save to SharedPreferences
                        editor.putString(OyeConstants.REFERAL_CODE, referallCode);
                        editor.apply();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        webServiceHelper.getReferallCodeService(SharedDataManager.getInstance().userObject.emailID);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case referallIntentCode:
                if (resultCode == RESULT_OK) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReferFriendActivity.this);
                    alertDialogBuilder.setMessage("Thanks for referring a friend, you will get your voucher if your friend aplies loan with the referall code");
                    alertDialogBuilder.setTitle("Awesome");

                    alertDialogBuilder.setNegativeButton("Cool!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                break;
            default:
                break;
        }
    }
}


