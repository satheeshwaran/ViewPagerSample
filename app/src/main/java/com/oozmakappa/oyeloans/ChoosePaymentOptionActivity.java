package com.oozmakappa.oyeloans;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cooltechworks.creditcarddesign.CardEditActivity;
import com.cooltechworks.creditcarddesign.CreditCardUtils;
import com.oozmakappa.oyeloans.Adapters.ChoosePaymentOptionAdapter;
import com.oozmakappa.oyeloans.Adapters.LoanPickerAdapter;
import com.oozmakappa.oyeloans.Adapters.NetbankingPickerAdapter;
import com.oozmakappa.oyeloans.Models.DebitCard;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import layout.AddNewDebitCardFragment;
import layout.OnFragmentInteractionListener;
import layout.SelectDebitCardFragment;

public class ChoosePaymentOptionActivity extends AppCompatActivity implements OnFragmentInteractionListener{

    private static final int CONTENT_VIEW_ID = 10101010;
    private ArrayList<String> paymentOptions = new ArrayList<>();
    private ArrayList<String> bankNames = new ArrayList<>();

    ChoosePaymentOptionAdapter paymentOptionsAdapter;
    RelativeLayout debitCardContainerLayout;
    RelativeLayout netBankingContainerLayout;

    ArrayList<DebitCard> savedDebitCards = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_payment_option);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }

        bankNames.add("HDFC Bank");
        bankNames.add("SBI");
        bankNames.add("Citi Bank");
        bankNames.add("Axis Banks");
        bankNames.add("ICICI Bank");

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        paymentOptions.add("Debit Card");
        paymentOptions.add("NetBanking");


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

        debitCardContainerLayout = (RelativeLayout) findViewById(R.id.debitCardFragmentContainer);
        netBankingContainerLayout = (RelativeLayout) findViewById(R.id.netBankingContainer);
        setupSavedDebitCards();

        Button newDebitCardButton = (Button) findViewById(R.id.makePaymentButton);
        newDebitCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paymentResultIntent = new Intent(ChoosePaymentOptionActivity.this,PaymentSucceededActivity.class);
                startActivity(paymentResultIntent);
            }
        });

    }

    void showDebitCardLayout() {

        /*if(debitCardContainerLayout.getChildCount() > 0)
            debitCardContainerLayout.removeAllViews();

        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (DebitCard card : this.savedDebitCards) {

            CreditCardView creditCardView = new CreditCardView(this);
            creditCardView.setCVV(card.debitCardCVV);
            creditCardView.setCardHolderName(card.debitCardName);
            creditCardView.setCardExpiry(card.debitCardExiry);
            creditCardView.setCardNumber(card.debitCardNumber);

            debitCardContainerLayout.addView(creditCardView);
        }

        AddNewDebitCardFragment addNewDebitCardFragment = AddNewDebitCardFragment.newInstance();
        fragmentTransaction.add(R.id.debitCardFragmentContainer, addNewDebitCardFragment, "HELLO");
        fragmentTransaction.commit();*/

        debitCardContainerLayout.setVisibility(View.VISIBLE);
        netBankingContainerLayout.setVisibility(View.INVISIBLE);

        ViewPager viewPager = (ViewPager) findViewById(R.id.debitCardPager);
        DebitCardPagerAdapter adapter = new DebitCardPagerAdapter(getSupportFragmentManager(),this);
        adapter.debitCards = this.savedDebitCards;
        viewPager.setAdapter(adapter);

        CirclePageIndicator titleIndicator = (CirclePageIndicator)findViewById(R.id.titles);
        titleIndicator.setFillColor(R.color.deep_orange_500);
        titleIndicator.setViewPager(viewPager);


    }

    void showNetbankingLayout(){
        debitCardContainerLayout.setVisibility(View.INVISIBLE);
        netBankingContainerLayout.setVisibility(View.VISIBLE);

        Spinner spinner = (Spinner) findViewById(R.id.netbanking_spinner);
        NetbankingPickerAdapter customAdapter=new NetbankingPickerAdapter(getApplicationContext(),this.bankNames);
        spinner.setAdapter(customAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    void setupSavedDebitCards(){
        DebitCard dc = new DebitCard();
        dc.debitCardName = "SATHEESHWARAN J";
        dc.debitCardCVV = "522";
        dc.debitCardExiry = "01/17";
        dc.debitCardNumber = "44906789000000000";

        this.savedDebitCards.add(dc);

        DebitCard dc1 = new DebitCard();
        dc1.debitCardName = "SATHEESHWARAN J";
        dc1.debitCardCVV = "123";
        dc1.debitCardExiry = "11/19";
        dc1.debitCardNumber = "57576789000000000";
        this.savedDebitCards.add(dc1);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

        final int GET_NEW_CARD = 2;
        Intent intent = new Intent(ChoosePaymentOptionActivity.this, CardEditActivity.class);
        startActivityForResult(intent,GET_NEW_CARD);

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



class DebitCardPagerAdapter extends FragmentStatePagerAdapter {

    Context context;
    ArrayList<DebitCard> debitCards;

    public DebitCardPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        if (position<debitCards.size()){
            fragment = SelectDebitCardFragment.newInstance(debitCards.get(position));
        }else{
            fragment = new AddNewDebitCardFragment();
        }
        return fragment;
    }


    @Override
    public int getCount() {
        return debitCards.size() + 1;
    }

}

