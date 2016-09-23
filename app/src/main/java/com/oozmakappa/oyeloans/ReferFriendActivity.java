package com.oozmakappa.oyeloans;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referralpage);
        getSupportActionBar().setTitle("Refer a friend");
        Button sendInvite = (Button)findViewById(R.id.inviteFriendsButton);

        sendInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
                txtIntent .setType("text/plain");
                txtIntent .putExtra(android.content.Intent.EXTRA_SUBJECT, "Oye Referral Code");
                txtIntent .putExtra(android.content.Intent.EXTRA_TEXT, "Please use this referral code " + referallCode + " for applying a loan on Oye Loans. Download the app here https://play.google.com/store/apps/details?id=com.facebook.orca&hl=en");
                startActivity(Intent.createChooser(txtIntent ,"Share"));

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
}

