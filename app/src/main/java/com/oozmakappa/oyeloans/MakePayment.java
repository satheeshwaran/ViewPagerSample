package com.oozmakappa.oyeloans;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.crash.FirebaseCrash;
import com.oozmakappa.oyeloans.Adapters.LoanPickerAdapter;
import com.oozmakappa.oyeloans.Adapters.MakePaymentAmountsAdapter;
import com.oozmakappa.oyeloans.DataExtraction.AppController;
import com.oozmakappa.oyeloans.Models.LoanSummaryModel;
import com.oozmakappa.oyeloans.utils.SharedDataManager;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;


public class MakePayment extends AppCompatActivity {


    public ArrayList<LoanSummaryModel> loanObjects;
    public JSONObject loanObject;

    private int selectedLoan = 0;
    private String loanAmount = "";
    private String loanOutstandingAmount = "";

    private ArrayList<HashMap<String, String>> makePaymentValues;
    private MakePaymentAmountsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);

        if (getIntent().getBooleanExtra("MultiLoanPayment", false)) {
            loanObjects = SharedDataManager.getInstance().activeLoans;
            setupMultiLoan();
        } else {
            Spinner spinner = (Spinner) findViewById(R.id.loanpicker_spinner);
            spinner.setVisibility(View.GONE);
            loanObject = SharedDataManager.getInstance().singleLoan;
        }

        if (getIntent().getStringExtra("LoanID") != null){
            ((TextView)findViewById(R.id.loanID)).setText("Loan ID: "+getIntent().getStringExtra("LoanID"));
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
                if (adapter != null) {
                    String payableAmount = "";
                    switch (adapter.selectedIndex){
                        case 0:
                            payableAmount = loanOutstandingAmount;
                            break;
                        case 1:
                            payableAmount = loanAmount;
                            break;
                        case 2:
                            payableAmount = adapter.amountEditValue.getText().toString();
                            break;
                        default:
                            payableAmount = adapter.amountEditValue.getText().toString();

                    }
                    Intent choosePaymentIntent = new Intent(MakePayment.this, ChoosePaymentOptionActivity.class);
                    choosePaymentIntent.putExtra("payable_amount", payableAmount);
                    startActivity(choosePaymentIntent);
                }
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        Tracker t = ((AppController) this.getApplication()).getDefaultTracker();
        t.setScreenName("Make Payment screen");
        t.send(new HitBuilders.ScreenViewBuilder().build());
        t.enableAutoActivityTracking(true);
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

    void setupMultiLoan() {

        Spinner spinner = (Spinner) findViewById(R.id.loanpicker_spinner);
        LoanPickerAdapter customAdapter = new LoanPickerAdapter(getApplicationContext(), this.loanObjects);
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

    void setUpPaymentAmountsList() {
        makePaymentValues = new ArrayList<>();

        JSONObject selectedLoanObject = null;

        if (getIntent().getBooleanExtra("MultiLoanPayment", false) && loanObjects != null && loanObjects.size() > 0) {
            //selectedLoanObject = loanObjects.get(selectedLoan);
        } else {
            selectedLoanObject = loanObject;
        }
        try {
            if (selectedLoanObject != null) {
                HashMap<String, String> amountDueMap = new HashMap<>();
                amountDueMap.put("title", "Amount Due");
                amountDueMap.put("subTitle", "on 26/05/2016");
                amountDueMap.put("requiresEditText", "false");
                String loanAmount1 = "₹." + selectedLoanObject.getString("ob");
                loanOutstandingAmount = selectedLoanObject.getString("ob");
                amountDueMap.put("amountValue",loanAmount1);
                ((TextView)findViewById(R.id.loanAmount)).setText(loanAmount1);

                HashMap<String, String> fullBalanceMap = new HashMap<>();
                fullBalanceMap.put("title", "Full Balance");
                fullBalanceMap.put("subTitle", "This will pay off your loan");
                loanAmount = selectedLoanObject.getString("total_amount");
                fullBalanceMap.put("amountValue", "₹." + selectedLoanObject.getString("total_amount"));
                fullBalanceMap.put("requiresEditText", "false");

                HashMap<String, String> otherAmountMap = new HashMap<>();
                otherAmountMap.put("subTitle", "");
                otherAmountMap.put("title", "Other Amount");
                otherAmountMap.put("requiresEditText", "true");


                makePaymentValues.add(amountDueMap);
                makePaymentValues.add(fullBalanceMap);
                makePaymentValues.add(otherAmountMap);
            }
        } catch (Exception ex) {
            FirebaseCrash.log(ex.getLocalizedMessage());
        }
        adapter = new MakePaymentAmountsAdapter(this, makePaymentValues);
        final ListView paymentAmountsList = (ListView) findViewById(R.id.makePaymentAmountsList);
        paymentAmountsList.setAdapter(adapter);
        paymentAmountsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 2)
                {
                    paymentAmountsList.setItemsCanFocus(true);

                    // Use afterDescendants, because I don't want the ListView to steal focus
                    paymentAmountsList.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                }
                else
                {
                    paymentAmountsList.setItemsCanFocus(false);

                        // Use beforeDescendants so that the EditText doesn't re-take focus
                        paymentAmountsList.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                        paymentAmountsList.requestFocus();
                }

                adapter.setSelectedIndex(position);
                adapter.notifyDataSetChanged();
            }

            public void onNothingSelected(AdapterView<?> listView)
            {
                // This happens when you start scrolling, so we need to prevent it from staying
                // in the afterDescendants mode if the EditText was focused
                listView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
            }
        });
    }

}
