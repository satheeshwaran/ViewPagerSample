package com.oozmakappa.oyeloans.Adapters;

/**
 * Created by sankarnarayanan on 21/09/16.
 */
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oozmakappa.oyeloans.MainActivity;
import com.oozmakappa.oyeloans.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoanDashBoardListAdapter extends BaseAdapter{
    JSONArray result;
    Context context;
    private static LayoutInflater inflater=null;
    public LoanDashBoardListAdapter(AppCompatActivity mainActivity, JSONArray prgmNameList) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=mainActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView payableAmount;
        TextView dueDate;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        try {
            JSONObject currObj = result.getJSONObject(position);
            Holder holder = new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.listitem_dashboardloan, null);
            holder.dueDate = (TextView) rowView.findViewById(R.id.dueDateValue);
            holder.dueDate.setText(currObj.getString("scheduled_due_date"));
            holder.payableAmount = (TextView) rowView.findViewById(R.id.paymentAmountValue);
            holder.payableAmount.setText(currObj.getString("payment_amount"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                View container = (View) rowView.findViewById(R.id.indicatorView);
                if (currObj.get("payment_amount").equals(currObj.getString("paid_amount"))) {
                    container.setBackgroundColor(context.getResources().getColor(R.color.green_500));
                }else{
                    container.setBackgroundColor(context.getResources().getColor(R.color.red_500));
                }
            }
            rowView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //Toast.makeText(context, "You Clicked " + result[position], Toast.LENGTH_LONG).show();
                }
            });
            return rowView;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return  null;
    }

}
