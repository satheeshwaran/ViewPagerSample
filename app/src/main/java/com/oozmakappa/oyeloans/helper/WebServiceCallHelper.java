package com.oozmakappa.oyeloans.helper;

import android.util.Log;
import com.android.volley.Request;
import com.oozmakappa.oyeloans.DataExtraction.AppConstants;
import com.oozmakappa.oyeloans.Models.Application;
import com.oozmakappa.oyeloans.Models.LoanUser;
import java.util.HashMap;
import com.oozmakappa.oyeloans.Models.SuccessModel;
import com.oozmakappa.oyeloans.constants.Jsonconstants;
import com.oozmakappa.oyeloans.utils.FacebookHelperUtilsCallback;
import com.oozmakappa.oyeloans.utils.SharedDataManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sankarnarayanan on 13/09/16.
 */
public class WebServiceCallHelper implements VolleyRequestHelper.OnRequestCompletedListener {


    public interface RequestNameKeys{
        String FB_REQUEST_KEY = "fb";
        String VALIDATE_REFERRAL_KEY = "validateReferral";
        String NEW_APPLICATION_REQUEST_KEY = "newapplication";
    }

    private JSONObject authObject = new JSONObject();

    private JSONObject requestInfo = new JSONObject();

    public WebServiceCallHelper() {
        try {
            this.authObject.putOpt(Jsonconstants.OL_PASSWORD_KEY, Jsonconstants.OL_PASSWORD_VALUE);
            this.authObject.putOpt(Jsonconstants.OL_USERNAME_KEY, Jsonconstants.OL_USERNAME_VALUE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    VolleyRequestHelper vHelper;

    private OnWebServiceRequestCompletedListener completionHandler;


    public interface OnWebServiceRequestCompletedListener {

        /**
         * Called when the JSON Object request has been completed.
         *
         * @param model  the String refers the request name
         * @param errorMessage the String refers the error message when request failed to
         *                     get the response
         */
        public void onRequestCompleted(SuccessModel model, String errorMessage);

    }


    public WebServiceCallHelper(OnWebServiceRequestCompletedListener handler){
        completionHandler = handler;
    }

    public void makeNewApplicationServiceCall(Application applicationObject) {
        // Construct the request
        try{
            JSONObject requestMap = new JSONObject();
            requestMap.putOpt(Jsonconstants.OL_NA_NAME_KEY, (applicationObject.firstName.concat(" ").concat(applicationObject.lastName)));
            requestMap.putOpt(Jsonconstants.OL_NA_EMAIL_KEY, applicationObject.emailID);
            requestMap.putOpt(Jsonconstants.OL_NA_MOBILE_NO_KEY, applicationObject.mobile);
            requestMap.putOpt(Jsonconstants.OL_NA_DOB_KEY, applicationObject.dob);
            requestMap.putOpt(Jsonconstants.OL_NA_GENDER_KEY, applicationObject.gender);
            requestMap.putOpt(Jsonconstants.OL_NA_PAN_KEY, applicationObject.panNumber);
            requestMap.putOpt(Jsonconstants.OL_NA_AADHAR_KEY, applicationObject.aadharNumber);
            requestMap.putOpt(Jsonconstants.OL_NA_ADDRESS1_KEY, applicationObject.buildingNumber.concat(" ").concat(applicationObject.buildingName));
            requestMap.putOpt(Jsonconstants.OL_NA_ADDRESS2_KEY, applicationObject.streetName);
            requestMap.putOpt(Jsonconstants.OL_NA_PINCODE_KEY, applicationObject.pinCode);
            requestMap.putOpt(Jsonconstants.OL_NA_CITY_KEY, applicationObject.city);
            requestMap.putOpt(Jsonconstants.OL_NA_APPLICATIONID_KEY, applicationObject.applicationID);
            requestMap.putOpt(Jsonconstants.OL_NA_LOAN_AMOUNT_KEY, applicationObject.loanAmount);
            requestMap.putOpt(Jsonconstants.OL_NA_LOAN_DURATION_KEY, applicationObject.loanDuration);
            requestMap.putOpt(Jsonconstants.OL_NA_FIRST_PAYDATE, applicationObject.firstPayDate);

            // Construct the auth object for the request
            requestMap.putOpt(Jsonconstants.OL_AUTH_KEY, this.authObject);

            // Construct the request info object for the request
            this.requestInfo.putOpt(Jsonconstants.OL_SERVICENAME_KEY, "NewApplication");
            this.requestInfo.putOpt(Jsonconstants.OL_SERVICECODE_KEY, "GAI002");
            this.requestInfo.putOpt(Jsonconstants.OL_REQUESTID_KEY, "1285");
            requestMap.putOpt(Jsonconstants.OL_REQUESTINFO_KEY, requestInfo);

            vHelper = new VolleyRequestHelper(this);
            String url = Jsonconstants.OL_BASE_URL.concat(Jsonconstants.OL_NEWAPPLICATION_SERVICE_KEY);
            // Post the device data
            final HashMap<Object, Object> requestParams = new HashMap<>();

            // Priority
            requestParams.put(VolleyRequestHelper.VolleyRequestConstants.HTTP_PRIORITY, Request.Priority.HIGH);

            // Headers
            final HashMap<String, String> headers = new HashMap<>();
            headers.put(VolleyRequestHelper.VolleyRequestConstants.HTTP_CONTENT_TYPE, AppConstants.CONTENT_TYPE_JSON);
            requestParams.put(VolleyRequestHelper.VolleyRequestConstants.HTTP_HEADERS, headers);

            // Body
            final String content = requestMap.toString();
            requestParams.put(VolleyRequestHelper.VolleyRequestConstants.HTTP_BODY_CONTENT, content.getBytes());

            // Content Type
            requestParams.put(VolleyRequestHelper.VolleyRequestConstants.HTTP_CONTENT_TYPE, AppConstants.CONTENT_TYPE_JSON);

            HttpsTrustManager.allowAllSSL();

            vHelper.requestString(RequestNameKeys.NEW_APPLICATION_REQUEST_KEY, url, requestParams,Request.Method.POST,true);



        }catch(Exception e){
            Log.v("json exception", e.getLocalizedMessage());
        }

    }

    public void makeFacebookServiceCall(LoanUser userObject){
        //Construct request
        try {
            JSONObject requestMap = new JSONObject();
            requestMap.putOpt(Jsonconstants.OL_FB_NAME_KEY, (userObject.firstName.concat(" ").concat(userObject.lastName)));
            requestMap.putOpt(Jsonconstants.OL_FB_GENDER_KEY, userObject.gender);
            requestMap.putOpt(Jsonconstants.OL_FB_HOMETOWN_KEY, userObject.city);
            requestMap.putOpt(Jsonconstants.OL_FB_EMAIL_KEY, userObject.emailID);
            requestMap.putOpt(Jsonconstants.OL_FB_DOB_KEY,userObject.DOB);
            requestMap.putOpt(Jsonconstants.OL_FB_WORK_ID_KEY, userObject.fbUserID);
            requestMap.putOpt(Jsonconstants.OL_FB_RELSTATUS_KEY, userObject.relationshipStatus);
            requestMap.putOpt(Jsonconstants.OL_FB_WORK_HISTORY_LOCATION_KEY, userObject.city);

            JSONArray friendList = new JSONArray();
            for (int i=0; i< userObject.friendList.length(); i++) {
                JSONObject currObject = userObject.friendList.getJSONObject(i);
                friendList.put(currObject.get("name"));
            }


            requestMap.putOpt(Jsonconstants.OL_FB_FRIENDLIST_KEY, friendList);

            JSONObject historyEdu = new JSONObject();
            JSONArray education = new JSONArray();
            for (int i=0; i< userObject.education.length(); i++){
                JSONObject currObject = userObject.education.getJSONObject(i);
                JSONObject constructedObject = new JSONObject();
                constructedObject.put(Jsonconstants.OL_FB_EDUCATION_SCHOOL_KEY,currObject.getJSONObject("school").get("name"));
                constructedObject.put(Jsonconstants.OL_FB_WORK_ID_KEY,currObject.get("id"));
                constructedObject.put(Jsonconstants.OL_FB_EDUCATION_TYPE_KEY,currObject.get("type"));
                education.put(constructedObject);
            }
            historyEdu.put(Jsonconstants.OL_FB_HISTORY_KEY, education);
            requestMap.putOpt(Jsonconstants.OL_FB_EDUCATION_KEY, historyEdu);
            JSONObject historyWork = new JSONObject();
            JSONArray work = new JSONArray();
            for (int i=0; i< userObject.employment.length(); i++) {
                JSONObject currObject = userObject.employment.getJSONObject(i);
                JSONObject constructedObject = new JSONObject();
                constructedObject.put(Jsonconstants.OL_FB_WORK_HISTORY_POSITION_KEY,currObject.getJSONObject("position").get("name"));
                constructedObject.put(Jsonconstants.OL_FB_WORK_HISTORY_LOCATION_KEY,currObject.getJSONObject("location").get("name"));
                constructedObject.put(Jsonconstants.OL_FB_WORK_EMPLOYER_ID_KEY,currObject.getJSONObject("employer").get("name"));
                constructedObject.put(Jsonconstants.OL_FB_WORK_ID_KEY,currObject.get("id"));
                work.put(constructedObject);
            }
            historyWork.put(Jsonconstants.OL_FB_HISTORY_KEY, work);
            requestMap.putOpt(Jsonconstants.OL_FB_WORK_KEY, historyWork);
            JSONObject authObject = new JSONObject();
            authObject.putOpt(Jsonconstants.OL_USERNAME_KEY,"fb");
            authObject.putOpt(Jsonconstants.OL_PASSWORD_KEY, "fb@123");
            requestMap.putOpt(Jsonconstants.OL_AUTH_KEY, authObject);
            JSONObject requestInfo = new JSONObject();
            requestInfo.putOpt(Jsonconstants.OL_SERVICENAME_KEY, "Fb");
            requestInfo.putOpt(Jsonconstants.OL_SERVICECODE_KEY, "2000");
            requestInfo.putOpt(Jsonconstants.OL_REQUESTID_KEY,"12345678");
            requestMap.putOpt(Jsonconstants.OL_REQUESTINFO_KEY, requestInfo);
            vHelper = new VolleyRequestHelper(this);
            String url = Jsonconstants.OL_BASE_URL.concat(Jsonconstants.OL_FBSERVICE_KEY);
            // Post the device data
            final HashMap<Object, Object> requestParams = new HashMap<>();

            // Priority
            requestParams.put(VolleyRequestHelper.VolleyRequestConstants.HTTP_PRIORITY, Request.Priority.HIGH);

            // Headers
            final HashMap<String, String> headers = new HashMap<>();
            headers.put(VolleyRequestHelper.VolleyRequestConstants.HTTP_CONTENT_TYPE, AppConstants.CONTENT_TYPE_JSON);
            requestParams.put(VolleyRequestHelper.VolleyRequestConstants.HTTP_HEADERS, headers);

            // Body
            final String content = requestMap.toString();
            requestParams.put(VolleyRequestHelper.VolleyRequestConstants.HTTP_BODY_CONTENT, content.getBytes());

            // Content Type
            requestParams.put(VolleyRequestHelper.VolleyRequestConstants.HTTP_CONTENT_TYPE, AppConstants.CONTENT_TYPE_JSON);

            HttpsTrustManager.allowAllSSL();

            vHelper.requestString(RequestNameKeys.FB_REQUEST_KEY, url, requestParams,Request.Method.POST,true);

        }catch(Exception e){
            Log.v("json exception", e.getLocalizedMessage());
        }


    }


    public void validateReferral(String code) {

        try {
            JSONObject requestMap = new JSONObject();
            requestMap.putOpt(Jsonconstants.OL_VALIDATE_REFERALCODE_EMAIL, SharedDataManager.getInstance().userObject.emailID);
            requestMap.putOpt(Jsonconstants.OL_REFERRALCODE_KEY, code);
            requestMap.putOpt(Jsonconstants.OL_APPID_KEY, 1001);
            JSONObject authObject = new JSONObject();
            authObject.putOpt(Jsonconstants.OL_USERNAME_KEY,"intest");
            authObject.putOpt(Jsonconstants.OL_PASSWORD_KEY, "intest!23");
            requestMap.putOpt(Jsonconstants.OL_AUTH_KEY, authObject);
            JSONObject requestInfo = new JSONObject();
            requestInfo.putOpt(Jsonconstants.OL_SERVICENAME_KEY, "ValidateReferralCode");
            requestInfo.putOpt(Jsonconstants.OL_SERVICECODE_KEY, "1001");
            requestInfo.putOpt(Jsonconstants.OL_REQUESTID_KEY,"12345678");
            requestMap.putOpt(Jsonconstants.OL_REQUESTINFO_KEY, requestInfo);

            vHelper = new VolleyRequestHelper(this);
            String url = Jsonconstants.OL_BASE_URL.concat(Jsonconstants.OL_VALIDATEREFERRAL_KEY);
            // Post the device data
            final HashMap<Object, Object> requestParams = new HashMap<>();

            // Priority
            requestParams.put(VolleyRequestHelper.VolleyRequestConstants.HTTP_PRIORITY, Request.Priority.HIGH);

            // Headers
            final HashMap<String, String> headers = new HashMap<>();
            headers.put(VolleyRequestHelper.VolleyRequestConstants.HTTP_CONTENT_TYPE, AppConstants.CONTENT_TYPE_JSON);
            requestParams.put(VolleyRequestHelper.VolleyRequestConstants.HTTP_HEADERS, headers);

            // Body
            final String content = requestMap.toString();
            requestParams.put(VolleyRequestHelper.VolleyRequestConstants.HTTP_BODY_CONTENT, content.getBytes());

            // Content Type
            requestParams.put(VolleyRequestHelper.VolleyRequestConstants.HTTP_CONTENT_TYPE, AppConstants.CONTENT_TYPE_JSON);

            HttpsTrustManager.allowAllSSL();

            vHelper.requestString(RequestNameKeys.VALIDATE_REFERRAL_KEY, url, requestParams,Request.Method.POST,true);


        }catch (Exception e){
            Log.v("json exception", e.getLocalizedMessage());
        }

    }



    //Call backs

    /**
     * Called when the JSON Object request has been completed.
     *
     * @param requestName  the String refers the request name
     * @param status       the status of the request either success or failure
     * @param response     the JSON Object returns from WebService Response. It may
     *                     be null if request failed.
     * @param errorMessage the String refers the error message when request failed to
     *                     get the response
     */
    public void onRequestCompleted(String requestName, boolean status,
                                   JSONObject response, String errorMessage){

        Log.v("response",response.toString());

    }

    /**
     * Called when the JSON Array request has been completed.
     *
     * @param requestName  the String refers the request name
     * @param status       the status of the request either success or failure
     * @param response     the JSON Array returns from WebService Response. It may be
     *                     null if request failed.
     * @param errorMessage the String refers the error message when request failed to
     *                     get the response
     */
    public void onRequestCompleted(String requestName, boolean status,
                                   JSONArray response, String errorMessage){

        Log.v("response",response.toString());

    }

    /**
     * Called when the String request has been completed.
     *
     * @param requestName  the String refers the request name
     * @param status       the status of the request either success or failure
     * @param response     the String response returns from the Webservice. It may be
     *                     null if request failed.
     * @param errorMessage the String refers the error message when request failed to
     *                     get the response
     */
    public void onRequestCompleted(String requestName, boolean status,
                                   String response, String errorMessage){
        try {

            if (errorMessage == null && response != null && (requestName.equals(RequestNameKeys.FB_REQUEST_KEY) || requestName.equals(RequestNameKeys.VALIDATE_REFERRAL_KEY))) {
                JSONObject jsonObject = new JSONObject(response);
                SuccessModel sModel = SuccessModel.sucessModelFromJSONObject(jsonObject);
                completionHandler.onRequestCompleted(sModel,null);
                Log.v("response", response);
            } else {

            }
        }catch (Exception e){

        }

    }


}
