package com.oozmakappa.oyeloans.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sankarnarayanan on 23/09/16.
 */
public class LoanApplicationInfo extends SuccessModel {

    public JSONArray getApplicationHistory() {
        return applicationHistory;
    }

    public JSONArray getLoanHistory() {
        return loanHistory;
    }

    JSONArray applicationHistory;

    public void setLoanHistory(JSONArray loanHistory) {
        this.loanHistory = loanHistory;
    }

    public void setApplicationHistory(JSONArray applicationHistory) {
        this.applicationHistory = applicationHistory;
    }

    JSONArray loanHistory;


    public static LoanApplicationInfo LoanApplicationModelFromJSONObject(JSONObject object){

        LoanApplicationInfo sModel = new LoanApplicationInfo();
        try {
            sModel.setDescription(object.getString("description"));
            sModel.setRequestId(object.getString("request_id"));
            sModel.setServiceName(object.getString("service_name"));
            sModel.setStatus(object.getString("status"));
            sModel.setApplicationHistory(object.getJSONArray("application_status_history"));
            sModel.setLoanHistory(object.getJSONArray("loan_status_history"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  sModel;

    }


}
