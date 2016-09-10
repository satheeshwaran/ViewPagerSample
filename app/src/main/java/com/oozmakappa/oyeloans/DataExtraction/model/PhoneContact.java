/**
 * PhoneContact.java
 * <p/>
 * A model that represents the phone contact.
 *
 * @category Global Analytics
 * @package com.globalanalytics.dataapp.data.model
 * @version 1.0
 * @author Rajkumar.N
 * @copyright Copyright (C) 2016 Global Analytics. All rights reserved.
 */
package com.oozmakappa.oyeloans.DataExtraction.model;

/**
 * A model that represents the phone contact.
 */
public class PhoneContact {

    private String name;
    private String phoneNumber;


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
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "PhoneContact [ name = " + name + ", phone number = " + phoneNumber + "]";
    }
}
