/**
 * ApplicationDetails.java
 * <p/>
 * A model that represents the application details installed on the mobile.
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
 * A model that represents the application details installed on the mobile.
 */
public class ApplicationDetails {

    @SerializedName("app_name")
    private String appName;

    @SerializedName("package_name")
    private String packageName;

    @Override
    public String toString() {
        return "ApplicationDetails [ appName = " + appName + ", packageName = " + packageName + "]";
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
