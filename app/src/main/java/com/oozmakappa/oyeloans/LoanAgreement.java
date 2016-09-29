package com.oozmakappa.oyeloans;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.oozmakappa.oyeloans.utils.Utils;

public class LoanAgreement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_agreement);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setElevation(0);
            actionBar.setTitle("Loan Agreement");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }

        WebView webView = (WebView) findViewById(R.id.loanAgreementWebView);
        webView.loadUrl("file:///android_asset/LoanAgreement.html");

        ImageView proceed = (ImageView)findViewById(R.id.proceedLoanAgreement);
        proceed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Intent ecsAgreement = new Intent(LoanAgreement.this,ActivityEcsAgreement.class);
                startActivity(ecsAgreement);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                Utils.showLoading(LoanAgreement.this,"Loading...");
                // TODO show you progress image
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                Utils.removeLoading();
                // TODO hide your progress image
                super.onPageFinished(view, url);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loan_agreement, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                onBackPressed();
                return true;
//            case R.id.menu_proceed:
//                Intent ecsAgreement = new Intent(this,ActivityEcsAgreement.class);
//                startActivity(ecsAgreement);
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
