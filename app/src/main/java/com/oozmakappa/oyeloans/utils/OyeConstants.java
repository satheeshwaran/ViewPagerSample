package com.oozmakappa.oyeloans.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by satheeshwaran on 9/7/16.
 */
public class OyeConstants {

    public static final String REFERAL_CODE = "REFERAL_CODE";
    public static final int SMS_OTP_SIZE = 6;
    public static String fbMeRequestFields = "id,name,link,birthday,gender,first_name,last_name,verified,email,location,picture.type(large),age_range,devices,education,about,context,work,hometown,friends.limit(20),relationship_status";
    public static List<String> permissionNeeds= Arrays.asList("user_status","user_about_me","user_location","user_education_history","user_posts","user_friends","user_relationship_details","user_work_history","user_relationships");
    //public static String smsOTPIncomingMessageSenderNumber = "+919176034562";
    public static String smsOTPIncomingMessageSenderNumber1 = "VK-KAPMSG";
    public static String smsOTPIncomingMessageSenderNumber2 = "VM-KAPMSG";
    public static String FCM_REGISTRATION_TOKEN = "fcm_registration_id";
}
