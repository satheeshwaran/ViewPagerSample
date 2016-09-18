package com.oozmakappa.oyeloans.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by satheeshwaran on 9/7/16.
 */
public class LoanUser {
    public String firstName="";
    public String lastName="";
    public String fbUserID="";
    public String emailID="";
    public String PANNumber="";
    public String aadharNumber = "";;
    public String mobileNumber="";
    public String landlineNumber="";

    public String fbUserName = "";
    public String fbProfileLink = "";
    public String fbProfilePicURL = "";
    public String DOB = "";
    public Boolean isFBProfileVerified = false;
    public int ageRange;
    public double totalFriendcount;
    public String gender = "";
    public String highestEducation = "";
    public String highestEducationPlace= "";
    public String highestEducationYear ="";

    public String workPlace ="";
    public String workTitle ="";
    public String workStartDate="";
    public int totalWorkExperience;
    public String doorNumber = "";

    public String PINCode = "";
    public String street = "";
    public String locaility = "";
    public String city = "";
    public String landmark = "";
    public String state = "";
    public String relationshipStatus = "";

    public JSONArray employment;
    public HashMap<String, Object> employmentInfo = null;

    public JSONArray friendList;
    public JSONArray education;

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
            user.relationshipStatus = object.getString("relationship_status");

            if (object.getJSONArray("work") != null) {
                JSONObject workArray = (JSONObject) object.getJSONArray("work").get(0);
                user.workPlace = workArray.getJSONObject("employer").getString("name");
                user.workTitle = workArray.getJSONObject("position").getString("name");
                user.totalWorkExperience = calcaulteTotalWorkExp(object.getJSONArray("work"));
            }

            user.employment = object.getJSONArray("work");
            if (object.getJSONObject("friends") != null){
                user.friendList = object.getJSONObject("friends").getJSONArray("data");
            }
            user.education = object.getJSONArray("education");
            user.city = object.getJSONObject("location").getString("name");
            ArrayList<String> eductaionList = calculateHighestQualification(object.getJSONArray("education"));

            if(!eductaionList.isEmpty()) {
                user.highestEducation = eductaionList.get(0);
                user.highestEducationYear= eductaionList.get(1);
                user.highestEducationPlace = eductaionList.get(2);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    private static int calcaulteTotalWorkExp(JSONArray workExpArray){
        try {
            String startDate = ((JSONObject)workExpArray.get(workExpArray.length()-1)).getString("start_date");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date convertedDate = dateFormat.parse(startDate);
            return getDiffYears(convertedDate,new Date());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) ||
                (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            diff--;
        }
        return diff;
    }

    private static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(date);
        return cal;
    }

    private static ArrayList<String> calculateHighestQualification(JSONArray jsonArray){

        int recentYear = 0;
        String highestQualification = "";
        String highestQualifaicationPlace = "";
        try {


        for (int i=0;i<jsonArray.length();i++){
                JSONObject workItem = (JSONObject) jsonArray.get(i);
                if (workItem.getString("type").equalsIgnoreCase("college") && workItem.has("concentration") ){
                    int year = Integer.parseInt(workItem.getJSONObject("year").getString("name"));
                    if(year>recentYear) {
                        recentYear = year;
                        highestQualification = ((JSONObject)workItem.getJSONArray("concentration").get(0)).getString("name");
                        highestQualifaicationPlace = workItem.getJSONObject("school").getString("name");
                    }
                }

        }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList returnList = new ArrayList();
        returnList.add(highestQualification);
        returnList.add(String.valueOf(recentYear));
        returnList.add(highestQualifaicationPlace);

        return returnList;
    }
}
