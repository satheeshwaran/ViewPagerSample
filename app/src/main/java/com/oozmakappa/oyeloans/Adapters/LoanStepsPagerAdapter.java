package com.oozmakappa.oyeloans.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.oozmakappa.oyeloans.fragments.LoanApplicationStepOne;
import com.oozmakappa.oyeloans.fragments.LoanApplicationStepThree;
import com.oozmakappa.oyeloans.fragments.LoanApplicationStepTwo;

/**
 * Created by sankarnarayanan on 9/10/16.
 */

public class LoanStepsPagerAdapter extends FragmentStatePagerAdapter {


    Context context;

    public LoanStepsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position){
            case 0:
                fragment = new LoanApplicationStepOne();
                break;
            case 1:
                fragment = new LoanApplicationStepTwo();
                break;
            case 2:
                fragment = new LoanApplicationStepThree();
                break;
            default:
                //Just a fall back. this code will never come into picture.
                fragment = new LoanApplicationStepOne();
                break;

        }
        return fragment;
    }


    @Override
    public int getCount() {
        return 3;
    }

}
