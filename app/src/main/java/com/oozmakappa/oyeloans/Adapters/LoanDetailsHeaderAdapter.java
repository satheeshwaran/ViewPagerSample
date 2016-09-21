package com.oozmakappa.oyeloans.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.oozmakappa.oyeloans.fragments.LoanDetailsHeaderFragment;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by sankarnarayanan on 20/09/16.
 */
public class LoanDetailsHeaderAdapter extends FragmentStatePagerAdapter {

    public String loanHistoryData = "";

    public String loanInfoData = "";

    Context context;
    int carouselCount = 0;

    public LoanDetailsHeaderAdapter(FragmentManager fm, Context context, String loanHistoryData, String loanInfoData) {
        super(fm);
        this.context = context;
        this.loanHistoryData = loanHistoryData;
        this.loanInfoData = loanInfoData;
    }


    @Override
    public Fragment getItem(int position) {
        try {
            LoanDetailsHeaderFragment fragment;
            //make web service call and load the data.
            String loanInfoData = this.loanInfoData;
            JSONObject jsonLoan = new JSONObject(loanInfoData);
            String outstandingBal = jsonLoan.getString("ob");
            fragment = new LoanDetailsHeaderFragment();
            String string = outstandingBal;
            String[] parts = string.split("\\.");
            String part1 = parts[0];
            String part2 = parts[1];
            part2 = part2.concat("0");
            JSONObject jsonLoanObj = new JSONObject(loanHistoryData);
            JSONArray loanArrayList = jsonLoanObj.getJSONArray("loan_status_history");
            fragment.setValues(part1, part2, position, loanArrayList);

            return fragment;
        }catch(Exception e){
            return null;
        }
    }


    @Override
    public int getCount() {
        try {
            JSONObject jsonLoan = new JSONObject(loanHistoryData);
            JSONArray loanArrayList = jsonLoan.getJSONArray("loan_status_history");
            return loanArrayList.length();
        }catch(Exception e){
            return 0;
        }
    }

}
