package com.oozmakappa.oyeloans;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.oozmakappa.oyeloans.Adapters.LoanPickerAdapter;
import com.oozmakappa.oyeloans.Adapters.MakePaymentAmountsAdapter;
import com.oozmakappa.oyeloans.Models.LoanSummaryModel;
import com.oozmakappa.oyeloans.utils.SharedDataManager;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;


public class MakePayment extends AppCompatActivity {


    public ArrayList<LoanSummaryModel> loanObjects;
    public LoanSummaryModel loanObject;

    private int selectedLoan = 0;

    private ArrayList<HashMap<String,String>> makePaymentValues;
    private MakePaymentAmountsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);

        if (getIntent().getBooleanExtra("MultiLoanPayment",false)){
            loanObjects = SharedDataManager.getInstance().activeLoans;
            setupMultiLoan();
        }else{
            loanObject = SharedDataManager.getInstance().singleLoan;
        }

        setUpPaymentAmountsList();

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


        Button proceedButton = (Button) findViewById(R.id.proceedToPay);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter!= null && adapter.selectedIndex != -1) {
                    Intent choosePaymentIntent = new Intent(MakePayment.this, ChoosePaymentOptionActivity.class);
                    choosePaymentIntent.putExtra("payable_amount","8000");
                    startActivity(choosePaymentIntent);
                }
            }
        });


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

    void setupMultiLoan(){

        Spinner spinner = (Spinner) findViewById(R.id.loanpicker_spinner);
        LoanPickerAdapter customAdapter=new LoanPickerAdapter(getApplicationContext(),this.loanObjects);
        spinner.setAdapter(customAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  TextView loanIdTextView = (TextView) MakePayment.this.findViewById(R.id.loanID);
                  loanIdTextView.setText(loanObjects.get(position).getLoanId());
                  TextView loanAmountTextView = (TextView) MakePayment.this.findViewById(R.id.loanAmount);
                  loanAmountTextView.setText(loanObjects.get(position).getLoanAmount());
                    setUpPaymentAmountsList();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });



    }

    void setUpPaymentAmountsList(){
        makePaymentValues = new ArrayList<>();

        LoanSummaryModel selectedLoanObject = null;

        if (getIntent().getBooleanExtra("MultiLoanPayment",false)){
            selectedLoanObject = loanObjects.get(selectedLoan);
        }else{
            selectedLoanObject = loanObject;
        }

        if (selectedLoanObject != null) {
            HashMap<String, String> amountDueMap = new HashMap<>();
            amountDueMap.put("title", "Amount Due");
            amountDueMap.put("subTitle", "on 26/05/2016");
            amountDueMap.put("requiresEditText", "false");
            amountDueMap.put("amountValue", selectedLoanObject.getLoanAmount());


            HashMap<String, String> fullBalanceMap = new HashMap<>();
            fullBalanceMap.put("title", "Full Balance");
            fullBalanceMap.put("subTitle", "This will pay off your loan");
            fullBalanceMap.put("amountValue", selectedLoanObject.getLoanAmount());
            fullBalanceMap.put("requiresEditText", "false");

            HashMap<String, String> otherAmountMap = new HashMap<>();
            otherAmountMap.put("subTitle", "");
            otherAmountMap.put("title", "Other Amount");
            otherAmountMap.put("requiresEditText", "true");

            makePaymentValues.add(amountDueMap);
            makePaymentValues.add(fullBalanceMap);
            makePaymentValues.add(otherAmountMap);

            adapter = new MakePaymentAmountsAdapter(this, makePaymentValues);
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

}
