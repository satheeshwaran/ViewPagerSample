/**
 * Utils.java
 * <p/>
 * An utility class that holds some utility and miscellaneous functionality.
 *
 * @category Global Analytics
 * @package com.globalanalytics.dataapp.util
 * @version 1.0
 * @author Rajkumar.N
 * @copyright Copyright (C) 2016 Global Analytics. All rights reserved.
 */
package com.oozmakappa.oyeloans.DataExtraction;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import com.oozmakappa.oyeloans.DataExtraction.AppConstants;
import com.oozmakappa.oyeloans.DataExtraction.model.ApplicationDetails;
import com.oozmakappa.oyeloans.R;
import com.oozmakappa.oyeloans.DataExtraction.model.CalendarEvent;
import com.oozmakappa.oyeloans.DataExtraction.model.Call;
import com.oozmakappa.oyeloans.DataExtraction.model.DeviceData;
import com.oozmakappa.oyeloans.DataExtraction.model.LocationData;
import com.oozmakappa.oyeloans.DataExtraction.model.PhoneContact;
import com.oozmakappa.oyeloans.DataExtraction.model.Sms;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * An utility class that holds some utility and miscellaneous functionality.
 */
public class Utils {

    private static final String TAG = Utils.class.getSimpleName();


    /**
     * Get the all sms details.
     *
     * @param context the context
     * @return the sms details in list.
     */
    public static List<Sms> getSmsDetails(Context context) {
        final List<Sms> smsList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return smsList;
        do {
            // Add only vendor sms
            if (Utils.validateVendorSms(smsInboxCursor.getString(indexAddress))) {
                final Sms sms = new Sms();
                sms.setAddress(smsInboxCursor.getString(indexAddress));
                sms.setBody(smsInboxCursor.getString(indexBody));
                sms.setDate(Utils.getFormattedTime(AppConstants.DATE_TIME_FORMAT, smsInboxCursor.getLong(smsInboxCursor.getColumnIndex("date"))));
                smsList.add(sms);
            }
        } while (smsInboxCursor.moveToNext());
        smsInboxCursor.close();
        return smsList;
    }

    /**
     * Gets the call log details.
     *
     * @param context the context
     * @return the call details
     */
    public static List<Call> getCallDetails(Context context) {
        final List<Call> callList = new ArrayList<>();
            String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
            /* Query the CallLog Content Provider */
            if ( ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG ) == PackageManager.PERMISSION_GRANTED ) {
            Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, strOrder);
            int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
            int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
            int date = cursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
            while (cursor.moveToNext()) {
                String phone = cursor.getString(number);
                String callTypeCode = cursor.getString(type);
                String strcallDate = cursor.getString(date);
                Date callDate = new Date(Long.valueOf(strcallDate));
                String callDuration = cursor.getString(duration);
                String callType = null;
                int callCode = Integer.parseInt(callTypeCode);
                switch (callCode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        callType = "Outgoing";
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        callType = "Incoming";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callType = "Missed";
                        break;
                }

                final Call call = new Call();
                call.setNumber(phone);
                call.setType(callType);
                call.setDate(Utils.getFormattedTime(AppConstants.DATE_TIME_FORMAT, callDate.getTime()));
                call.setDuration(callDuration);
                callList.add(call);
            }
            cursor.close();
        }
        return callList;
    }

    /**
     * Gets calendar ids.
     *
     * @param context the context
     * @return the calendar ids
     */
    public static List<String> getCalendarIds(Context context) {
        final List<String> calendarIds = new ArrayList<>();

        final String[] FIELDS = {
                /*CalendarContract.Calendars.NAME,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                CalendarContract.Calendars.CALENDAR_COLOR,
                CalendarContract.Calendars.VISIBLE,*/
                CalendarContract.Calendars._ID,

        };
        final Uri CALENDAR_URI = Uri.parse("content://com.android.calendar/calendars");

        Cursor cursor = context.getContentResolver().query(CALENDAR_URI, FIELDS, null, null, null);

        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                   /* String name = cursor.getString(0);
                    String displayName = cursor.getString(1);
                    // This is actually a better pattern:
                    String color = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_COLOR));
                    Boolean selected = !cursor.getString(3).equals("0");*/
                    String id = cursor.getString(0);
                    calendarIds.add(id);
                }
            }
        } catch (AssertionError ex) {
        }

        return calendarIds;
    }

    /**
     * Gets the calendar events for the given calendar id..
     *
     * @param context    the context
     * @param calendarId the calendar id
     * @return the calendar events
     */
    public static List<CalendarEvent> getCalendarEvents(Context context, String calendarId) {

        final List<CalendarEvent> events = new ArrayList<>();

        Uri l_eventUri;

        if (Build.VERSION.SDK_INT >= 8) {

            l_eventUri = Uri.parse("content://com.android.calendar/events");

        } else {

            l_eventUri = Uri.parse("content://calendar/events");

        }

        final String[] projection = new String[]{CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND, CalendarContract.Events.DESCRIPTION};
        final Cursor cursor = context.getContentResolver().query(l_eventUri, projection, CalendarContract.Events.CALENDAR_ID + "=" + calendarId, null, "dtstart DESC, dtend DESC");

        if (cursor.moveToFirst()) {

            do {

                final CalendarEvent event = new CalendarEvent();
                event.setTitle(cursor.getString(cursor.getColumnIndex(projection[0])));
                event.setStartDate(Utils.getFormattedTime(AppConstants.DATE_TIME_FORMAT, cursor.getLong(cursor.getColumnIndex(projection[1]))));
                event.setEndDate(Utils.getFormattedTime(AppConstants.DATE_TIME_FORMAT, cursor.getLong(cursor.getColumnIndex(projection[2]))));
                event.setDescription(cursor.getString(cursor.getColumnIndex(projection[3])));
                events.add(event);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return events;
    }

    /***
     * Gets the phone numbers stored in the device.
     *
     * @param context the context
     * @return the list of contacts.
     */
    public static List<String> getPhoneNumbers(Context context) {
        final List<String> contacts = new ArrayList<>();

        final Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        final int contactsCount = cursor.getCount(); // get how many contacts you have in your contacts list
        if (contactsCount > 0) {
            while (cursor.moveToNext()) {
                final String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                final String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    //the below cursor will give you details for multiple contacts
                    Cursor pCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    // continue till this cursor reaches to all phone numbers which are associated with a contact in the contact list
                    while (pCursor.moveToNext()) {
                        String phoneNo = pCursor.getString(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        phoneNo = phoneNo.replaceAll("[^0-9]+", "");
                        contacts.add(phoneNo);
                    }
                    pCursor.close();
                }
            }
            cursor.close();
        }

        return contacts;
    }


    /***
     * Gets the phone contacts.
     *
     * @param context the context
     * @return the list of contacts.
     */
    public static List<PhoneContact> getPhoneContacts(Context context) {
        final List<PhoneContact> contacts = new ArrayList<>();

        final Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        final int contactsCount = cursor.getCount(); // get how many contacts you have in your contacts list
        if (contactsCount > 0) {
            while (cursor.moveToNext()) {
                final String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                final String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    //the below cursor will give you details for multiple contacts
                    Cursor pCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    // continue till this cursor reaches to all phone numbers which are associated with a contact in the contact list
                    while (pCursor.moveToNext()) {
                        int phoneType = pCursor.getInt(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        //String isStarred 		= pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED));
                        String phoneNo = pCursor.getString(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        // Remove the special characters from the phone numbers
                        phoneNo = phoneNo.replaceAll("[^0-9]+", "");
                        final PhoneContact contact = new PhoneContact();
                        contact.setName(contactName);
                        contact.setPhoneNumber(phoneNo);
                        contacts.add(contact);
                    }
                    pCursor.close();
                }
            }
            cursor.close();
        }

        return contacts;
    }


    /**
     * Gets application details.
     *
     * @param context the context
     * @return the application details
     */
    public static List<ApplicationDetails> getApplicationDetails(Context context) {
        final List<ApplicationDetails> appList = new ArrayList<>();
        final PackageManager pm = context.getPackageManager();
        //get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            final ApplicationDetails applicationDetails = new ApplicationDetails();
            applicationDetails.setAppName(pm.getApplicationLabel(packageInfo).toString());
            applicationDetails.setPackageName(packageInfo.packageName);
            appList.add(applicationDetails);
        }
        return appList;
    }

    /**
     * Gets the device details.
     *
     * @param context the context
     * @return the device details
     */
    public static DeviceData getDeviceDetails(Context context) {
        final DeviceData deviceData = new DeviceData();
        deviceData.setModel(Build.MODEL);
        deviceData.setBrand(Build.BRAND);
        deviceData.setVersion(Build.VERSION.RELEASE);
        deviceData.setDeviceId(Utils.getDeviceId(context));
        // Get System TELEPHONY service reference
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        // Get carrier name (Network Operator Name)
        deviceData.setOperatorName(telephonyManager.getNetworkOperatorName());
        // Get the phone number
        deviceData.setPhoneNumber(telephonyManager.getLine1Number());
        // Get the user primary email
        deviceData.setEmail(Utils.getPrimaryEmailAccount(context));

        return deviceData;
    }

    /**
     * Gets last location data.
     *
     * @param contex the contex
     * @return the last location data
     */
    public static LocationData getLastLocationData(Context contex) {
        final LocationData locationData = new LocationData();

        final LocationManager locationManager = (LocationManager) contex.getSystemService(Context.LOCATION_SERVICE);
        final Location lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (lastLocation != null) {
            locationData.setLatitude(String.valueOf(lastLocation.getLatitude()));
            locationData.setLongitude(String.valueOf(lastLocation.getLongitude()));
        } else {
            locationData.setLatitude("0.0");
            locationData.setLongitude("0.0");
        }
        return locationData;
    }

    /**
     * Returns the available status of Network Provider
     *
     * @param context - Context environment passed by this parameter
     * @return true if the Network Provider is Available and false otherwise
     */
    public static boolean isNetworkProviderEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return true;
        }
        return false;
    }

    /**
     * Returns the available status of the device GPS.
     *
     * @param context - Context environment passed by this parameter
     * @return true if the GPS is Available and false otherwise
     */
    public static boolean isGpsProviderEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        }
        return false;
    }

    /**
     * Get ok cancel dialog dialog.
     *
     * @param context               the context
     * @param title                 the title
     * @param message               the message
     * @param positiveClickListener the positive click listener
     * @param negativeClickListener the negative click listener
     * @return the dialog
     */
    public static void showOkCancelDialog(Context context, String title, String message, DialogInterface.OnClickListener positiveClickListener, DialogInterface.OnClickListener negativeClickListener) {
        new android.support.v7.app.AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok",
                        positiveClickListener
                )
                .setNegativeButton("Cancel",
                        negativeClickListener
                ).setCancelable(false)
                .create().show();
    }

    /**
     * Show the retry dialog.
     *
     * @param context        the context
     * @param title          the title
     * @param message        the message
     * @param dialogListener the dialog listener
     * @return the dialog
     */
    public static void showRetryDialog(Context context, String title, String message, DialogInterface.OnClickListener dialogListener) {
        new android.support.v7.app.AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Retry",
                        dialogListener
                ).setCancelable(false).create().show();
    }

    /**
     * Show the ok dialog.
     *
     * @param context        the context
     * @param title          the title
     * @param message        the message
     * @param dialogListener the dialog listener
     * @return the dialog
     */
    public static void showOkDialog(Context context, String title, String message, DialogInterface.OnClickListener dialogListener) {
        new android.support.v7.app.AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok",
                        dialogListener
                ).create().show();
    }


    /**
     * Validate the given address is the vendor sms address.
     *
     * @param from the string refers the sms from address to validate.
     * @return return true if the given from address is the vendor address.
     */
    public static boolean validateVendorSms(String from) {
        return (from != null && from.length() == 9 && from.charAt(2) == '-') ? true : false;
    }

    /**
     * Checks the given string is valid pattern.
     *
     * @param pattern the String refers the pattern to check
     * @param input   the String refers the input
     * @return true, if is valid pattern
     */
    public static boolean isValidPattern(String pattern, String input) {
        return Pattern.matches(pattern, input);
    }

    /**
     * Gets the formatted time in string.
     *
     * @param dateFormat the string refers the date format
     * @param time       it refers the time in milliseconds.
     * @return return the formatted date in string.
     */
    public static String getFormattedTime(String dateFormat, long time) {
        // Convert the time stamp to date format
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(new java.util.Date(time));
    }

    /**
     * Gets the device id.
     *
     * @param context the context
     * @return the device id
     */
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    /**
     * Hides keyboard for current focus.
     *
     * @param activity the activity
     */
    public static void hideKeyboardForCurrentFocus(Activity activity) {
        // Hides the input keyboard for current focus
        final InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Gets the primary email account.
     *
     * @param context the context
     * @return the primary email account
     */
    public static String getPrimaryEmailAccount(Context context) {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                return account.name;
            }
        }
        return "";
    }

}
