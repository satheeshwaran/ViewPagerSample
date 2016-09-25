package com.oozmakappa.oyeloans.Offers;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.ArrayList;

enum FenceType {

    FENCE_TYPE_CIRCULAR,
    FENCE_TYPE_UNKOWN
}

/**
 * Created by Satheesh on 26/09/16.
 */
public class Fence {

    public Fence(){

    }

    public String fenceID;
    public LatLng fenceCenter;
    public Double fenceRadius;
    public ArrayList<Offer> fenceOffers;
    public Double fenceStartTime;
    public Double fenceEndTime;
    public FenceType fenceType = FenceType.FENCE_TYPE_UNKOWN;

    public  Fence (JSONObject jsonObject){
        try {
            JSONObject fenceObject = jsonObject.getJSONObject("fenceDetails");

            this.fenceID = fenceObject.getString("fenceId");
            this.fenceRadius = fenceObject.getJSONObject("fenceCoordinates").getDouble("radius");
            this.fenceStartTime = fenceObject.getJSONObject("fenceDuration").getDouble("startTime");
            this.fenceEndTime = fenceObject.getJSONObject("fenceDuration").getDouble("endTime");
            this.fenceType = FenceType.FENCE_TYPE_CIRCULAR;
            this.fenceCenter = new LatLng(fenceObject.getJSONObject("fenceCoordinates").getJSONObject("fenceCenter").getDouble("latitude"),fenceObject.getJSONObject("fenceCoordinates").getJSONObject("fenceCenter").getDouble("longitude"));
            this.fenceOffers = new ArrayList<Offer>();

            JSONObject offerObject = jsonObject.getJSONObject("offer");
            this.fenceOffers.add(new Offer(offerObject));
        }

        catch (Exception ex)
        {
            Log.e("Fence Model", "Failed to parse JSON due to: " + ex);
        }
    }
}
