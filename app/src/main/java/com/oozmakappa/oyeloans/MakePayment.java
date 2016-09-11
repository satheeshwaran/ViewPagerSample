package com.oozmakappa.oyeloans;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.oozmakappa.oyeloans.Adapters.MakePaymentAmountsAdapter;

import java.util.ArrayList;
import java.util.HashMap;


public class MakePayment extends AppCompatActivity {

    private ArrayList<HashMap<String,String>> makePaymentValues;
    private MakePaymentAmountsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);
        setUpPaymentAmountsList();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void setUpPaymentAmountsList(){
        makePaymentValues = new ArrayList<>();

        HashMap<String,String> amountDueMap = new HashMap<>();
        amountDueMap.put("title","Amount Due");
        amountDueMap.put("subTitle","on 26/05/2016");
        amountDueMap.put("requiresEditText","false");
        amountDueMap.put("amountValue","Rs.1200");


        HashMap<String,String> fullBalanceMap = new HashMap<>();
        fullBalanceMap.put("title","Full Balance");
        fullBalanceMap.put("subTitle","This will pay off your loan");
        fullBalanceMap.put("amountValue","Rs.10000");
        fullBalanceMap.put("requiresEditText","false");

        HashMap<String,String> otherAmountMap = new HashMap<>();
        otherAmountMap.put("title","Other Amount");
        otherAmountMap.put("requiresEditText","true");

        makePaymentValues.add(amountDueMap);
        makePaymentValues.add(fullBalanceMap);
        makePaymentValues.add(otherAmountMap);

        adapter = new MakePaymentAmountsAdapter(this,makePaymentValues);
        ListView paymentAmountsList = (ListView) findViewById(R.id.makePaymentAmountsList);
        paymentAmountsList.setAdapter(adapter);
        paymentAmountsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelectedIndex(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

}
