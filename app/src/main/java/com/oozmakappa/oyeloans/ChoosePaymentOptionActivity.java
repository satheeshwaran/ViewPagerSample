package com.oozmakappa.oyeloans;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;

import com.oozmakappa.oyeloans.Adapters.ChoosePaymentOptionAdapter;

import java.util.ArrayList;

public class ChoosePaymentOptionActivity extends AppCompatActivity {

    private ArrayList<String> paymentOptions = new ArrayList<>();
    ChoosePaymentOptionAdapter paymentOptionsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_payment_option_row);

        paymentOptions.add("Debit Card");
        paymentOptions.add("NetBanking");

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ListView paymentOptionsListView = (ListView) findViewById(R.id.paymentOptionsListView);
        if(paymentOptionsListView != null) {
            paymentOptionsAdapter = new ChoosePaymentOptionAdapter(this, paymentOptions);
            paymentOptionsListView.setAdapter(paymentOptionsAdapter);


            paymentOptionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    paymentOptionsAdapter.setSelectedIndex(position);
                    paymentOptionsAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}
