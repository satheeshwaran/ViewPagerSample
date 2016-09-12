package com.oozmakappa.oyeloans.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.oozmakappa.oyeloans.MakePayment;
import com.oozmakappa.oyeloans.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by satheeshwaran on 9/11/16.
 */
public class MakePaymentAmountsAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<HashMap<String,String>> strings;
    public int selectedIndex = -1;

    public MakePaymentAmountsAdapter(Context context,ArrayList<HashMap<String,String>> strings) {
        super(context,R.layout.make_payment_options_row);
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
                    R.layout.make_payment_options_row, null);
        }

        if (position%2 == 0)
            convertView.setBackgroundColor(context.getResources().getColor(R.color.grey_200));

        TextView title = (TextView)convertView.findViewById(R.id.mainTextTitle);
        TextView subtitle=(TextView)convertView.findViewById(R.id.subTextTitle);
        TextView amountValue=(TextView)convertView.findViewById(R.id.rightTitleText);
        EditText amountEditValue=(EditText)convertView.findViewById(R.id.rightEditText);

        HashMap<String,String> map = strings.get(position);

        title.setText(map.get("title"));
        subtitle.setText(map.get("subTitle"));

        if (map.get("subTitle") == null || map.get("subTitle").length() == 0){
            title.setGravity(Gravity.CENTER_VERTICAL);
        }

        Boolean requiresTextField = Boolean.parseBoolean(map.get("requiresEditText"));

        if (!requiresTextField) {
            amountValue.setText(map.get("amountValue"));
            amountEditValue.setVisibility(View.INVISIBLE);
            amountValue.setVisibility(View.VISIBLE);
        }else{
            amountValue.setVisibility(View.INVISIBLE);
            amountEditValue.setVisibility(View.VISIBLE);
            amountEditValue.setText("Enter Amount");
        }

        RadioButton rbSelect = (RadioButton) convertView
                .findViewById(R.id.paymentAmountRadioButton);
        if(selectedIndex == position){
            rbSelect.setChecked(true);
        }
        else{
            rbSelect.setChecked(false);
        }

        return convertView;
    }
}
