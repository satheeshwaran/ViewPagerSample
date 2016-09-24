package com.oozmakappa.oyeloans.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sankarnarayanan on 24/09/16.
 */
public class LoanDetailsInfo extends SuccessModel {

    public JSONObject getResponse() {
        return response;
    }

    public void setResponse(JSONObject response) {
        this.response = response;
    }

    JSONObject response;


    public static LoanDetailsInfo LoanDetailsModelFromJSONObject(JSONObject object){

        LoanDetailsInfo sModel = new LoanDetailsInfo();
        try {
            sModel.setDescription(object.getString("description"));
            sModel.setRequestId(object.getString("request_id"));
            sModel.setServiceName(object.getString("service_name"));
            sModel.setStatus(object.getString("status"));
            sModel.setResponse(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  sModel;

    }

}
