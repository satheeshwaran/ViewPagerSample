package com.oozmakappa.oyeloans.fragments;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.oozmakappa.oyeloans.ApplyLoanSecondActivity;
import com.oozmakappa.oyeloans.ApplyLoanThirdActivity;
import com.oozmakappa.oyeloans.DataExtraction.AppController;
import com.oozmakappa.oyeloans.Models.LoanUser;
import com.oozmakappa.oyeloans.Models.SuccessModel;
import com.oozmakappa.oyeloans.R;
import com.oozmakappa.oyeloans.helper.WebServiceCallHelper;
import com.oozmakappa.oyeloans.utils.SharedDataManager;
import com.oozmakappa.oyeloans.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by sankarnarayanan on 12/09/16.
 */
public class ApplyLoanEmploymentInfo extends Fragment {

    AutoCompleteTextView universityNameField;
    AutoCompleteTextView degreeValueField;
    AutoCompleteTextView employerField;
    AutoCompleteTextView designationField;
    AutoCompleteTextView workStartDateField;
    AutoCompleteTextView workPhone;
    AutoCompleteTextView passoutYearField;
    AutoCompleteTextView grossMonthlyIncome;
    Spinner employmentTypeSpinner;
    String fieldError = "";
    String employmentStatus = "Full-time";

    final Calendar myCalendar = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_employment_details, null);

        universityNameField = (AutoCompleteTextView) v.findViewById(R.id.universityValue);
        degreeValueField = (AutoCompleteTextView) v.findViewById(R.id.qualification);
        passoutYearField = (AutoCompleteTextView) v.findViewById(R.id.passout_year);
        employerField = (AutoCompleteTextView) v.findViewById(R.id.empname);
        designationField = (AutoCompleteTextView) v.findViewById(R.id.occupation);
        workStartDateField = (AutoCompleteTextView) v.findViewById(R.id.date_joined);
        grossMonthlyIncome = (AutoCompleteTextView) v.findViewById(R.id.gross_income);
        workPhone = (AutoCompleteTextView) v.findViewById(R.id.emp_phone);

        setupDatePickerForPassoutYear();

        LoanUser user = SharedDataManager.getInstance().userObject;
        if (user != null) {
            universityNameField.setText(user.highestEducationPlace);
            degreeValueField.setText(user.highestEducation);
            passoutYearField.setText(user.highestEducationYear);
            employerField.setText(user.workPlace);
            designationField.setText(user.workTitle);
            workStartDateField.setText(user.workStartDate);
            workPhone.setText(user.workPhone);
            grossMonthlyIncome.setText(user.monthlyIncome);
        }
        return v;
    }


    void setupDatePickerForPassoutYear() {

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                passoutYearField.setText(sdf.format(myCalendar.getTime()));
            }

        };


        passoutYearField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                workStartDateField.setText(sdf.format(myCalendar.getTime()));
            }

        };


        workStartDateField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }
    @Override
    public void onStart() {

        employmentTypeSpinner = (Spinner) getActivity().findViewById(R.id.static_spinner_employment_status);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(getActivity(), R.array.employment_array,
                        R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        employmentTypeSpinner.setAdapter(staticAdapter);

        employmentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                employmentStatus=getResources().getStringArray(R.array.employment_array)[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Button proceedButton = (Button) getActivity().findViewById(R.id.profileProceedButtonEmployment);
        proceedButton.setOnClickListener(buttonClickListener);

        super.onStart();
        Tracker t = ((AppController) getActivity().getApplication()).getDefaultTracker();
        t.setScreenName("Loan application - Enter Employment info screen");
        t.send(new HitBuilders.ScreenViewBuilder().build());
        t.enableAutoActivityTracking(true);
    }


    private View.OnClickListener buttonClickListener = new View.OnClickListener() {

        public void onClick(View v) {

            if (performValidations()) {
                populateGivenData();
                makeEmploymentInfoCall();

            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setMessage(fieldError);
                alertDialogBuilder.setTitle("Unable to save!!");

                alertDialogBuilder.setNegativeButton("Let me fix it!", null);

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

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


    private boolean performValidations() {
        if (universityNameField.getText().length() <= 0 || degreeValueField.getText().length() <= 0 || workStartDateField.getText().length() <= 0
                || employerField.getText().length() <= 0 || designationField.getText().length() <= 0 || workPhone.getText().length() <= 0  || passoutYearField.getText().length() <= 0|| grossMonthlyIncome.getText().length() <= 0) {
            fieldError = "None of the fields can be empty, Please fill up all";
            return false;
        }

        if (!isValidMobile(workPhone.getText().toString())) {
            fieldError = "Invalid Phone number";
            return false;
        }

        return true;
    }

    private boolean isValidMobile(String phone) {
        //^[2-9]{2}[0-9]{8}$
        return Pattern.compile("\\+?\\d[\\d -]{8,12}\\d").matcher(phone).matches();
    }
    private void populateGivenData(){
        LoanUser user = SharedDataManager.getInstance().userObject;

        user.highestEducationPlace = universityNameField.getText().toString();
        user.highestEducation = degreeValueField.getText().toString();
        user.highestEducationYear = passoutYearField.getText().toString();
        user.workPlace = employerField.getText().toString();
        user.workTitle = designationField.getText().toString();
        user.workStartDate = workStartDateField.getText().toString();
        user.workPhone = workPhone.getText().toString();
        user.monthlyIncome = grossMonthlyIncome.getText().toString();
        user.workStatus = employmentStatus;

    }

    private void makeEmploymentInfoCall(){

        Utils.showLoading(getActivity(),"Saving Employment Information...");

        WebServiceCallHelper webServiceHelper = new WebServiceCallHelper(new WebServiceCallHelper.OnWebServiceRequestCompletedListener() {
            @Override
            public void onRequestCompleted(SuccessModel model, String errorMessage) {
                com.oozmakappa.oyeloans.utils.Utils.removeLoading();
                if (errorMessage == null && model != null && model.getStatus().equals("success")) {
                    Intent applicationPageTwo = new Intent(getActivity(),ApplyLoanSecondActivity.class);
                    startActivity(applicationPageTwo);
                }else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Error!");
                    if (model!=null)
                        alertDialogBuilder.setMessage(model.getDescription());
                    else
                        alertDialogBuilder.setMessage("Unknown error, please try again.");
                    alertDialogBuilder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });
        webServiceHelper.makeEmploymentInfoServiceCall(SharedDataManager.getInstance().activeApplication);

    }

}