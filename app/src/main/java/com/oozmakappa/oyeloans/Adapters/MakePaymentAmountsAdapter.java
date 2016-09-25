package com.oozmakappa.oyeloans.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by satheeshwaran on 9/11/16.
 */
public class MakePaymentAmountsAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<HashMap<String,String>> strings;
    public int selectedIndex = -1;
    public EditText amountEditValue;

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
        amountEditValue=(EditText)convertView.findViewById(R.id.rightEditText);

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
            amountEditValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (amountEditValue == null) return;
                    String string = s.toString();
                    amountEditValue.removeTextChangedListener(this);
                    String cleanString = string.toString().replaceAll("[₹,.]", "");
                    String formatted = NumberFormat.getCurrencyInstance().format(Double.valueOf(cleanString));
                    amountEditValue.setText(formatted);
                    amountEditValue.setSelection(formatted.length());
                    amountEditValue.addTextChangedListener(this);
                }
            });
        }else{
            amountValue.setVisibility(View.INVISIBLE);
            amountEditValue.setVisibility(View.VISIBLE);
            //amountEditValue.setText("Enter Amount");
        }

        RadioButton rbSelect = (RadioButton) convertView
                .findViewById(R.id.paymentAmountRadioButton);
        if(selectedIndex == position){
            rbSelect.setChecked(true);
            if (selectedIndex == 2)
                amountEditValue.requestFocus();
        }
        else{
            rbSelect.setChecked(false);
        }

        return convertView;
    }

    public class MoneyTextWatcher implements TextWatcher {
        private final WeakReference<EditText> editTextWeakReference;

        public MoneyTextWatcher(EditText editText) {
            editTextWeakReference = new WeakReference<EditText>(editText);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            EditText editText = editTextWeakReference.get();
            if (editText == null) return;
            String s = editable.toString();
            editText.removeTextChangedListener(this);
            String cleanString = s.toString().replaceAll("[₹,.]", "");
            String formatted = NumberFormat.getCurrencyInstance().format(Double.valueOf(s));
            editText.setText(formatted);
            editText.setSelection(formatted.length());
            editText.addTextChangedListener(this);
        }
    }
}
