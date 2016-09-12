package com.oozmakappa.oyeloans.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oozmakappa.oyeloans.Models.LoanSummaryModel;
import com.oozmakappa.oyeloans.R;

import java.util.ArrayList;

/**
 * Created by satheeshwaran on 9/12/16.
 */
public class LoanPickerAdapter extends BaseAdapter {
    Context context;
    ArrayList<LoanSummaryModel> loanDescriptions;
    LayoutInflater inflter;

    public LoanPickerAdapter(Context applicationContext,ArrayList<LoanSummaryModel> loanDescriptions) {
        this.context = applicationContext;
        this.loanDescriptions = loanDescriptions;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return loanDescriptions.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.loan_spinner, null);
        //ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        //icon.setImageResource(flags[i]);
        names.setText(loanDescriptions.get(i).getLoanId() + " "  + "Amount: " + loanDescriptions.get(i).getLoanAmount());
        return view;
    }
}