package com.oozmakappa.oyeloans;

import android.os.Build;
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

public class LoanRejectionActivity extends AppCompatActivity {

    @Override
    public void onStart() {
        super.onStart();
        Tracker t = ((AppController) this.getApplication()).getDefaultTracker();
        t.setScreenName("Loan Rejection");
        t.send(new HitBuilders.ScreenViewBuilder().build());
        t.enableAutoActivityTracking(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_rejection);

        if (getIntent().getStringExtra("loan_rejection_message") != null)
            ((TextView) findViewById(R.id.loan_rejection_message)).setText(getIntent().getStringExtra("loan_rejection_message"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.NavBarColor));
        }

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setElevation(0);
        }

        Button loanRejectionOk = (Button) findViewById(R.id.loan_rejected_ok);
        loanRejectionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button whatElseToDo = (Button) findViewById(R.id.what_to_do);
        whatElseToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
