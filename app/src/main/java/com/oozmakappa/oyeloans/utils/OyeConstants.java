package com.oozmakappa.oyeloans.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by satheeshwaran on 9/7/16.
 */
public class OyeConstants {

    public static String fbMeRequestFields = "id,name,link,birthday,gender,first_name,last_name,verified,email,location,picture.type(large),age_range,devices,education,about,context,friends,work,hometown";
    public static List<String> permissionNeeds= Arrays.asList("user_status","user_about_me","user_location","user_education_history","user_posts","user_friends","user_relationship_details","user_work_history","user_relationships");
}
