package com.oozmakappa.oyeloans.HowitWorks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HowItWorksFragment extends Fragment {
 
    final static String LAYOUT_ID = "layoutid";
 
    public static HowItWorksFragment newInstance(int layoutId) {
        HowItWorksFragment pane = new HowItWorksFragment();
        Bundle args = new Bundle();
        args.putInt(LAYOUT_ID, layoutId);
        pane.setArguments(args);
        return pane;
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(getArguments().getInt(LAYOUT_ID, -1), container, false);
        return rootView;
    }
}