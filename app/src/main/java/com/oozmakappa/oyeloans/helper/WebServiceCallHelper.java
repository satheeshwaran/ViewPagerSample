package com.oozmakappa.oyeloans.helper;

import android.util.Log;

import com.android.volley.Request;
import com.oozmakappa.oyeloans.DataExtraction.AppConstants;
import com.oozmakappa.oyeloans.Models.Application;
import com.oozmakappa.oyeloans.Models.BankInfo;
import com.oozmakappa.oyeloans.Models.Loan;
import com.oozmakappa.oyeloans.Models.LoanApplicationInfo;
import com.oozmakappa.oyeloans.Models.LoanDetailsInfo;
import com.oozmakappa.oyeloans.Models.LoanSummaryModel;
import com.oozmakappa.oyeloans.Models.LoanUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

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


    public interface RequestNameKeys {
        String FB_REQUEST_KEY = "fb";
        String VALIDATE_REFERRAL_KEY = "validateReferral";
        String NEW_APPLICATION_REQUEST_KEY = "newapplication";
        String EMPLOYMENTINFO_REQUEST_KEY = "employmentinfo";
        String LOAN_HISTORY_KEY = "loanApplicationHistory";
        String LOAN_INFO_KEY = "loanInfo";
        String UPLOAD_PHONE_DATA_KEY = "uploadDeviceData";
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
         * @param model        the String refers the request name
         * @param errorMessage the String refers the error message when request failed to
         *                     get the response
         */
        public void onRequestCompleted(SuccessModel model, String errorMessage);

    }


    public WebServiceCallHelper(OnWebServiceRequestCompletedListener handler) {
        completionHandler = handler;
    }

    public void makeNewApplicationServiceCall(Application applicationObject,String deviceID) {
        // Construct the request
        try {
            JSONObject requestMap = requestObjectWithDetails("NewApplication", "GAI002", "1285");

            Random ran = new Random();
            int applicationID = ran.nextInt(1000);
            SharedDataManager.getInstance().activeApplication.applicationID = String.valueOf(applicationID);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            Calendar c = Calendar.getInstance(); // starts with today's date and time
            c.add(Calendar.DAY_OF_YEAR, 2);  // advances day by 2
            Date date = c.getTime();
            String firstPayDate = sdf1.format(date);

            requestMap.putOpt(Jsonconstants.OL_NA_NAME_KEY, (applicationObject.loanUserObject.firstName.concat(" ").concat(applicationObject.loanUserObject.lastName)));
            requestMap.putOpt(Jsonconstants.OL_NA_EMAIL_KEY, applicationObject.loanUserObject.emailID);
            requestMap.putOpt(Jsonconstants.OL_NA_MOBILE_NO_KEY, applicationObject.loanUserObject.mobileNumber);
            String dob = applicationObject.loanUserObject.DOB;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date formatedDate = sdf.parse(dob);
                dob = sdf1.format(formatedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            requestMap.putOpt(Jsonconstants.OL_NA_DOB_KEY, dob);
            requestMap.putOpt(Jsonconstants.OL_NA_GENDER_KEY, applicationObject.loanUserObject.gender);
            requestMap.putOpt(Jsonconstants.OL_NA_PAN_KEY, applicationObject.loanUserObject.PANNumber);
            requestMap.putOpt(Jsonconstants.OL_NA_AADHAR_KEY, applicationObject.loanUserObject.aadharNumber);
            requestMap.putOpt(Jsonconstants.OL_NA_ADDRESS1_KEY, applicationObject.loanUserObject.doorNumber);
            requestMap.putOpt(Jsonconstants.OL_NA_ADDRESS2_KEY, applicationObject.loanUserObject.street);
            requestMap.put(Jsonconstants.OL_NA_PINCODE_KEY,Integer.parseInt(applicationObject.loanUserObject.PINCode));
            requestMap.putOpt(Jsonconstants.OL_NA_CITY_KEY, applicationObject.loanUserObject.city);
            requestMap.put(Jsonconstants.OL_NA_APPLICATIONID_KEY,applicationID);
            requestMap.put(Jsonconstants.OL_NA_LOAN_AMOUNT_KEY, Integer.parseInt(applicationObject.loanAmount));
            requestMap.put(Jsonconstants.OL_NA_LOAN_DURATION_KEY, 12);
            requestMap.putOpt(Jsonconstants.OL_NA_FIRST_PAYDATE, firstPayDate);
            requestMap.putOpt(Jsonconstants.OL_DEVICE_ID_KEY, deviceID);

            // Construct the auth object for the request
            //requestMap.putOpt(Jsonconstants.OL_AUTH_KEY, this.authObject);

            // Construct the request info object for the request
            /*this.requestInfo.putOpt(Jsonconstants.OL_SERVICENAME_KEY, "NewApplication");
            this.requestInfo.putOpt(Jsonconstants.OL_SERVICECODE_KEY, "GAI002");
            this.requestInfo.putOpt(Jsonconstants.OL_REQUESTID_KEY, "1285");
            requestMap.putOpt(Jsonconstants.OL_REQUESTINFO_KEY, this.requestInfo);*/

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

            vHelper.requestString(RequestNameKeys.NEW_APPLICATION_REQUEST_KEY, url, requestParams, Request.Method.POST, true);


        } catch (Exception e) {
            Log.v("json exception", e.getLocalizedMessage());
        }

    }

    public void makeEmploymentInfoServiceCall(Application applicationObject) {
        //Construct request
        try {
            JSONObject requestMap = requestObjectWithDetails("AddEmploymentInfo","GAI003","1285");
            requestMap.put(Jsonconstants.OL_EI_APPLICATIONID_KEY, Long.parseLong(applicationObject.applicationID));
            requestMap.putOpt(Jsonconstants.OL_EI_DEGREE_KEY, applicationObject.loanUserObject.highestEducation);
            requestMap.putOpt(Jsonconstants.OL_EI_INSTITUTION_KEY, applicationObject.loanUserObject.highestEducationPlace);

            requestMap.putOpt(Jsonconstants.OL_EI_STATUS_KEY, applicationObject.loanUserObject.workStatus);
            requestMap.putOpt(Jsonconstants.OL_EI_EMPLOYER_KEY, applicationObject.loanUserObject.workPlace);
            requestMap.putOpt(Jsonconstants.OL_EI_PHONE_KEY, applicationObject.loanUserObject.workPhone);
            requestMap.put(Jsonconstants.OL_EI_INCOME_KEY,Long.parseLong(applicationObject.loanUserObject.monthlyIncome));



            /*if (applicationObject.loanUserObject.employmentInfo != null) {
                HashMap<String, Object> employmentInfo = applicationObject.loanUserObject.employmentInfo;
                requestMap.putOpt(Jsonconstants.OL_EI_STATUS_KEY, employmentInfo.get("emp_status"));
                requestMap.putOpt(Jsonconstants.OL_EI_EMPLOYER_KEY, employmentInfo.get("emp_name"));
                requestMap.putOpt(Jsonconstants.OL_EI_PHONE_KEY, employmentInfo.get("emp_phone"));
                requestMap.putOpt(Jsonconstants.OL_EI_INCOME_KEY, employmentInfo.get("income_per_month"));
            }

            // Construct the auth object for the request
            requestMap.putOpt(Jsonconstants.OL_AUTH_KEY, this.authObject);

            // Construct the request info object for the request
            this.requestInfo.putOpt(Jsonconstants.OL_SERVICENAME_KEY, "AddEmploymentInfo");
            this.requestInfo.putOpt(Jsonconstants.OL_SERVICECODE_KEY, "GAI003");
            this.requestInfo.putOpt(Jsonconstants.OL_REQUESTID_KEY, "1285");
            requestMap.putOpt(Jsonconstants.OL_REQUESTINFO_KEY, this.requestInfo);*/

            vHelper = new VolleyRequestHelper(this);
            String url = Jsonconstants.OL_BASE_URL.concat(Jsonconstants.OL_EMPLOYMENTINFO_SERVICE_KEY);
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

            vHelper.requestString(RequestNameKeys.EMPLOYMENTINFO_REQUEST_KEY, url, requestParams, Request.Method.POST, true);

            initiateVolleyCall(requestMap,Jsonconstants.OL_EMPLOYMENTINFO_SERVICE_KEY);

        } catch (Exception e) {
            Log.v("json exception", e.getLocalizedMessage());
        }
    }

    public void makeFacebookServiceCall(LoanUser userObject) {
        //Construct request
        try {
            JSONObject requestMap = new JSONObject();
            requestMap.putOpt(Jsonconstants.OL_FB_NAME_KEY, (userObject.firstName.concat(" ").concat(userObject.lastName)));
            requestMap.putOpt(Jsonconstants.OL_FB_GENDER_KEY, userObject.gender);
            requestMap.putOpt(Jsonconstants.OL_FB_HOMETOWN_KEY, userObject.city);
            requestMap.putOpt(Jsonconstants.OL_FB_EMAIL_KEY, userObject.emailID);
            requestMap.putOpt(Jsonconstants.OL_FB_DOB_KEY, userObject.DOB);
            requestMap.putOpt(Jsonconstants.OL_FB_WORK_ID_KEY, userObject.fbUserID);
            requestMap.putOpt(Jsonconstants.OL_FB_RELSTATUS_KEY, userObject.relationshipStatus);
            requestMap.putOpt(Jsonconstants.OL_FB_WORK_HISTORY_LOCATION_KEY, userObject.city);

            JSONArray friendList = new JSONArray();
            for (int i = 0; i < userObject.friendList.length(); i++) {
                JSONObject currObject = userObject.friendList.getJSONObject(i);
                friendList.put(currObject.get("name"));
            }


            requestMap.putOpt(Jsonconstants.OL_FB_FRIENDLIST_KEY, friendList);

            JSONObject historyEdu = new JSONObject();
            JSONArray education = new JSONArray();
            for (int i = 0; i < userObject.education.length(); i++) {
                JSONObject currObject = userObject.education.getJSONObject(i);
                JSONObject constructedObject = new JSONObject();
                constructedObject.put(Jsonconstants.OL_FB_EDUCATION_SCHOOL_KEY, currObject.getJSONObject("school").get("name"));
                constructedObject.put(Jsonconstants.OL_FB_WORK_ID_KEY, currObject.get("id"));
                constructedObject.put(Jsonconstants.OL_FB_EDUCATION_TYPE_KEY, currObject.get("type"));
                education.put(constructedObject);
            }
            historyEdu.put(Jsonconstants.OL_FB_HISTORY_KEY, education);
            requestMap.putOpt(Jsonconstants.OL_FB_EDUCATION_KEY, historyEdu);
            JSONObject historyWork = new JSONObject();
            JSONArray work = new JSONArray();
            for (int i = 0; i < userObject.employment.length(); i++) {
                JSONObject currObject = userObject.employment.getJSONObject(i);
                JSONObject constructedObject = new JSONObject();
                constructedObject.put(Jsonconstants.OL_FB_WORK_HISTORY_POSITION_KEY, currObject.getJSONObject("position").get("name"));
                constructedObject.put(Jsonconstants.OL_FB_WORK_HISTORY_LOCATION_KEY, currObject.getJSONObject("location").get("name"));
                constructedObject.put(Jsonconstants.OL_FB_WORK_EMPLOYER_ID_KEY, currObject.getJSONObject("employer").get("name"));
                constructedObject.put(Jsonconstants.OL_FB_WORK_ID_KEY, currObject.get("id"));
                work.put(constructedObject);
            }
            historyWork.put(Jsonconstants.OL_FB_HISTORY_KEY, work);
            requestMap.putOpt(Jsonconstants.OL_FB_WORK_KEY, historyWork);
            JSONObject authObject = new JSONObject();
            authObject.putOpt(Jsonconstants.OL_USERNAME_KEY, "intest");
            authObject.putOpt(Jsonconstants.OL_PASSWORD_KEY, "intest!23");
            requestMap.putOpt(Jsonconstants.OL_AUTH_KEY, authObject);
            JSONObject requestInfo = new JSONObject();
            requestInfo.putOpt(Jsonconstants.OL_SERVICENAME_KEY, "Fb");
            requestInfo.putOpt(Jsonconstants.OL_SERVICECODE_KEY, "2000");
            requestInfo.putOpt(Jsonconstants.OL_REQUESTID_KEY, "12345678");
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

            vHelper.requestString(RequestNameKeys.FB_REQUEST_KEY, url, requestParams, Request.Method.POST, true);

        } catch (Exception e) {
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
            authObject.putOpt(Jsonconstants.OL_USERNAME_KEY, "intest");
            authObject.putOpt(Jsonconstants.OL_PASSWORD_KEY, "intest!23");
            requestMap.putOpt(Jsonconstants.OL_AUTH_KEY, authObject);
            JSONObject requestInfo = new JSONObject();
            requestInfo.putOpt(Jsonconstants.OL_SERVICENAME_KEY, "ValidateReferralCode");
            requestInfo.putOpt(Jsonconstants.OL_SERVICECODE_KEY, "1001");
            requestInfo.putOpt(Jsonconstants.OL_REQUESTID_KEY, "12345678");
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

            vHelper.requestString(RequestNameKeys.VALIDATE_REFERRAL_KEY, url, requestParams, Request.Method.POST, true);


        } catch (Exception e) {
            Log.v("json exception", e.getLocalizedMessage());
        }

    }


    public void makePaymentCall(LoanSummaryModel loanObject) {
        try {

            JSONObject requestMap = requestObjectWithDetails("paymentgateway", "1001", "12345678");

            JSONObject customerDetailsMap = new JSONObject();
            customerDetailsMap.put("loanid",Integer.parseInt(loanObject.getLoanId()));
            customerDetailsMap.put("amount",Long.parseLong(loanObject.getLoanAmount()));
            UUID uniqueKey = UUID.randomUUID();
            customerDetailsMap.put("transaction_id", "IN1354");
            requestMap.putOpt("customer_details", customerDetailsMap);

            initiateVolleyCall(requestMap, Jsonconstants.OL_BASE_URL.concat(Jsonconstants.OL_PAYMENTGATEWAY_SERVICE));

        } catch (Exception e) {
            Log.v("json exception", e.getLocalizedMessage());
        }

    }

    public void registerFCMToken(String token, String email) {
        try {
            JSONObject requestMap = requestObjectWithDetails("storetoken", "1001", "12345678");

            requestMap.putOpt("token", token);
            requestMap.putOpt("email", email);

            initiateVolleyCall(requestMap, Jsonconstants.OL_BASE_URL.concat(Jsonconstants.OL_PUSH_NOTIIFCATION_REGISTRATION_SERVICE));


        } catch (Exception e) {
            Log.v("json exception", e.getLocalizedMessage());
            completionHandler.onRequestCompleted(null, e.getLocalizedMessage());
        }
    }

    public void generateOTPService(String mobileNumber) {

        try {
            JSONObject requestMap = requestObjectWithDetails("OTPGenerator", "2000", "12345678");
            requestMap.put("mobile_number",Long.parseLong(mobileNumber));
            initiateVolleyCall(requestMap, Jsonconstants.OL_BASE_URL.concat(Jsonconstants.OL_OTP_GENERATION_SERVICE));
        } catch (JSONException e) {
            e.printStackTrace();
            completionHandler.onRequestCompleted(null, e.getLocalizedMessage());
        }

    }

    public void validateOTPService(String otp, String mobileNumber) {
        try {
            JSONObject requestMap = requestObjectWithDetails("OTPValidator", "2001", "12345678");
            requestMap.put("mobile_number",Long.parseLong(mobileNumber));
            requestMap.putOpt("otp", otp);
            requestMap.putOpt(Jsonconstants.OL_APPID_KEY, 1001);
            initiateVolleyCall(requestMap, Jsonconstants.OL_BASE_URL.concat(Jsonconstants.OL_OTP_VALIDATION_SERVICE));
        } catch (Exception e) {
            e.printStackTrace();
            completionHandler.onRequestCompleted(null, e.getLocalizedMessage());
        }

    }

    public void getReferallCodeService(String email){
        try {
            JSONObject requestMap = requestObjectWithDetails("GetReferralCode", "1001", "12345678");
            requestMap.put("Email",email);
            requestMap.putOpt("app_id", "10001");
            requestMap.putOpt(Jsonconstants.OL_APPID_KEY, 1001);
            initiateVolleyCall(requestMap, Jsonconstants.OL_BASE_URL.concat(Jsonconstants.OL_REFERRAL_CODE_SERVICE));
        } catch (JSONException e) {
            e.printStackTrace();
            completionHandler.onRequestCompleted(null, e.getLocalizedMessage());
        }
    }

    public void makeBankInfoServiceCall(Application applicationObject) {
        try{
            JSONObject requestMap = requestObjectWithDetails("AddBankInfo", "GAI004", "1285");
            BankInfo bankInfoObject = applicationObject.bankInfoObject;

            requestMap.put(Jsonconstants.OL_BI_IFSC_KEY, bankInfoObject.ifscCode);
            requestMap.put(Jsonconstants.OL_BI_BANK_ADDRESS1_KEY, bankInfoObject.bankAddress1);
            requestMap.put(Jsonconstants.OL_BI_BANK_ADDRESS2_KEY, bankInfoObject.bankAddress2);
            requestMap.put(Jsonconstants.OL_EI_APPLICATIONID_KEY, Long.parseLong(applicationObject.applicationID));

            initiateVolleyCall(requestMap, Jsonconstants.OL_BASE_URL.concat(Jsonconstants.OL_BANKINFO_SERVICE));

        } catch (JSONException e) {
            e.printStackTrace();
            completionHandler.onRequestCompleted(null, e.getLocalizedMessage());
        }
    }


    public void makeDueDateGenerationService(Application applicationObject){
        try{
            JSONObject requestMap = requestObjectWithDetails("due_date_generator", "2002", "12345678");

            requestMap.put(Jsonconstants.OL_DD_LOAN_DURATION_KEY, applicationObject.loanDuration);
            requestMap.put(Jsonconstants.OL_DD_FIRST_PAYDATE_KEY, applicationObject.firstPayDate);
            requestMap.put(Jsonconstants.OL_DD_LOAN_AMOUNT_KEY, applicationObject.loanAmount);
            requestMap.put(Jsonconstants.OL_APPID_KEY, applicationObject.applicationID);
            requestMap.put(Jsonconstants.OL_DD_GENERATE_AGREEMENT_KEY, "false");

            initiateVolleyCall(requestMap, Jsonconstants.OL_BASE_URL.concat(Jsonconstants.OL_DUEDATE_GENERATOR_SERVICE));

        } catch (JSONException e) {
            e.printStackTrace();
            completionHandler.onRequestCompleted(null, e.getLocalizedMessage());
        }
    }

    public void getPaydatesService(String loanID, String applicationID){
        try{
            JSONObject requestMap = requestObjectWithDetails("GetPaydates", "1001", "12345678");

            requestMap.put(Jsonconstants.OL_LOANID_KEY, loanID);
            requestMap.put(Jsonconstants.OL_APPID_KEY, applicationID);

            initiateVolleyCall(requestMap, Jsonconstants.OL_BASE_URL.concat(Jsonconstants.OL_GET_PAYDATES_SERVICE));

        } catch (JSONException e) {
            e.printStackTrace();
            completionHandler.onRequestCompleted(null, e.getLocalizedMessage());
        }
    }

    public void getPersonalInfoService(String emailID){
        try{
            JSONObject requestMap = requestObjectWithDetails("getpersonalinfo", "1001", "12345615");

            requestMap.put(Jsonconstants.OL_NA_EMAIL_KEY, emailID);

            initiateVolleyCall(requestMap, Jsonconstants.OL_BASE_URL.concat(Jsonconstants.OL_GET_PERSONALINFO_SERVICE));

        } catch (JSONException e) {
            e.printStackTrace();
            completionHandler.onRequestCompleted(null, e.getLocalizedMessage());
        }
    }

    public void getEmploymentInfoService(String emailID){
        try{
            JSONObject requestMap = requestObjectWithDetails("getpersonalinfo", "1001", "12345615");

            requestMap.put(Jsonconstants.OL_NA_EMAIL_KEY, emailID);
            requestMap.put(Jsonconstants.OL_APPID_KEY, Jsonconstants.OL_APPID_VALUE);

            initiateVolleyCall(requestMap, Jsonconstants.OL_BASE_URL.concat(Jsonconstants.OL_GET_EMPLOYMENTINFO_SERVICE));

        } catch (JSONException e) {
            e.printStackTrace();
            completionHandler.onRequestCompleted(null, e.getLocalizedMessage());
        }
    }


    public void getLoanHistory(String emailId){
        try{
            JSONObject requestMap = requestObjectWithDetails("customerhistory", "1895", "12345678");
            requestMap.put(Jsonconstants.OL_NA_EMAIL_KEY, emailId);
            requestMap.put(Jsonconstants.OL_APPID_KEY, Jsonconstants.OL_APPID_VALUE);

            vHelper = new VolleyRequestHelper(this);
            String url = Jsonconstants.OL_BASE_URL.concat(Jsonconstants.OL_GET_LOAN_HISTORY);
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

            vHelper.requestString(RequestNameKeys.LOAN_HISTORY_KEY, url, requestParams, Request.Method.POST, true);


        } catch (JSONException e) {
            e.printStackTrace();
            completionHandler.onRequestCompleted(null, e.getLocalizedMessage());
        }
    }

    public void getLoanInfoService(Loan loanObject){
        try{
            JSONObject requestMap = requestObjectWithDetails("loaninfoprovider", "LI001", "1");

            requestMap.put(Jsonconstants.OL_LOANID_KEY, loanObject.loanID);

            vHelper = new VolleyRequestHelper(this);
            String url = Jsonconstants.OL_BASE_URL.concat(Jsonconstants.OL_LOANINFO_SERVICE);
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

            vHelper.requestString(RequestNameKeys.LOAN_INFO_KEY, url, requestParams, Request.Method.POST, true);


        } catch (JSONException e) {
            e.printStackTrace();
            completionHandler.onRequestCompleted(null, e.getLocalizedMessage());
        }

    }



    public void sendMobileDeviceDataToServer(JSONObject deviceData){
        try {

            JSONObject requestMap = deviceData;
            JSONObject authObject = new JSONObject();
            authObject.putOpt(Jsonconstants.OL_USERNAME_KEY, "intest");
            authObject.putOpt(Jsonconstants.OL_PASSWORD_KEY, "intest!23");
            requestMap.putOpt(Jsonconstants.OL_AUTH_KEY, authObject);
            JSONObject requestInfo = new JSONObject();
            requestInfo.putOpt(Jsonconstants.OL_SERVICENAME_KEY, "PhoneData");
            requestInfo.putOpt(Jsonconstants.OL_SERVICECODE_KEY, "GAI004");
            requestInfo.putOpt(Jsonconstants.OL_REQUESTID_KEY, "1289");
            requestMap.putOpt(Jsonconstants.OL_REQUESTINFO_KEY, requestInfo);

            vHelper = new VolleyRequestHelper(this);
            String url = Jsonconstants.OL_BASE_URL.concat(Jsonconstants.OL_SENDPHONE_DATA_SERVICE);
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

            vHelper.requestString(RequestNameKeys.UPLOAD_PHONE_DATA_KEY, url, requestParams, Request.Method.POST, true);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void getReferralCodeInfoService(String emailID, String applicationID){
        try{
            JSONObject requestMap = requestObjectWithDetails("ReferralCodeInfo", "1001", "12345678");

            requestMap.put(Jsonconstants.OL_NA_EMAIL_KEY, emailID);
            requestMap.put(Jsonconstants.OL_APPID_KEY, applicationID);

            initiateVolleyCall(requestMap, Jsonconstants.OL_BASE_URL.concat(Jsonconstants.OL_REFERRALCODE_INFO_SERVICE));

        } catch (JSONException e) {
            e.printStackTrace();
            completionHandler.onRequestCompleted(null, e.getLocalizedMessage());
        }
    }

    public void makeAgreementInfoSaveServiceCall(Application applicationObject){
        try{
            JSONObject requestMap = requestObjectWithDetails("agreementdetails", "1001", "1");

            requestMap.put(Jsonconstants.OL_APPID_KEY, applicationObject.applicationID);
            requestMap.put(Jsonconstants.OL_AS_REASON_KEY, applicationObject.loanReason);
            requestMap.put(Jsonconstants.OL_AS_PREFERRED_DATE_KEY, applicationObject.preferredApplicationPickupDate);
            requestMap.put(Jsonconstants.OL_AS_PREFERRED_TIME_KEY, applicationObject.preferredApplicationPickupTime);

            initiateVolleyCall(requestMap, Jsonconstants.OL_BASE_URL.concat(Jsonconstants.OL_GET_AGREEMENT_INFO_SERVICE));

        } catch (JSONException e) {
            e.printStackTrace();
            completionHandler.onRequestCompleted(null, e.getLocalizedMessage());
        }
    }

    public void makeApplicationAbandonServiceCall(Application applicationObject){
        try{
            JSONObject requestMap = requestObjectWithDetails("MarkAbandoned", "1021", "12345678");

            requestMap.put(Jsonconstants.OL_APPID_KEY, applicationObject.applicationID);

            initiateVolleyCall(requestMap, Jsonconstants.OL_BASE_URL.concat(Jsonconstants.OL_MARK_APPLICATION_ABANDON_SERVICE));

        } catch (JSONException e) {
            e.printStackTrace();
            completionHandler.onRequestCompleted(null, e.getLocalizedMessage());
        }
    }

    public void updateApplicationStateService(Application applicationObject){
        try{
            JSONObject requestMap = requestObjectWithDetails("UpdateState", "1020", "12345678");

            requestMap.put(Jsonconstants.OL_APPID_KEY, applicationObject.applicationID);
            requestMap.put(Jsonconstants.OL_APPLICATION_STATE_KEY, applicationObject.applicationState);

            initiateVolleyCall(requestMap, Jsonconstants.OL_BASE_URL.concat(Jsonconstants.OL_APPLICATION_TRACKING_SERVICE));

        } catch (JSONException e) {
            e.printStackTrace();
            completionHandler.onRequestCompleted(null, e.getLocalizedMessage());
        }
    }





    private JSONObject requestObjectWithDetails(String serviceName, String serviceID, String requestID) {

        try {
            JSONObject requestMap = new JSONObject();
            JSONObject authObject = new JSONObject();
            authObject.putOpt(Jsonconstants.OL_USERNAME_KEY, "intest");
            authObject.putOpt(Jsonconstants.OL_PASSWORD_KEY, "intest!23");
            requestMap.putOpt(Jsonconstants.OL_AUTH_KEY, authObject);
            JSONObject requestInfo = new JSONObject();
            requestInfo.putOpt(Jsonconstants.OL_SERVICENAME_KEY, serviceName);
            requestInfo.putOpt(Jsonconstants.OL_SERVICECODE_KEY, serviceID);
            requestInfo.putOpt(Jsonconstants.OL_REQUESTID_KEY, requestID);
            requestMap.putOpt(Jsonconstants.OL_REQUESTINFO_KEY, requestInfo);

            return requestMap;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void initiateVolleyCall(JSONObject requestMap, String serviceEndPoint) {
        vHelper = new VolleyRequestHelper(this);
        String url = serviceEndPoint;
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

        vHelper.requestString(RequestNameKeys.VALIDATE_REFERRAL_KEY, url, requestParams, Request.Method.POST, true);
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
                                   JSONObject response, String errorMessage) {

        Log.v("response", response.toString());

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
                                   JSONArray response, String errorMessage) {

        Log.v("response", response.toString());

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
                                   String response, String errorMessage) {
        try {

            if (errorMessage == null && response != null && (requestName.equals(RequestNameKeys.FB_REQUEST_KEY) || requestName.equals(RequestNameKeys.VALIDATE_REFERRAL_KEY) || requestName.equals(RequestNameKeys.LOAN_HISTORY_KEY) || requestName.equals(RequestNameKeys.UPLOAD_PHONE_DATA_KEY )|| requestName.equals(RequestNameKeys.EMPLOYMENTINFO_REQUEST_KEY ) || requestName.equals(RequestNameKeys.LOAN_INFO_KEY )))
            {
                JSONObject jsonObject = new JSONObject(response);
                if (requestName.equalsIgnoreCase(RequestNameKeys.LOAN_HISTORY_KEY)){
                    LoanApplicationInfo sModel;
                    sModel = LoanApplicationInfo.LoanApplicationModelFromJSONObject(jsonObject);
                    completionHandler.onRequestCompleted(sModel, null);
                }else if (requestName.equalsIgnoreCase(RequestNameKeys.LOAN_INFO_KEY)){
                    LoanDetailsInfo sModel;
                    sModel = LoanDetailsInfo.LoanDetailsModelFromJSONObject(jsonObject);
                    completionHandler.onRequestCompleted(sModel, null);

                }else {
                    SuccessModel sModel;
                    sModel = SuccessModel.sucessModelFromJSONObject(jsonObject);
                    sModel.response = response;
                    completionHandler.onRequestCompleted(sModel, null);
                    Log.v("response", response);
                }
            } else {
                completionHandler.onRequestCompleted(null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
