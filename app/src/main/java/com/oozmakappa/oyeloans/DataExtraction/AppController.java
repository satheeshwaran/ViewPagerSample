/**
 * AppController.java
 * <p/>
 * An application class that controls the entire application.
 *
 * @category Global Analytics
 * @package com.globalanalytics.dataapp.app
 * @version 1.0
 * @author Rajkumar.N
 * @copyright Copyright (C) 2016 Global Analytics. All rights reserved.
 */
package com.oozmakappa.oyeloans.DataExtraction;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.oozmakappa.oyeloans.DataExtraction.model.CalendarEvent;
import com.oozmakappa.oyeloans.DataExtraction.model.MobileData;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * An application class that controls the entire application.
 */
public class AppController extends Application {

    private static final String TAG = AppController.class.getSimpleName();

    public interface DataFetchListener {
        /**
         * A call back triggered once the device data fetch completed.
         */
        void onFetchCompleted();

        /**
         * A call back that publish progress while the data fetching process going on.
         *
         * @param progress the integer refers the current progress value.
         */
        void onProgressUpdate(Integer progress);
    }

    /**
     * The application instance.
     */
    private static AppController mInstance;

    private static MobileData mMobileData;

    /**
     * The application request queue.
     */
    private RequestQueue mRequestQueue;


    @Override
    public void onCreate() {
        super.onCreate();
        // Initializes the application instance.
        mInstance = this;
    }

    /**
     * To get the Application instance.
     *
     * @return AppController it return the instance of the Application.
     */
    public static synchronized AppController getInstance() {
        return mInstance;
    }


    /**
     * Start the data fetch process. It call back the <b>onFetchCompleted</b> once the process completed..
     *
     * @param listener the listener
     */
    public synchronized void startDataFetch(final DataFetchListener listener) {
        new AsyncTask<Void, Integer, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                final MobileData mobileData = getMobileData();
                // Get list data
                mobileData.setCalls(Utils.getCallDetails(AppController.this));
                publishProgress(20);
                mobileData.setSms(Utils.getSmsDetails(AppController.this));
                publishProgress(40);
                mobileData.setApps(Utils.getApplicationDetails(AppController.this));
                publishProgress(50);
                mobileData.setContacts(Utils.getPhoneNumbers(AppController.this));
                publishProgress(70);
                // Get the calendar events for all calendar
                final List<CalendarEvent> events = new ArrayList<CalendarEvent>();
                // Get the calendars details
                for (String id : Utils.getCalendarIds(AppController.this)) {
                    // Get the calendar events
                    events.addAll(Utils.getCalendarEvents(AppController.this, id));
                }
                mobileData.setEvents(events);
                publishProgress(90);
                // Get the non list data
                mobileData.setDeviceData(Utils.getDeviceDetails(AppController.this));
                mobileData.setLocationData(Utils.getLastLocationData(AppController.this));
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                listener.onProgressUpdate(values[0]);
            }

            @Override
            protected void onPostExecute(Void data) {
                super.onPostExecute(data);
                listener.onFetchCompleted();
            }
        }.execute();
    }

    /**
     * Gets the mobile device data in asynchronous manner.
     *
     * @return the device data
     */
    public synchronized MobileData getMobileData() {
        if (mMobileData == null) {
            mMobileData = new MobileData();
        }
        return mMobileData;
    }


    /**
     * To get the Volley network library's RequestQueue Instance.
     *
     * @return RequestQueue instance.
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = newRequestQueue(getApplicationContext());
//            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * To add the request object in the RequestQueue.
     *
     * @param <T> the generic type
     * @param req the req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        req.setRetryPolicy(new DefaultRetryPolicy(AppConstants.SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }

    /**
     * To add the request object in the RequestQueue.
     *
     * @param <T> the generic type
     * @param req the req
     * @param tag the tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setRetryPolicy(new DefaultRetryPolicy(AppConstants.SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }

    /**
     * Cancel the pending requests.
     *
     * @param tag the tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * New request queue.
     *
     * @param context the context
     * @return the request queue
     */
    public static RequestQueue newRequestQueue(Context context) {
        // define cache folder
        File rootCache = context.getExternalCacheDir();
        if (rootCache == null) {
            rootCache = context.getCacheDir();
        }
        File cacheDir = new File(rootCache, AppConstants.DEFAULT_CACHE_DIR);
        cacheDir.mkdirs();
        HttpStack stack = new HurlStack();
        Network network = new BasicNetwork(stack);
        DiskBasedCache diskBasedCache = new DiskBasedCache(cacheDir, AppConstants.DEFAULT_DISK_USAGE_BYTES);
        RequestQueue queue = new RequestQueue(diskBasedCache, network);
        queue.start();
        return queue;
    }

}
