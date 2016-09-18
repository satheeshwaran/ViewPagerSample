package com.oozmakappa.oyeloans;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.oozmakappa.oyeloans.Models.LoanUser;
import com.oozmakappa.oyeloans.Models.SuccessModel;
import com.oozmakappa.oyeloans.helper.WebServiceCallHelper;
import com.oozmakappa.oyeloans.utils.SharedDataManager;
import com.oozmakappa.oyeloans.utils.Utils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();

    EditText firstNameField;
    EditText lastNameField;
    EditText phoneNumberField;
    EditText dobField;
    EditText doorNumberField;
    EditText streetNameField;
    EditText localityField;
    EditText cityField;
    EditText stateField;
    EditText pinCodeField;
    EditText universityNameField;
    EditText degreeValueField;
    EditText employerField;
    EditText designationField;
    EditText workStartDateField;
    EditText passoutYearField;
    EditText totalWorkExpField;

    String fieldError = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setupEditTexts();
        setupDatePickerForDOB();
        setupDatePickerForPassoutYear();
        setupDatePickerForEmploymentStartDate();

        Button saveButton = (Button) findViewById(R.id.editProfileProceedButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAllFormDetailsToUserObject();
            }
        });

        if (getIntent().getBooleanExtra("AllEdit", true)) {
            prefillUserInformation(true);
        } else {
            prefillUserInformation(false);
        }

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setElevation(0);
        }
    }

    void setupEditTexts() {
        firstNameField = (EditText) findViewById(R.id.editFirstNameValue);
        lastNameField = (EditText) findViewById(R.id.editLastNameValue);
        phoneNumberField = (EditText) findViewById(R.id.editphoneNumberValue);
        dobField = (EditText) findViewById(R.id.editDOBAgeValue);
        doorNumberField = (EditText) findViewById(R.id.editdoorNumberValue);
        streetNameField = (EditText) findViewById(R.id.editStreetNameValue);
        localityField = (EditText) findViewById(R.id.editStreetName1Value);
        cityField = (EditText) findViewById(R.id.editCityValue);
        stateField = (EditText) findViewById(R.id.editStateValue);
        pinCodeField = (EditText) findViewById(R.id.editPINCodeValue);
        universityNameField = (EditText) findViewById(R.id.universityValue);
        degreeValueField = (EditText) findViewById(R.id.degreeValue);
        employerField = (EditText) findViewById(R.id.editEmploymentValue);
        passoutYearField = (EditText) findViewById(R.id.passoutYearValue);
        workStartDateField = (EditText) findViewById(R.id.editEmploymentStartDateValue);
        totalWorkExpField = (EditText) findViewById(R.id.editTotalExperienceValue);
        designationField = (EditText) findViewById(R.id.editDesignationValue);

    }

    void setupDatePickerForDOB() {


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateTextField(dobField);
            }

        };


        dobField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditProfileActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    void setupDatePickerForEmploymentStartDate() {

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateTextField(workStartDateField);
            }

        };


        workStartDateField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditProfileActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

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
                updateDateTextField(passoutYearField);
            }

        };


        passoutYearField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditProfileActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }


    private void updateDateTextField(EditText text) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        text.setText(sdf.format(myCalendar.getTime()));
    }

    private void saveAllFormDetailsToUserObject() {

        if (performValidations()) {
            Utils.showLoading(EditProfileActivity.this,"Saving...");

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
            user.highestEducationPlace = universityNameField.getText().toString();
            user.highestEducation = degreeValueField.getText().toString();
            user.highestEducationYear = passoutYearField.getText().toString();
            user.workPlace = employerField.getText().toString();
            user.workStartDate = workStartDateField.getText().toString();
            user.highestEducationYear = designationField.getText().toString();
            user.totalWorkExperience = Integer.parseInt(totalWorkExpField.getText().toString());

            WebServiceCallHelper webServiceHelper = new WebServiceCallHelper(new WebServiceCallHelper.OnWebServiceRequestCompletedListener(){
                @Override
                public void onRequestCompleted(SuccessModel model, String errorMessage){
                    if (model.getStatus().equals("success")) {
                        Utils.removeLoading();
                        EditProfileActivity.this.finish();
                    }
                }
            });
            webServiceHelper.makeFacebookServiceCall(SharedDataManager.getInstance().userObject);

            // TODO: Remove this and make service call properly.

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditProfileActivity.this);
            alertDialogBuilder.setMessage(fieldError);
            alertDialogBuilder.setTitle("Updated Successfully");

            alertDialogBuilder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }else{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditProfileActivity.this);
            alertDialogBuilder.setMessage(fieldError);
            alertDialogBuilder.setTitle("Unable to save");

            alertDialogBuilder.setNegativeButton("Let me fix it",null);

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

    }

    private void prefillUserInformation(Boolean enableAllEdit) {

        LoanUser user = SharedDataManager.getInstance().userObject;
        firstNameField.setText(user.firstName);
        lastNameField.setText(user.lastName);
        phoneNumberField.setText(user.mobileNumber);
        dobField.setText(user.DOB);
        doorNumberField.setText(user.doorNumber);
        streetNameField.setText(user.street);
        localityField.setText(user.locaility);
        cityField.setText(user.city);
        stateField.setText(user.state);
        pinCodeField.setText(user.PINCode);
        universityNameField.setText(user.highestEducationPlace);
        degreeValueField.setText(user.highestEducation);
        passoutYearField.setText(user.highestEducationYear);
        employerField.setText(user.workPlace);
        workStartDateField.setText(user.workStartDate);
        designationField.setText(user.highestEducationYear);
        totalWorkExpField.setText(Integer.toString(user.totalWorkExperience));

        if (user.fbProfilePicURL != null && user.fbProfilePicURL.length() > 0) {
            Picasso.with(this)
                    .load(user.fbProfilePicURL)
                    .placeholder(R.drawable.profile)
                    .resize(400, 400)
                    .into((ImageView) findViewById(R.id.editprofileImage));
        }

        if (!enableAllEdit) {
            phoneNumberField.setEnabled(false);
            dobField.setEnabled(false);
            firstNameField.setEnabled(false);
            lastNameField.setEnabled(false);
        }
    }

    private boolean performValidations() {
        if (firstNameField.getText().length() <= 0 || lastNameField.getText().length() <= 0 || phoneNumberField.getText().length() <= 0 || dobField.getText().length() <= 0
                || doorNumberField.getText().length() <= 0 || streetNameField.getText().length() <= 0
                || cityField.getText().length() <= 0 || stateField.getText().length() <= 0 || pinCodeField.getText().length() <= 0
                || universityNameField.getText().length() <= 0 || degreeValueField.getText().length() <= 0 || workStartDateField.getText().length() <= 0
                || employerField.getText().length() <= 0 || designationField.getText().length() <= 0 || totalWorkExpField.getText().length() <= 0) {
            fieldError = "Fields cannot be empty";
            return false;
        }

        if (!isValidMobile(phoneNumberField.getText().toString())) {
            fieldError = "Invalid Phone number";
            return false;
        }
        return true;
    }

    private boolean isValidMobile(String phone)
    {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
}
