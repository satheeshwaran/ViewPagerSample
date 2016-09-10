/**
 * UserData.java
 * <p>
 * A model that represents the user data.
 *
 * @category Global Analytics
 * @package com.globalanalytics.dataapp.data.model
 * @version 1.0
 * @author Rajkumar.N
 * @copyright Copyright (C) 2016 Global Analytics. All rights reserved.
 */
package com.oozmakappa.oyeloans.DataExtraction.model;

/**
 * A model that represents the user data.
 */
public class UserData {
    private String pan;
    private String aadhaar;

    /**
     * Gets the pan.
     *
     * @return the pan
     */
    public String getPan() {
        return pan;
    }

    /**
     * Sets the pan.
     *
     * @param pan the pan
     */
    public void setPan(String pan) {
        this.pan = pan;
    }

    /**
     * Gets the aadhaar.
     *
     * @return the aadhaar
     */
    public String getAadhaar() {
        return aadhaar;
    }

    /**
     * Sets the aadhaar.
     *
     * @param aadhaar the aadhaar
     */
    public void setAadhaar(String aadhaar) {
        this.aadhaar = aadhaar;
    }
}
