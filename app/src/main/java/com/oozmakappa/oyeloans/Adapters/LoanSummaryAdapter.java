package com.oozmakappa.oyeloans.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oozmakappa.oyeloans.Models.LoanSummaryModel;
import com.oozmakappa.oyeloans.R;

import java.util.ArrayList;

/**
 * Created by sankarnarayanan on 08/09/16.
 */
public class LoanSummaryAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    LoanSummaryModel tempValues=null;
    int i=0;


    /*************  CustomAdapter Constructor *****************/
    public LoanSummaryAdapter(Activity a, ArrayList d,Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        data=d;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView loanId;
        public TextView loanStatus;
        public ImageView image;

    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.tabitem, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.loanId = (TextView) vi.findViewById(R.id.text);
            holder.loanStatus=(TextView)vi.findViewById(R.id.text1);
            holder.image=(ImageView)vi.findViewById(R.id.image);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.loanId.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = ( LoanSummaryModel ) data.get( position );

            /************  Set Model values in Holder elements ***********/

            holder.loanId.setText( "Loan Id" +tempValues.getLoanId() );
            holder.loanStatus.setText("Status :" +tempValues.getLoanStatus() );

            Log.v("status", tempValues.getLoanStatus());

            if(tempValues.getLoanStatus().equals("Closed")){
                holder.image.setImageResource(
                        res.getIdentifier(
                                "com.oozmakappa.oyeloans:drawable/closed"
                                ,null,null));
            }else if(tempValues.getLoanStatus().equals("Pre- Closed")){
                holder.image.setImageResource(
                        res.getIdentifier(
                                "com.oozmakappa.oyeloans:drawable/preclosed"
                                ,null,null));
            }else if(tempValues.getLoanStatus().equals("Active")){
                holder.image.setImageResource(
                        res.getIdentifier(
                                "com.oozmakappa.oyeloans:drawable/active"
                                ,null,null));
            }


            /******** Set Item Click Listner for LayoutInflater for each row *******/

            //vi.setOnClickListener(new AdapterView.OnItemClickListener( position ));
        }
        return vi;
    }



}
