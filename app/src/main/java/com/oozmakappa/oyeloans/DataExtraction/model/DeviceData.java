/**
 * DeviceData.java
 * <p/>
 * A model that represents the device information.
 *
 * @category Global Analytics
 * @package com.globalanalytics.dataapp.data.model
 * @version 1.0
 * @author Rajkumar.N
 * @copyright Copyright (C) 2016 Global Analytics. All rights reserved.
 */
package com.oozmakappa.oyeloans.DataExtraction.model;

import com.google.gson.annotations.SerializedName;

/**
 * A model that represents the device information.
 */
public class DeviceData {

    private String model;
    private String brand;
    private String version;

    @SerializedName("device_id")
    private String deviceId;

    @SerializedName("operator_name")
    private String operatorName;
    private String phoneNumber;
    private String email;


    @Override
    public String toString() {
        return "DeviceData [ model = " + model + ", brand = " + brand + ", version = " + version + ", operatorName = " + operatorName + ", Device Id = " + deviceId + ", Phone Number = " + phoneNumber + ", Email = " + email + "]";
    }

    /**
     * Gets model.
     *
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets model.
     *
     * @param model the model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Gets brand.
     *
     * @return the brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets brand.
     *
     * @param brand the brand
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Gets version.
     *
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets version.
     *
     * @param version the version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Gets operator name.
     *
     * @return the operator name
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * Sets operator name.
     *
     * @param operatorName the operator name
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    /**
     * Gets device id.
     *
     * @return the device id
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * Sets device id.
     *
     * @param deviceId the device id
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * Gets phone number.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets phone number.
     *
     * @param phoneNumber the phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
