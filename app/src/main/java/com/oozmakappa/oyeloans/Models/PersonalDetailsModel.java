package com.oozmakappa.oyeloans.Models;

import org.json.JSONObject;

/**
 * Created by sankarnarayanan on 25/09/16.
 */
public class PersonalDetailsModel extends SuccessModel {

    public JSONObject getPersonalDetails() {
        return personalDetails;
    }

    public void setPersonalDetails(JSONObject personalDetails) {
        this.personalDetails = personalDetails;
    }

    JSONObject personalDetails;

    public static PersonalDetailsModel personalDetailsModel(JSONObject object) {
        try {
            PersonalDetailsModel sModel = new PersonalDetailsModel();
            sModel.setDescription(object.getString("description"));
            sModel.setRequestId(object.getString("request_id"));
            sModel.setServiceName(object.getString("service_name"));
            sModel.setStatus(object.getString("status"));
            sModel.setPersonalDetails(object.getJSONObject("personaldetails"));
            return sModel;
        }catch (Exception e){
            e.printStackTrace();
        }
        return new PersonalDetailsModel();
    }

}
