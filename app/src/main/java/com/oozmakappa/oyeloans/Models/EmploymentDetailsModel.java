package com.oozmakappa.oyeloans.Models;

import org.json.JSONObject;

/**
 * Created by sankarnarayanan on 25/09/16.
 */
public class EmploymentDetailsModel extends SuccessModel {

    public JSONObject getEmploymentDetails() {
        return employmentDetails;
    }

    public void setEmploymentDetails(JSONObject employmentDetails) {
        this.employmentDetails = employmentDetails;
    }

    JSONObject employmentDetails;

    public static EmploymentDetailsModel employmentDetailsModel(JSONObject object) {
        try {
            EmploymentDetailsModel sModel = new EmploymentDetailsModel();
            sModel.setDescription(object.getString("description"));
            sModel.setRequestId(object.getString("request_id"));
            sModel.setServiceName(object.getString("service_name"));
            sModel.setStatus(object.getString("status"));
            sModel.setEmploymentDetails(object.getJSONArray("emp_info").getJSONObject(0));
            return sModel;
        }catch (Exception e){
            e.printStackTrace();
        }
        return new EmploymentDetailsModel();
    }

}