package com.oozmakappa.oyeloans;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by sankarnarayanan on 14/09/16.
 */
public class ApplicationFailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan_application_failed);
        Button okButton = (Button)findViewById(R.id.draw_cash);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationFailedActivity.this.finish();
            }
        });
    }
}
