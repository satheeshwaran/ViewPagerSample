package com.oozmakappa.oyeloans;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cooltechworks.creditcarddesign.CardEditActivity;
import com.cooltechworks.creditcarddesign.CreditCardUtils;
import com.oozmakappa.oyeloans.Adapters.ChoosePaymentOptionAdapter;

import java.util.ArrayList;

import layout.OnFragmentInteractionListener;
import layout.SelectDebitCardFragment;

public class ChoosePaymentOptionActivity extends AppCompatActivity implements OnFragmentInteractionListener{

    private static final int CONTENT_VIEW_ID = 10101010;
    private ArrayList<String> paymentOptions = new ArrayList<>();
    ChoosePaymentOptionAdapter paymentOptionsAdapter;
    LinearLayout debitCardContainerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_payment_option);

        paymentOptions.add("Debit Card");
        paymentOptions.add("NetBanking");

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ListView paymentOptionsListView = (ListView) findViewById(R.id.paymentOptionsListView);
        if (paymentOptionsListView != null) {
            paymentOptionsAdapter = new ChoosePaymentOptionAdapter(this, paymentOptions);
            paymentOptionsListView.setAdapter(paymentOptionsAdapter);


            paymentOptionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    paymentOptionsAdapter.setSelectedIndex(position);
                    paymentOptionsAdapter.notifyDataSetChanged();
                    if (position == 0) {
                        showDebitCardLayout();
                    }else{
                        showNetbankingLayout();
                    }
                }
            });
        }

        debitCardContainerLayout = (LinearLayout) findViewById(R.id.debitCardFragmentContainer);


        Button newDebitCardButton = (Button) findViewById(R.id.addNewDebitCard);
        newDebitCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int GET_NEW_CARD = 2;
                Intent intent = new Intent(ChoosePaymentOptionActivity.this, CardEditActivity.class);
                startActivityForResult(intent,GET_NEW_CARD);
            }
        });

    }

    void showDebitCardLayout() {
        debitCardContainerLayout.setVisibility(View.VISIBLE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SelectDebitCardFragment selectDebitCardFragment = SelectDebitCardFragment.newInstance("Test", "Test");
        fragmentTransaction.add(R.id.debitCardFragmentContainer, selectDebitCardFragment, "HELLO");
        fragmentTransaction.commit();
    }

    void showNetbankingLayout(){
        debitCardContainerLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {

            String cardHolderName = data.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME);
            String cardNumber = data.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER);
            String expiry = data.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY);
            String cvv = data.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV);

            // Your processing goes here.

        }
    }
}
