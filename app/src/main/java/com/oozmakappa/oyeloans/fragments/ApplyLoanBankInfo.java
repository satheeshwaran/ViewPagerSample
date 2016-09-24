package com.oozmakappa.oyeloans.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.oozmakappa.oyeloans.ApplyLoanSecondActivity;
import com.oozmakappa.oyeloans.ApplyLoanThirdActivity;
import com.oozmakappa.oyeloans.DataExtraction.AppController;
import com.oozmakappa.oyeloans.LoanReasonActivity;
import com.oozmakappa.oyeloans.Models.Application;
import com.oozmakappa.oyeloans.Models.BankInfo;
import com.oozmakappa.oyeloans.Models.LoanUser;
import com.oozmakappa.oyeloans.Models.SuccessModel;
import com.oozmakappa.oyeloans.R;
import com.oozmakappa.oyeloans.helper.WebServiceCallHelper;
import com.oozmakappa.oyeloans.utils.SharedDataManager;
import com.oozmakappa.oyeloans.utils.Utils;

import java.util.regex.Pattern;

/**
 * Created by sankarnarayanan on 14/09/16.
 */
public class ApplyLoanBankInfo extends Fragment {

    AutoCompleteTextView accountNumber;
    AutoCompleteTextView ifscCode;
    AutoCompleteTextView bankAddr1;
    AutoCompleteTextView bankAddr2;

    String fieldError = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_bankinfo_details, null);

        accountNumber = (AutoCompleteTextView) v.findViewById(R.id.account_number);
        ifscCode = (AutoCompleteTextView) v.findViewById(R.id.ifsc_code);
        bankAddr1 = (AutoCompleteTextView) v.findViewById(R.id.bank_addr_1);
        bankAddr2 = (AutoCompleteTextView) v.findViewById(R.id.bank_addr_2);

        Application application = SharedDataManager.getInstance().activeApplication;
        if (application.bankInfoObject != null) {
            accountNumber.setText(application.bankInfoObject.accountNumber);
            ifscCode.setText(application.bankInfoObject.ifscCode);
            bankAddr1.setText(application.bankInfoObject.bankAddress1);
            bankAddr2.setText(application.bankInfoObject.bankAddress2);
        }

        Button proceedButton = (Button) v.findViewById(R.id.profileProceedButtonBankInfo);
        proceedButton.setOnClickListener(buttonClickListener);
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        Tracker t = ((AppController) getActivity().getApplication()).getDefaultTracker();
        t.setScreenName("Loan application - Enter bank info screen");
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


    private boolean performValidations() {
        if (accountNumber.getText().length() <= 0 || ifscCode.getText().length() <= 0 || bankAddr1.getText().length() <= 0 || bankAddr2.getText().length() <= 0) {
            fieldError = "None of the fields can be empty, Please fill up all";
            return false;
        }
        if (!Pattern.compile("[A-Z|a-z]{4}[0][\\d]{6}$").matcher(ifscCode.getText().toString()).matches()) {
            fieldError = "Invalid IFSC Code";
            return false;
        }
//^[A-Z]{4}[0]\\w{6}$
        return true;
    }


    private void populateGivenData(){
        SharedDataManager.getInstance().activeApplication.bankInfoObject = new BankInfo();
        SharedDataManager.getInstance().activeApplication.bankInfoObject.accountNumber = accountNumber.getText().toString();
        SharedDataManager.getInstance().activeApplication.bankInfoObject.ifscCode = ifscCode.getText().toString();
        SharedDataManager.getInstance().activeApplication.bankInfoObject.bankAddress1 = bankAddr1.getText().toString();
        SharedDataManager.getInstance().activeApplication.bankInfoObject.bankAddress2 = bankAddr2.getText().toString();

    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {

        public void onClick(View v) {

            if (performValidations()) {
                populateGivenData();
                makeBankInfoService();

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



    private void makeBankInfoService(){
        Utils.showLoading(getActivity(),"Saving Bank Information...");

        WebServiceCallHelper webServiceHelper = new WebServiceCallHelper(new WebServiceCallHelper.OnWebServiceRequestCompletedListener() {
            @Override
            public void onRequestCompleted(SuccessModel model, String errorMessage) {
                com.oozmakappa.oyeloans.utils.Utils.removeLoading();
                if (errorMessage == null && model != null && model.getStatus().equals("success")) {
                    ((ApplyLoanSecondActivity)getActivity()).setCurrentItem(2, true);
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
        webServiceHelper.makeBankInfoServiceCall(SharedDataManager.getInstance().activeApplication);
    }
}
