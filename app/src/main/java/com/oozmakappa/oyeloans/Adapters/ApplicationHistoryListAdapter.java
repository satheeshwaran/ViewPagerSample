package com.oozmakappa.oyeloans.Adapters;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oozmakappa.oyeloans.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by sankarnarayanan on 25/09/16.
 */
public class ApplicationHistoryListAdapter extends BaseAdapter {
    JSONArray result;
    Context context;
    private static LayoutInflater inflater=null;
    public ApplicationHistoryListAdapter(AppCompatActivity mainActivity, JSONArray prgmNameList) {
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
        TextView loanAmount;
        TextView status;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        try {
            JSONObject currObj = result.getJSONObject(position);
            Holder holder = new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.list_item_application_history, null);
            holder.loanAmount = (TextView) rowView.findViewById(R.id.loanAmountValue);
            holder.loanAmount.setText(currObj.getString("loan_amount"));
            holder.status = (TextView) rowView.findViewById(R.id.statusValue);
            holder.status.setText(currObj.getString("app_status"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                View container = (View) rowView.findViewById(R.id.indicatorView);
                container.setBackgroundColor(context.getResources().getColor(R.color.green_500));
            }
            rowView.setOnClickListener(new View.OnClickListener() {
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
