package com.oozmakappa.oyeloans;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class PaymentSucceededActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_succeeded);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setElevation(0);
        }

        Button okButton = (Button)findViewById(R.id.payment_success_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentSucceededActivity.this.finish();
            }
        });

        TextView paymentSuccessTextView = (TextView) findViewById(R.id.payment_success_message);
        paymentSuccessTextView.setText("Payment of Rs."+getIntent().getStringExtra("payment_amount")+" succeeded!!");

        TextView paymentRemainingTextView = (TextView) findViewById(R.id.exisiting_loan_amount_message_success);
        paymentRemainingTextView.setText("Your Existing Loan balance would be Rs."+getIntent().getStringExtra("remaining_amount"));

    }
}
