package com.oozmakappa.oyeloans.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.oozmakappa.oyeloans.fragments.ApplyLoanBankInfo;
import com.oozmakappa.oyeloans.fragments.ApplyLoanUploadDocuments;

/**
 * Created by sankarnarayanan on 14/09/16.
 */
public class UploadDocsPagerAdapter extends FragmentStatePagerAdapter {

    Context context;
    public UploadDocsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position){
            case 0:
                fragment = new ApplyLoanBankInfo();
                break;
            case 1:
                fragment = new ApplyLoanUploadDocuments();
                break;
            default:
                //Just a fall back. this code will never come into picture.
                fragment = new ApplyLoanBankInfo();
                break;

        }
        return fragment;
    }


    @Override
    public int getCount() {
        return 2;
    }

}
