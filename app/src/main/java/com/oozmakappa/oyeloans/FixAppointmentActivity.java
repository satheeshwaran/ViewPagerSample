package com.oozmakappa.oyeloans;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.oozmakappa.oyeloans.DataExtraction.AppController;
import com.oozmakappa.oyeloans.Models.SuccessModel;
import com.oozmakappa.oyeloans.helper.WebServiceCallHelper;
import com.oozmakappa.oyeloans.utils.SharedDataManager;
import com.oozmakappa.oyeloans.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FixAppointmentActivity extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();

    @Override
    public void onStart() {
        super.onStart();
        Tracker t = ((AppController) this.getApplication()).getDefaultTracker();
        t.setScreenName("Fix Appointment Screen");
        t.send(new HitBuilders.ScreenViewBuilder().build());
        t.enableAutoActivityTracking(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_appointment);
        setupDatePickerForAppointmentDate();
        setupDatePickerForAppointmentTime();

        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.NavigationMenuColor));
        }

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setElevation(0);
        }

        Button bookAppointmentButton = (Button) findViewById(R.id.bookAppointmentButton);
        bookAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Integrate web service here...
                Utils.showLoading(FixAppointmentActivity.this,"Booking your appointment...");

                WebServiceCallHelper webServiceHelper = new WebServiceCallHelper(new WebServiceCallHelper.OnWebServiceRequestCompletedListener() {
                    @Override
                    public void onRequestCompleted(SuccessModel model, String errorMessage) {
                        com.oozmakappa.oyeloans.utils.Utils.removeLoading();
                        if (errorMessage == null && model != null && model.getStatus().equals("success")) {
                            Intent thanksScreen = new Intent(FixAppointmentActivity.this, ApplicationCompletedActivity.class);
                            startActivity(thanksScreen);
                            FixAppointmentActivity.this.finish();

                        }else{
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FixAppointmentActivity.this);
                            alertDialogBuilder.setTitle("Error!");
                            if (model!=null)
                                alertDialogBuilder.setMessage(model.getDescription());
                            else
                                alertDialogBuilder.setMessage("Unknown error");
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
                webServiceHelper.makeAgreementInfoSaveServiceCall(SharedDataManager.getInstance().activeApplication);


            }
        });
    }

    void setupDatePickerForAppointmentDate() {

        final EditText appointmentField = (EditText) findViewById(R.id.appointmentDateTextField);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                appointmentField.setText(sdf.format(myCalendar.getTime()));

                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SharedDataManager.getInstance().activeApplication.preferredApplicationPickupDate = sdf1.format(myCalendar.getTime());
            }

        };


        appointmentField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(FixAppointmentActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    void setupDatePickerForAppointmentTime() {

        final EditText timeField = (EditText) findViewById(R.id.appointmentTimeField);

        timeField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(FixAppointmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String hour = String.valueOf(selectedHour);
                        if(selectedHour<10){
                            hour = "0"+hour;
                        }

                        String minute = String.valueOf(selectedMinute);
                        if(selectedMinute<10){
                            minute = "0"+minute;
                        }

                        SharedDataManager.getInstance().activeApplication.preferredApplicationPickupTime = hour+":"+minute+":00";
                        timeField.setText(SharedDataManager.getInstance().activeApplication.preferredApplicationPickupTime);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
