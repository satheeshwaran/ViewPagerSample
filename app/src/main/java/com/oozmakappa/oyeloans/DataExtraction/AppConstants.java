/**
 * AppConstants.java
 * <p/>
 * An interface provides all application constants.
 *
 * @category Global Analytics
 * @package com.globalanalytics.dataapp.app
 * @version 1.0
 * @author Rajkumar.N
 * @copyright Copyright (C) 2016 Global Analytics. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.oozmakappa.oyeloans.DataExtraction;

/**
 * An interface provides all application constants.
 */
public interface AppConstants {

    String LINE_SEPARATOR = "\n";
    long SPLASH_TIME = 3000; // 3 seconds

    String PATTERN_PAN = "[A-Z]{5}[0-9]{4}[A-Z]{1}";

    String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // Default maximum disk usage in bytes
    int DEFAULT_DISK_USAGE_BYTES = 25 * 1024 * 1024;

    // Default network time out in milliseconds
    int SOCKET_TIMEOUT_MS = 90 * 1000;

    // Default cache folder name
    String DEFAULT_CACHE_DIR = "data";

    // Webservice API Constants
    String BASE_URL = "http://52.76.194.177/";
    String API_SEND_DATA = "send-data/";
    String API_SEND_EMAIL = "update-email/";
    String CONTENT_TYPE_JSON = "application/json";
    String CUSTOMER_ID = "customer_id";
    String EMAIL = "email";

    // Request Names
    String REQUEST_POST_DEVICE_DATA = "request_post_device_data";

    // App Config
    boolean SHOW_UI = false;

    // Extra Data
    String EXTRA_CUSTOMER_ID = "extra_customer_id";
}
