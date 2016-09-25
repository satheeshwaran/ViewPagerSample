package com.oozmakappa.oyeloans.fragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.oozmakappa.oyeloans.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by sankarnarayanan on 20/09/16.
 */
public class LoanDetailsHeaderFragment extends Fragment {

    String amount, paise, totalLoanAmount;
    JSONArray totalArray;
    int index = 0,fillScore=0;


    public void setValues(String amount, String paise, int index, JSONArray totalArray,String loanAmount,int score){
        this.amount = amount;
        this.paise = paise;
        this.index = index;
        this.totalArray = totalArray;
        totalLoanAmount = loanAmount;
        fillScore = score;
    }


    public void setFragmentValues(String outstandingBal,String totalAmount,int score){
        String amt = "", pse= "";
        if (outstandingBal.contains(".")) {
            String string = outstandingBal;
            String[] parts = string.split("\\.");
            String part1 = parts[0];
            String part2 = parts[1];
            part2 = part2.concat("0");
            amt = part1;
            pse = part2;
        }else{
            amt = outstandingBal;
            pse = "00";
        }
        TextView amountText =  (TextView)getView().findViewById(R.id.amountValue);
        TextView paiseText = (TextView) getView().findViewById(R.id.paiseValue);
        amountText.setText(amt);
        paiseText.setText(pse);

        TextView totalAmountTextVIew = (TextView)getView().findViewById(R.id.total_loan_amount);
        String loanAmountText = "Loan Amount: " + totalAmount;
        totalAmountTextVIew.setText(loanAmountText);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.loaninfo_header_fragment, null);
        TextView amountText =  (TextView) v.findViewById(R.id.amountValue);
        TextView paiseText = (TextView) v.findViewById(R.id.paiseValue);
        if(this.amount != null){
            amountText.setText(this.amount);
        }
        if(this.paise != null){
            paiseText.setText(this.paise);
        }

        try {
            JSONArray currentArray = this.totalArray;
            JSONObject currentObj = currentArray.getJSONObject(index);
            TextView loanIdView = (TextView) v.findViewById(R.id.loanIdentifier);
            loanIdView.setText("Loan Id - ".concat(currentObj.getString("loan_id")));

            TextView totalAmountTextView = (TextView)v.findViewById(R.id.total_loan_amount);
            String loanAmountText = "Loan Amount: Rs." + totalLoanAmount.substring(0,totalLoanAmount.indexOf("."));
            totalAmountTextView.setText(loanAmountText);

        }catch (Exception e){
            e.printStackTrace();
        }

        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        animateLoanArcWithAmount(fillScore >= 10?fillScore:5);
    }

    @Override
    public void onPause() {
        super.onPause();
    }



    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void animateLoanArcWithAmount(final int percentage){
         getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final ArcProgress loanArcProgress = (ArcProgress) getActivity().findViewById(R.id.loan_arc_progress);
                loanArcProgress.setProgress(0);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        ObjectAnimator animation = ObjectAnimator.ofInt(loanArcProgress, "progress", 0, percentage);
                        animation.setDuration(percentage * 10);//25 for a fast but not to fast animation
                        animation.setInterpolator(new DecelerateInterpolator());
                        animation.start();

                    }
                }, 2000);
            }
        });
    }

}
