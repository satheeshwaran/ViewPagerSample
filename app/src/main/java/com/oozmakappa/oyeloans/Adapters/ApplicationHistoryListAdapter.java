package com.oozmakappa.oyeloans.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oozmakappa.oyeloans.ApplicationCompletedActivity;
import com.oozmakappa.oyeloans.ApplicationHistoryActivity;
import com.oozmakappa.oyeloans.ApplyLoanFirstActivity;
import com.oozmakappa.oyeloans.ApplyLoanSecondActivity;
import com.oozmakappa.oyeloans.ApplyLoanThirdActivity;
import com.oozmakappa.oyeloans.FBLoginActivty;
import com.oozmakappa.oyeloans.Models.Application;
import com.oozmakappa.oyeloans.Models.EmploymentDetailsModel;
import com.oozmakappa.oyeloans.Models.LoanUser;
import com.oozmakappa.oyeloans.Models.PersonalDetailsModel;
import com.oozmakappa.oyeloans.Models.SuccessModel;
import com.oozmakappa.oyeloans.R;
import com.oozmakappa.oyeloans.helper.WebServiceCallHelper;
import com.oozmakappa.oyeloans.utils.SharedDataManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by sankarnarayanan on 25/09/16.
 */
public class ApplicationHistoryListAdapter extends BaseAdapter implements View.OnClickListener {
    JSONArray result;
    Context context;
    private static LayoutInflater inflater = null;

    public ApplicationHistoryListAdapter(AppCompatActivity mainActivity, JSONArray prgmNameList) {
        // TODO Auto-generated constructor stub
        result = prgmNameList;
        context = mainActivity;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        TextView loanAmount;
        TextView status;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        try {
            JSONObject currObj = result.getJSONObject(position);
            Holder holder = new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.list_item_application_history, null);
            holder.loanAmount = (TextView) rowView.findViewById(R.id.loanAmountValue);
            holder.loanAmount.setText(currObj.getString("loan_amount"));
            holder.status = (TextView) rowView.findViewById(R.id.statusValue);
            holder.status.setText(currObj.getString("app_status"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                View container = (View) rowView.findViewById(R.id.indicatorView);
                container.setBackgroundColor(context.getResources().getColor(R.color.green_500));
            }
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //Toast.makeText(context, "You Clicked " + result[position], Toast.LENGTH_LONG).show();
                }
            });
            rowView.setOnClickListener(new OnItemClickListener( position ));
            return rowView;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

    /*********
     * Called when Item click in ListView
     ************/
    private class OnItemClickListener implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
            try {
                JSONObject currObj = result.getJSONObject(mPosition);
                String appStatus = currObj.getString("app_status");
                Application appModel = new Application();
                appModel.applicationID = currObj.getString("app_id");
                appModel.loanAmount = currObj.getString("loan_amount");
                appModel.applicationState = appStatus;
                appModel.loanDuration = currObj.getString("application_start_time");
                SharedDataManager.getInstance().activeApplication = appModel;
                switch (appStatus) {
                    case "Personal Information Complete":
                        continueLoanFromPersonalDetails();
                        break;
                    case "Stage 1 UW Approved":
                        continueLoanFromEmploymentDetails(false);
                        break;
                    case "PAN / Bank Statement Uploaded":
                        continueLoanFromEmploymentDetails(true);
                        break;
                    case "Esign complete":
                        Intent thanksScreen = new Intent(context, ApplicationCompletedActivity.class);
                        context.startActivity(thanksScreen);
                        break;
                    case "Application Received. Stage 2 UW In Process":
                        Intent thanksScreenWithoutSchedule = new Intent(context, ApplicationCompletedActivity.class);
                        context.startActivity(thanksScreenWithoutSchedule);
                        break;
                    case "Stage 2 UW Approved":
                        SharedDataManager.getInstance().loanApproved = true;
                        SharedDataManager.getInstance().activeApplication.loanDuration = "12";
                        Date today = new Date();
                        today.setHours(0);  // Start date
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar c = Calendar.getInstance();
                        c.setTime(today);
                        c.add(Calendar.DATE, 2);  // number of days to add
                        String date = sdf.format(c.getTime());
                        SharedDataManager.getInstance().activeApplication.firstPayDate = date;
                        Intent thanksScreenApproved = new Intent(context, ApplicationCompletedActivity.class);
                        context.startActivity(thanksScreenApproved);
                        break;
                    default:
                        break;
                }


                Log.v("Item Clicked", Integer.toString(mPosition));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        private void setPersonalDetails(JSONObject object) {
            try {
                LoanUser user = SharedDataManager.getInstance().userObject;
                user.firstName = object.getString("full_name");
                user.lastName = object.getString("full_name");
                user.mobileNumber = object.getString("mobile_no");
                user.DOB = object.getString("dob");
                user.doorNumber = object.getString("address1");
                user.street = object.getString("address2");
                user.locaility = object.getString("address2");
                user.city = object.getString("city");
                user.state = object.getString("city");
                user.PINCode = object.getString("pincode");
                user.aadharNumber = object.getString("aadhar");
                user.PANNumber = object.getString("CMNPS5987B");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void continueLoanFromPersonalDetails() {
            WebServiceCallHelper webServiceHelper = new WebServiceCallHelper(new WebServiceCallHelper.OnWebServiceRequestCompletedListener() {
                @Override
                public void onRequestCompleted(SuccessModel model, String errorMessage) {
                    if (model.getStatus().equals("success")) {
                        setPersonalDetails(((PersonalDetailsModel) model).getPersonalDetails());
                        SharedDataManager.getInstance().activeApplication.loanUserObject = SharedDataManager.getInstance().userObject;
                        Intent applyLoanFirstActivity = new Intent(context, ApplyLoanFirstActivity.class);
                        context.startActivity(applyLoanFirstActivity);
                    }
                }
            });
            webServiceHelper.getPersonalInfoService(SharedDataManager.getInstance().userObject.emailID);
        }

        private void continueLoanFromEmploymentDetails(final Boolean otpFlow) {
            WebServiceCallHelper webServiceHelper = new WebServiceCallHelper(new WebServiceCallHelper.OnWebServiceRequestCompletedListener() {
                @Override
                public void onRequestCompleted(SuccessModel model, String errorMessage) {
                    if (model.getStatus().equals("success")) {
                        setPersonalDetails(((PersonalDetailsModel) model).getPersonalDetails());
                        getEmploymentInfo(otpFlow);
                    }
                }
            });
            webServiceHelper.getPersonalInfoService(SharedDataManager.getInstance().userObject.emailID);
        }


        private void getEmploymentInfo(final Boolean otpFlow){
            WebServiceCallHelper webServiceHelper = new WebServiceCallHelper(new WebServiceCallHelper.OnWebServiceRequestCompletedListener() {
                @Override
                public void onRequestCompleted(SuccessModel model, String errorMessage) {
                    if (model.getStatus().equals("success")) {
                        setEmploymentDetails(((EmploymentDetailsModel) model).getEmploymentDetails());
                        SharedDataManager.getInstance().activeApplication.loanUserObject = SharedDataManager.getInstance().userObject;
                        if (otpFlow == false) {
                            Intent applyLoanSecondActivity = new Intent(context, ApplyLoanSecondActivity.class);
                            context.startActivity(applyLoanSecondActivity);
                        }else{
                            continueLoanFromOtp();
                        }
                    }
                }
            });
            webServiceHelper.getEmploymentInfoService(SharedDataManager.getInstance().userObject.emailID);
        }


        private void setEmploymentDetails(JSONObject object){
            try {
                LoanUser user = SharedDataManager.getInstance().userObject;
                user.highestEducationPlace = object.getString("last_institution_studied");
                user.highestEducation = object.getString("highest_degree");
                //Hard coded since this is not received from service
                user.highestEducationYear = "2001";
                user.workPlace = object.getString("employer_name");
                user.workTitle = object.getString("employer_name");
                user.workStartDate = object.getString("employer_name");
                user.workPhone = object.getString("employer_phone");
                user.monthlyIncome = object.getString("gross_monthly_income");
                user.workStatus = object.getString("employment_status");
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private void continueLoanFromOtp(){
            Intent applicationPageThree = new Intent(context,ApplyLoanThirdActivity.class);
            context.startActivity(applicationPageThree);

        }

    }
}
