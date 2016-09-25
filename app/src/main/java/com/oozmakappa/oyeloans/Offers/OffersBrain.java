package com.oozmakappa.oyeloans.Offers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.oozmakappa.oyeloans.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Satheesh on 27/01/16.
 */
public class OffersBrain implements ConnectionCallbacks,OnConnectionFailedListener, ResultCallback<Status> {

    public Context ourContext;

    private static OffersBrain ourInstance = null;

    public static OffersBrain getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new OffersBrain(context);
        }
        return ourInstance;
    }

    public static OffersBrain getInstance() {
        if (ourInstance == null) {
            ourInstance = new OffersBrain();
        }
        return ourInstance;
    }

    protected static final String TAG = "OfferBrain";

    /**
     * The current set of monitored fences.
     */
    private ArrayList<Geofence> currentMonitoredFences;

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Used to keep track of whether geofences were added.
     */
    private boolean mGeofencesAdded;

    /**
     * Used when requesting to add or remove geofences.
     */
    private PendingIntent mGeofencePendingIntent;

    /**
     * Used to persist application state about whether geofences were added.
     */
    private SharedPreferences mSharedPreferences;

    private GoogleApiClient client;

    private Boolean googleAPIClientConnected = false;

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(ourContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    public OffersBrain(){

    }

    public OffersBrain(Context context) {

        ourContext = context;

        currentMonitoredFences = new ArrayList<>();

        mSharedPreferences =  context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,
                ourContext.MODE_PRIVATE);


        buildGoogleApiClient();

        client = new GoogleApiClient.Builder(ourContext).addApi(AppIndex.API).build();

        client.connect();

        mGoogleApiClient.connect();


    }


    /**
     * Method to populate geofences and to create Geofence Request objects for all the Fences objects we receive
     * from the remote service.
     */
    public void populateGeofenceList(ArrayList<Fence> fenceObjects) {

        if(googleAPIClientConnected) {

            currentMonitoredFences.clear();

            for (Fence fence : fenceObjects) {

                System.out.println(fence.fenceRadius.floatValue());

                currentMonitoredFences.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(fence.fenceID)

                    // Set the circular region of this geofence.
                    .setCircularRegion(
                            fence.fenceCenter.latitude,
                            fence.fenceCenter.longitude,
                            fence.fenceRadius.floatValue()
                    )
                    // Set the expiration duration of the geofence. This geofence gets automatically
                    // removed after this period of time.
                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)

                    // Set the transition types of interest. Alerts are only generated for these
                    // transition. We track entry and exit transitions in this sample.
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)

                    // Create the geofence.
                    .build());
            }

            registerForFences();
        }
    }

    public void registerForFences(){
        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    // The GeofenceRequest object.
                    getGeofencingRequest(),
                    // A pending intent that that is reused when calling removeGeofences(). This
                    // pending intent is used to generate an intent when a matched geofence
                    // transition is observed.
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }
    }


    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        googleAPIClientConnected = true;
        readOfferData();
        Log.i(TAG, "Connected to GoogleApiClient");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason.
        Log.i(TAG, "Connection suspended");

        // onConnected() will be called again automatically when the service reconnects
    }

    public void onResult(Status status) {
        if (status.isSuccess()) {
            // Update state and save in shared preferences.
            mGeofencesAdded = !mGeofencesAdded;
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(Constants.GEOFENCES_ADDED_KEY, mGeofencesAdded);
            editor.apply();
            /*Toast.makeText(
                    ourContext,
                    mGeofencesAdded ? R.string.geofences_added :
                            R.string.geofences_removed,
                    Toast.LENGTH_SHORT*/
            ).show();

        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(ourContext,
                    status.getStatusCode());
            Log.e(TAG, errorMessage);
        }
    }


    /**
     * Builds and returns a GeofencingRequest. Specifies the list of geofences to be monitored.
     * Also specifies how the geofence notifications are initially triggered.
     */
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofences(currentMonitoredFences);

        // Return a GeofencingRequest.
        return builder.build();
    }

    /**
     * Gets a PendingIntent to send with the request to add or remove Geofences. Location Services
     * issues the Intent inside this PendingIntent whenever a geofence transition occurs for the
     * current list of geofences.
     *
     * @return A PendingIntent for the IntentService that handles geofence transitions.
     */
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(ourContext, OfferFenceTransitionService.class);
        return PendingIntent.getService(ourContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void logSecurityException(SecurityException securityException) {
        Log.e(TAG, "Invalid location permission. " +
                "You need to use ACCESS_FINE_LOCATION with geofences", securityException);
    }

    private void readOfferData(){

        ArrayList <Fence> arrayList = new ArrayList<>();

        try {
            InputStream is = ourContext.getAssets().open("offer_fences.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONObject jso = new JSONObject(json);
            JSONArray ja = jso.getJSONArray("geoFences");
            for (int i = 0; i < ja.length(); i++) {
                arrayList.add(new Fence(ja.getJSONObject(i)));
            }
            System.out.println(arrayList);
            populateGeofenceList(arrayList);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
