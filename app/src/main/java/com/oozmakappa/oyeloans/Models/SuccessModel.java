package com.oozmakappa.oyeloans.Models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sankarnarayanan on 13/09/16.
 */
public class SuccessModel {

    public String getStatus() {
        return status;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getDescription() {
        return description;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    String status;
    String serviceName;
    String description;
    String requestId;
    public String response;

    public static SuccessModel sucessModelFromJSONObject(JSONObject object){

        SuccessModel sModel = new SuccessModel();
        try {
            sModel.setDescription(object.getString("description"));
            sModel.setRequestId(object.getString("request_id"));
            sModel.setServiceName(object.getString("service_name"));
            sModel.setStatus(object.getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  sModel;

    }

}
