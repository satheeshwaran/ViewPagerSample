package com.oozmakappa.oyeloans.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.oozmakappa.oyeloans.ApplyLoanThirdActivity;
import com.oozmakappa.oyeloans.DataExtraction.AppController;
import com.oozmakappa.oyeloans.ApplyLoanFirstActivity;
import com.oozmakappa.oyeloans.ApplyLoanSecondActivity;
import com.oozmakappa.oyeloans.EditProfileActivity;
import com.oozmakappa.oyeloans.Models.LoanUser;
import com.oozmakappa.oyeloans.Models.SuccessModel;
import com.oozmakappa.oyeloans.R;
import com.oozmakappa.oyeloans.helper.WebServiceCallHelper;
import com.oozmakappa.oyeloans.utils.SharedDataManager;
import com.oozmakappa.oyeloans.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sankarnarayanan on 11/09/16.
 */
public class ApplyLoanPersonalInfo extends Fragment {

    OnProceedSelectedListener mCallback;
    AutoCompleteTextView firstNameField;
    AutoCompleteTextView lastNameField;
    AutoCompleteTextView phoneNumberField;
    AutoCompleteTextView dobField;
    AutoCompleteTextView doorNumberField;
    AutoCompleteTextView streetNameField;
    AutoCompleteTextView localityField;
    AutoCompleteTextView cityField;
    AutoCompleteTextView stateField;
    AutoCompleteTextView pinCodeField;
    AutoCompleteTextView emailAddressField;
    AutoCompleteTextView panNumberField;
    AutoCompleteTextView aadharCardField;

    final Calendar myCalendar = Calendar.getInstance();

    String fieldError = "";

    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnProceedSelectedListener {
        /**
         * Called by HeadlinesFragment when a list item is selected
         */
        public void onPersonalDetailsEntered(HashMap<String, String> data);
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

        firstNameField = (AutoCompleteTextView) v.findViewById(R.id.firstname);
        lastNameField = (AutoCompleteTextView) v.findViewById(R.id.lastname);
        phoneNumberField = (AutoCompleteTextView) v.findViewById(R.id.mobile_phone);
        dobField = (AutoCompleteTextView) v.findViewById(R.id.dob);
        emailAddressField = (AutoCompleteTextView) v.findViewById(R.id.email_add);
        panNumberField = (AutoCompleteTextView) v.findViewById(R.id.pan_number);
        aadharCardField = (AutoCompleteTextView) v.findViewById(R.id.adhaar_number);
        doorNumberField = (AutoCompleteTextView) v.findViewById(R.id.address_number);
        streetNameField = (AutoCompleteTextView) v.findViewById(R.id.street_name);
        localityField = (AutoCompleteTextView) v.findViewById(R.id.address_value);
        cityField = (AutoCompleteTextView) v.findViewById(R.id.city);
        pinCodeField = (AutoCompleteTextView) v.findViewById(R.id.pin_code);
        stateField = (AutoCompleteTextView) v.findViewById(R.id.state_value);



        LoanUser user = SharedDataManager.getInstance().userObject;
        if (user != null) {
            firstNameField.setText(user.firstName);
            lastNameField.setText(user.lastName);
            phoneNumberField.setText(user.mobileNumber);

            Date dob = null;
            String dateinIndianFormat=user.DOB;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            try {
                dob = simpleDateFormat.parse(user.DOB);
                simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                dateinIndianFormat = simpleDateFormat.format(dob);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            setupDatePickerForDOB(dob);

            dobField.setText(dateinIndianFormat);
            emailAddressField.setText(user.emailID);
            panNumberField.setText(user.PANNumber);
            aadharCardField.setText(user.aadharNumber);
            streetNameField.setText(user.street);
            localityField.setText(user.locaility);
            cityField.setText(user.city);
            stateField.setText(user.state);
            pinCodeField.setText(user.PINCode);
        }

        return v;
    }

    void setupDatePickerForDOB(Date actualDOB) {

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                dobField.setText(sdf.format(myCalendar.getTime()));
            }

        };

        if (actualDOB!=null)
            myCalendar.setTime(actualDOB);

        dobField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

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
        Tracker t = ((AppController) getActivity().getApplication()).getDefaultTracker();
        t.setScreenName("Loan application - Enter personal info screen");
        t.send(new HitBuilders.ScreenViewBuilder().build());
        t.enableAutoActivityTracking(true);
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
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.profileProceedButtonPersonal:
                    if (performValidations()) {
                        populateGivenData();
                        makeLoanApplicationCall();
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setMessage(fieldError);
                        alertDialogBuilder.setTitle("Unable to save!!");

                        alertDialogBuilder.setNegativeButton("Let me fix it!", null);

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }

                    // TODO Auto-generated method stub
                    break;
                case View.NO_ID:
                default:
                    // TODO Auto-generated method stub
                    break;
            }
        }
    };

    private boolean performValidations() {
        if (firstNameField.getText().length() <= 0 || lastNameField.getText().length() <= 0 || phoneNumberField.getText().length() <= 0 || dobField.getText().length() <= 0
                || doorNumberField.getText().length() <= 0 || streetNameField.getText().length() <= 0
                || cityField.getText().length() <= 0 || stateField.getText().length() <= 0 || pinCodeField.getText().length() <= 0
                || panNumberField.getText().length() <= 0
                || aadharCardField.getText().length() <= 0) {
            fieldError = "None of the fields can be empty, Please fill up all";
            return false;
        }

        if (!isValidPanCard(panNumberField.getText().toString())) {
            fieldError = "Invlaid PAN Number";
            return false;
        }

        if (!isValidAadharNumber(aadharCardField.getText().toString())) {
            fieldError = "Invlaid Aadhar Number";
            return false;
        }

        if (!isValidMobile(phoneNumberField.getText().toString())) {
            fieldError = "Invalid Phone number";
            return false;
        }

        if(!Pattern.compile("^[1-9][0-9]{5}$").matcher(pinCodeField.getText().toString()).matches()) {
            fieldError = "Invalid PIN Code";
            return false;
        }
        return true;
    }

    private boolean isValidMobile(String phone) {
        //^[2-9]{2}[0-9]{8}$
        return Pattern.compile("\\+?\\d[\\d -]{8,12}\\d").matcher(phone).matches();
    }

    private boolean isValidPanCard(String panCard){

        //panCard= panCard.trim();

        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");

        Matcher matcher = pattern.matcher(panCard);

        return matcher.matches();

    }


    private boolean isValidAadharNumber(String aadharCard){
        //aadharCard= aadharCard.trim();

        Pattern pattern = Pattern.compile("\\d{4}\\s?\\d{4}\\s?\\d{4}");

        Matcher matcher = pattern.matcher(aadharCard);

        return matcher.matches();
    }


    private void populateGivenData(){
        LoanUser user = SharedDataManager.getInstance().userObject;
        user.firstName = firstNameField.getText().toString();
        user.lastName = lastNameField.getText().toString();
        user.mobileNumber = phoneNumberField.getText().toString();
        user.DOB = dobField.getText().toString();
        user.doorNumber = doorNumberField.getText().toString();
        user.street = streetNameField.getText().toString();
        user.locaility = localityField.getText().toString();
        user.city = cityField.getText().toString();
        user.state = stateField.getText().toString();
        user.PINCode = pinCodeField.getText().toString();
        user.aadharNumber = aadharCardField.getText().toString();
        user.PANNumber = panNumberField.getText().toString();
    }

    private void makeLoanApplicationCall(){

        SharedDataManager.getInstance().activeApplication.loanUserObject = SharedDataManager.getInstance().userObject;
        Utils.showLoading(getActivity(),"Saving Personal Information...");
        WebServiceCallHelper webServiceHelper = new WebServiceCallHelper(new WebServiceCallHelper.OnWebServiceRequestCompletedListener() {
            @Override
            public void onRequestCompleted(SuccessModel model, String errorMessage) {
                com.oozmakappa.oyeloans.utils.Utils.removeLoading();
                if (errorMessage == null && model != null && model.getStatus().equals("success")) {
                    HashMap<String, String> firstPageData = new HashMap<String, String>();
                    firstPageData.put("Amount", "Data");
                    ((ApplyLoanFirstActivity)getActivity()).setCurrentItem(2, true);
                    mCallback.onPersonalDetailsEntered(firstPageData);
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
        webServiceHelper.makeNewApplicationServiceCall(SharedDataManager.getInstance().activeApplication, com.oozmakappa.oyeloans.DataExtraction.Utils.getDeviceId(getActivity()));
    }

}
