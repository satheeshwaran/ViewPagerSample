package com.oozmakappa.oyeloans.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.oozmakappa.oyeloans.R;

import java.util.ArrayList;

/**
 * Created by satheeshwaran on 9/11/16.
 */
public class ChoosePaymentOptionAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> strings;
    public int selectedIndex = -1;

    public ChoosePaymentOptionAdapter(Context context,ArrayList<String> strings) {
        super(context, R.layout.make_payment_options_row);
        this.context = context;
        this.strings = strings;
    }

    @Override
    public int getCount() {
        return strings.size();
    }

    public void setSelectedIndex(int index){
        selectedIndex = index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = (View) inflater.inflate(
                    R.layout.choose_payment_option_row, null);
        }

        convertView.setBackgroundColor(context.getResources().getColor(R.color.halfWhiteColor));

        TextView title = (TextView)convertView.findViewById(R.id.paymentTypeTitle);
        title.setText(strings.get(position));

        RadioButton rbSelect = (RadioButton) convertView
                .findViewById(R.id.paymentOptionRadioButton);
        if(selectedIndex == position){
            rbSelect.setChecked(true);
        }
        else{
            rbSelect.setChecked(false);
        }

        return convertView;
    }
}
