package com.oozmakappa.oyeloans;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.oozmakappa.oyeloans.DataExtraction.AppController;
import com.oozmakappa.oyeloans.Models.LoanUser;
import com.oozmakappa.oyeloans.Models.SuccessModel;
import com.oozmakappa.oyeloans.helper.WebServiceCallHelper;
import com.oozmakappa.oyeloans.utils.SharedDataManager;
import com.oozmakappa.oyeloans.utils.Utils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import static android.view.View.GONE;

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
    EditText emailIDField;
    TextView emailIDTitle;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
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
        emailIDField = (EditText)findViewById(R.id.emailIDValue);
        emailIDTitle = (TextView)findViewById(R.id.emailTitle);

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

            LoanUser user = SharedDataManager.getInstance().userObject;
            user.firstName = firstNameField.getText().toString();
            user.lastName = lastNameField.getText().toString();
            user.mobileNumber = phoneNumberField.getText().toString();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date dob = sdf.parse(dobField.getText().toString());
                SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                user.DOB = sdf1.format(dob);
            }catch (Exception ex){
                ex.printStackTrace();
            }
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
            user.workTitle = designationField.getText().toString();
            user.totalWorkExperience = Integer.parseInt(totalWorkExpField.getText().toString());
            if (emailIDField.getVisibility() != GONE) {
                user.emailID = emailIDField.getText().toString();
            }

            Utils.showLoading(this,"Saving...");

            WebServiceCallHelper webServiceHelper = new WebServiceCallHelper(new WebServiceCallHelper.OnWebServiceRequestCompletedListener(){
                @Override
                public void onRequestCompleted(SuccessModel model, String errorMessage){
                    if (model!=null && model.getStatus().equals("success")) {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(EditProfileActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("made_fb_call_" + SharedDataManager.getInstance().userObject.fbUserID, true);
                        editor.apply();
                        Utils.removeLoading();
                        EditProfileActivity.this.finish();
                    }
                }
            });
            webServiceHelper.makeFacebookServiceCall(SharedDataManager.getInstance().userObject);

            // TODO: Remove this and make service call properly.

            /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditProfileActivity.this);
            alertDialogBuilder.setMessage(fieldError);
            alertDialogBuilder.setTitle("Updated Successfully");

            alertDialogBuilder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();*/

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
        ((TextView)findViewById(R.id.email)).setText((user.emailID.length()>0?user.emailID:""));
        if (user.fbProfilePicURL != null && user.fbProfilePicURL.length() > 0) {
            Picasso.with(this)
                    .load(user.fbProfilePicURL)
                    .placeholder(R.drawable.profile)
                    .resize(400, 400)
                    .into((ImageView) findViewById(R.id.editprofileImage));
        }

        if (!enableAllEdit) {
           // phoneNumberField.setEnabled(false);
            dobField.setEnabled(false);
            firstNameField.setEnabled(false);
            lastNameField.setEnabled(false);
        }

        if (user.emailID.length()>0){
            emailIDField.setVisibility(GONE);
            emailIDTitle.setVisibility(GONE);
        }
    }

    private boolean performValidations() {
        if (firstNameField.getText().length() <= 0 || lastNameField.getText().length() <= 0 || phoneNumberField.getText().length() <= 0 || dobField.getText().length() <= 0
                || doorNumberField.getText().length() <= 0 || streetNameField.getText().length() <= 0
                || cityField.getText().length() <= 0 || stateField.getText().length() <= 0 || pinCodeField.getText().length() <= 0
                || universityNameField.getText().length() <= 0 || degreeValueField.getText().length() <= 0 || workStartDateField.getText().length() <= 0
                || employerField.getText().length() <= 0 || designationField.getText().length() <= 0 || totalWorkExpField.getText().length() <= 0 ) {
            fieldError = "Fields cannot be empty";
            return false;
        }

        if (emailIDField.getVisibility() != GONE) {
            if (!Utils.isValidEmail(emailIDField.getText().toString())) {
                fieldError = "Invalid Email address";
                return false;
            }
        }

        if (!isValidMobile(phoneNumberField.getText().toString())) {
            fieldError = "Invalid Phone number";
            return false;
        }

        return true;
    }

    private boolean isValidMobile(String phone)
    {
        //^[2-9]{2}[0-9]{8}$
        return  Pattern.compile("\\+?\\d[\\d -]{8,12}\\d").matcher(phone).matches();
    }


    @Override
    public void onStart() {
        super.onStart();
        Tracker t = ((AppController) this.getApplication()).getDefaultTracker();
        t.setScreenName("Edit Profile");
        t.send(new HitBuilders.ScreenViewBuilder().build());
        t.enableAutoActivityTracking(true);
    }
}
