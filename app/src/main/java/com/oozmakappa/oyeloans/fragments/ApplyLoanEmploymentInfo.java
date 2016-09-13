package com.oozmakappa.oyeloans.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.oozmakappa.oyeloans.ApplyLoanSecondActivity;
import com.oozmakappa.oyeloans.EditMyProfilePage;
import com.oozmakappa.oyeloans.R;

/**
 * Created by sankarnarayanan on 12/09/16.
 */
public class ApplyLoanEmploymentInfo extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_employment_details, null);
        return v;
    }


    @Override
    public void onStart() {

        Spinner staticSpinner = (Spinner) getActivity().findViewById(R.id.static_spinner_employment_status);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(getActivity(), R.array.employment_array,
                        R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner.setAdapter(staticAdapter);

        Button proceedButton = (Button) getActivity().findViewById(R.id.profileProceedButtonEmployment);
        proceedButton.setOnClickListener(buttonClickListener);

        super.onStart();
    }


    private View.OnClickListener buttonClickListener = new View.OnClickListener() {

        public void onClick(View v) {
            Intent applicationPageTwo = new Intent(getActivity(),ApplyLoanSecondActivity.class);
            startActivity(applicationPageTwo);
        }
    };

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

}