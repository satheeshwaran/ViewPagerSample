package com.oozmakappa.oyeloans.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.oozmakappa.oyeloans.fragments.ApplyLoanEmploymentInfo;
import com.oozmakappa.oyeloans.fragments.ApplyLoanFirstFragment;
import com.oozmakappa.oyeloans.fragments.ApplyLoanPersonalInfo;

/**
 * Created by sankarnarayanan on 14/09/16.
 */
public class EnterDetailsPagerAdapter extends FragmentStatePagerAdapter {

    Context context;
    public EnterDetailsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position){
            case 0:
                fragment = new ApplyLoanFirstFragment();
                break;
            case 1:
                fragment = new ApplyLoanPersonalInfo();
                break;
            case 2:
                fragment = new ApplyLoanEmploymentInfo();
                break;
            default:
                //Just a fall back. this code will never come into picture.
                fragment = new ApplyLoanFirstFragment();
                break;

        }
        return fragment;
    }


    @Override
    public int getCount() {
        return 3;
    }

}
