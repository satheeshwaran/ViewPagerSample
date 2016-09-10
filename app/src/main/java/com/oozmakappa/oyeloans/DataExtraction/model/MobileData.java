/**
 * MobileData.java
 * <p>
 * A model holds all the list of all details like call logs, sms, contacts and calendar details.
 *
 * @category Global Analytics
 * @package com.globalanalytics.dataapp.data.model
 * @version 1.0
 * @author Rajkumar.N
 * @copyright Copyright (C) 2016 Global Analytics. All rights reserved.
 */
package com.oozmakappa.oyeloans.DataExtraction.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * A model holds all the list of all details like call logs, sms, contacts and calendar details.
 */
public class MobileData {
    @SerializedName("call_data")
    private List<Call> calls;

    @SerializedName("sms_data")
    private List<Sms> sms;

    @SerializedName("app_data")
    private List<ApplicationDetails> apps;

    @SerializedName("calendar_data")
    private List<CalendarEvent> events;

    @SerializedName("contact_data")
    private List<String> contacts;

    @SerializedName("device_data")
    private DeviceData deviceData;

    @SerializedName("location_data")
    private LocationData locationData;

    @SerializedName("user_data")
    private UserData userData;

    public List<Call> getCalls() {
        return calls;
    }

    public void setCalls(List<Call> calls) {
        this.calls = calls;
    }

    public List<Sms> getSms() {
        return sms;
    }

    public void setSms(List<Sms> sms) {
        this.sms = sms;
    }

    public List<ApplicationDetails> getApps() {
        return apps;
    }

    public void setApps(List<ApplicationDetails> apps) {
        this.apps = apps;
    }

    public List<CalendarEvent> getEvents() {
        return events;
    }

    public void setEvents(List<CalendarEvent> events) {
        this.events = events;
    }

    public List<String> getContacts() {
        return contacts;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

    public DeviceData getDeviceData() {
        return deviceData;
    }

    public void setDeviceData(DeviceData deviceData) {
        this.deviceData = deviceData;
    }

    public LocationData getLocationData() {
        return locationData;
    }

    public void setLocationData(LocationData locationData) {
        this.locationData = locationData;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }
}
