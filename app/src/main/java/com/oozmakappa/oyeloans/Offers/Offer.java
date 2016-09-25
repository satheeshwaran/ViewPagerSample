package com.oozmakappa.oyeloans.Offers;

import android.util.Log;
import org.json.JSONObject;


/**
 * Created by Satheesh on 26/09/16.
 */
public class Offer {

    public String offerID;
    public String offerURL;
    public String offerTitle;
    public String offerMessage;
    public String offerVendorName;
    public String offerDescription;
    public String offerStartDate;
    public String offerExpiryDate;

    public Offer(JSONObject jsonObject){
        try {
            this.offerID = jsonObject.getString("offerId");
            this.offerTitle = jsonObject.getString("offerTitle");
            this.offerMessage = jsonObject.getString("offerDescription");
            this.offerVendorName = jsonObject.getString("offerVendorName");
            this.offerDescription = jsonObject.getString("offerDescription");
            this.offerStartDate = jsonObject.getString("offerStartDate");
            this.offerExpiryDate = jsonObject.getString("offerExpiryDate");
        }

        catch (Exception ex)
        {
            Log.e("Fence Model", "Failed to parse JSON due to: " + ex);
        }
    }
}
