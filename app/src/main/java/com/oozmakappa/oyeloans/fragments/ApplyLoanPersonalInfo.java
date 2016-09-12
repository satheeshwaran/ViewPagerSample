package com.oozmakappa.oyeloans.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.oozmakappa.oyeloans.R;

import java.util.HashMap;

/**
 * Created by sankarnarayanan on 11/09/16.
 */
public class ApplyLoanPersonalInfo extends Fragment {

    OnProceedSelectedListener mCallback;

    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnProceedSelectedListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onPersonalDetailsEntered(HashMap<String,String> data);
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnProceedSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_personal_details, null);
        return v;
    }


    @Override
    public void onStart() {

        Spinner staticSpinner = (Spinner) getActivity().findViewById(R.id.static_spinner);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(getActivity(), R.array.gender_array,
                        R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner.setAdapter(staticAdapter);

        Button proceedButton = (Button) getActivity().findViewById(R.id.profileProceedButtonPersonal);
        if (proceedButton != null) {
            proceedButton.setOnClickListener(buttonClickListener);
        }

        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }



    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            switch (v.getId()) {
                case R.id.profileProceedButtonPersonal:
                    HashMap<String,String> firstPageData = new HashMap<String,String>();
                    firstPageData.put("Amount", "Data");
                    mCallback.onPersonalDetailsEntered(firstPageData);
                    // TODO Auto-generated method stub
                    break;
                case View.NO_ID:
                default:
                    // TODO Auto-generated method stub
                    break;
            }
        }
    };

}
