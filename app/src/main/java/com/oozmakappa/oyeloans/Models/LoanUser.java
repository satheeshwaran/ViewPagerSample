package com.oozmakappa.oyeloans.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by satheeshwaran on 9/7/16.
 */
public class LoanUser {
    public String firstName;
    public String lastName;
    public String fbUserID;
    public String emailID;
    public String PANNumber;

    public String fbUserName = "";
    public String fbProfileLink = "";
    public String fbProfilePicURL = "";
    public String DOB = "";
    public Boolean isFBProfileVerified = false;
    public int ageRange;
    public double totalFriendcount;
    public String gender = "";
    public String education;
    public String workPlace;
    public String workTitle;
    public int totalWorkExperience;


    public static LoanUser loanUserFromJSONObject(JSONObject object){
        LoanUser user = new LoanUser();
        try {
            user.firstName = object.getString("first_name");
            user.lastName = object.getString("last_name");
            user.fbUserID = object.getString("id");
            user.fbUserName = object.getString("name");
            user.fbProfileLink = object.getString("link");
            user.emailID = object.getString("email");
            user.fbProfilePicURL = object.getJSONObject("picture").getJSONObject("data").getString("url");
            user.ageRange = object.getJSONObject("age_range").getInt("min");
            user.totalFriendcount =object.getJSONObject("friends").getJSONObject("summary").getDouble("total_count");
            user.DOB = object.getString("birthday");
            user.gender = object.getString("gender");

            if (object.getJSONArray("work") != null) {
                JSONObject workArray = (JSONObject) object.getJSONArray("work").get(0);
                user.workPlace = workArray.getJSONObject("employer").getString("name");
                user.workTitle = workArray.getJSONObject("employer").getString("position");
                user.totalWorkExperience = calcaulteTotalWorkExp(object.getJSONArray("work"));
            }
            user.education = object.getString("");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    private static int calcaulteTotalWorkExp(JSONArray workExpArray){
        try {
            String startDate = ((JSONObject)workExpArray.get(workExpArray.length())).getString("start_date");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
            Date convertedDate = new Date();
            return getDiffYears(new Date(),convertedDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) ||
                (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(date);
        return cal;
    }
}
