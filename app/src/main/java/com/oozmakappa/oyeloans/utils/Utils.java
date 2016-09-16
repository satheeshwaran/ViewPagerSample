package com.oozmakappa.oyeloans.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.Layout;
import android.view.View;


/**
 * Created by satheeshwaran on 9/16/16.
 */
public class Utils {

    public static ProgressDialog mDialog = null;

    public static void showLoading(Context context,String text){

        if (mDialog != null)
            if (mDialog.isShowing())
                mDialog.dismiss();

        mDialog = new ProgressDialog(context);
        mDialog.setMessage(text);
        mDialog.setCancelable(false);
        mDialog.show();
    }

    public static void removeLoading(){
        try
        {
            if (mDialog.isShowing())
                mDialog.dismiss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
