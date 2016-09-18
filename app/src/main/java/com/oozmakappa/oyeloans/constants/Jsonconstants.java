package com.oozmakappa.oyeloans.constants;

/**
 * Created by sankarnarayanan on 13/09/16.
 */

/**
 * An interface provides all JSON constants.
 */
public interface Jsonconstants {

    //Referral code key
    String OL_VALIDATE_REFERALCODE_EMAIL = "Email";
    String OL_REFERRALCODE_KEY = "referral_code";
    String OL_APPID_KEY = "app_id";

    //Face book auth registration constants.
    String OL_FB_NAME_KEY = "name";
    String OL_FB_GENDER_KEY = "gender";
    String OL_FB_WORK_KEY = "work";
    String OL_FB_HISTORY_KEY = "history";
    String OL_FB_WORK_HISTORY_POSITION_KEY = "position";
    String OL_FB_WORK_HISTORY_LOCATION_KEY = "location";
    String OL_FB_WORK_ID_KEY = "id";
    String OL_FB_WORK_EMPLOYER_ID_KEY = "employer";
    String OL_FB_HOMETOWN_KEY = "hometown";
    String OL_FB_EMAIL_KEY = "email";
    String OL_FB_DOB_KEY = "birthday";
    String OL_FB_EDUCATION_KEY = "edu";
    String OL_FB_EDUCATION_SCHOOL_KEY = "school";
    String OL_FB_EDUCATION_TYPE_KEY = "type";
    String OL_FB_RELSTATUS_KEY = "relationship_status";
    String OL_FB_FRIENDLIST_KEY = "friend_list";

    // New Application service constants
    String OL_NA_NAME_KEY = "full_name";
    String OL_NA_EMAIL_KEY = "email";
    String OL_NA_MOBILE_NO_KEY = "mobile_no";
    String OL_NA_DOB_KEY = "dob";
    String OL_NA_GENDER_KEY = "gender";
    String OL_NA_PAN_KEY = "pan_number";
    String OL_NA_AADHAR_KEY = "aadhar";
    String OL_NA_ADDRESS1_KEY = "address1";
    String OL_NA_ADDRESS2_KEY = "address2";
    String OL_NA_PINCODE_KEY = "pincode";
    String OL_NA_CITY_KEY = "city";
    String OL_NA_APPLICATIONID_KEY = "app_id";
    String OL_NA_LOAN_AMOUNT_KEY = "loan_amount";
    String OL_NA_LOAN_DURATION_KEY = "loan_duration";
    String OL_NA_FIRST_PAYDATE = "first_paydate";

    // Employment info service constants
    String OL_EI_STATUS_KEY = "employment_status";
    String OL_EI_EMPLOYER_KEY = "employer_name";
    String OL_EI_PHONE_KEY = "employer_phone";
    String OL_EI_INCOME_KEY = "gross_monthly_income";
    String OL_EI_APPLICATIONID_KEY = "app_id";
    String OL_EI_DEGREE_KEY = "highest_degree";
    String OL_EI_INSTITUTION_KEY = "last_instituition_studied";

    //General auth & request info constants.
    String OL_AUTH_KEY = "auth";
    String OL_USERNAME_KEY = "username";
    String OL_USERNAME_VALUE = "intest";
    String OL_PASSWORD_KEY = "password";
    String OL_PASSWORD_VALUE = "selective";
    String OL_REQUESTINFO_KEY = "request_info";
    String OL_SERVICENAME_KEY = "service_name";
    String OL_SERVICECODE_KEY = "service_code";
    String OL_REQUESTID_KEY = "request_id";

    //URL Constants
    String OL_BASE_URL = "https://192.168.56.101/";
    String OL_FBSERVICE_KEY = "fb/fetch/";
    String OL_VALIDATEREFERRAL_KEY = "/referral/use/";
    String OL_NEWAPPLICATION_SERVICE_KEY = "newapplication/";
    String OL_EMPLOYMENTINFO_SERVICE_KEY = "employeeinfo/";




}