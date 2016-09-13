/**
 * VolleyRequestHelper.java
 * <p/>
 * A rest client helper based on volley tool.
 *
 * @category Global Analytics
 * @package com.globalanalytics.dataapp.helper
 * @version 1.0
 * @author Rajkumar.N
 * @copyright Copyright (C) 2016 Global Analytics. All rights reserved.
 */
package com.oozmakappa.oyeloans.helper;

/**
 * @author Rajkumar.N
 */

import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.oozmakappa.oyeloans.DataExtraction.AppController;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * A helper class to executes the web service using the volley. Supported
 * Methods: GET and POST. By default it will be executed on POST method.
 */
public class VolleyRequestHelper {

    private static final String TAG = VolleyRequestHelper.class.getSimpleName();

    private OnRequestCompletedListener mRequestCompletedListener;

    public interface VolleyRequestConstants {
        public static final String HTTP_PARAMS = "Params";
        public static final String HTTP_HEADERS = "Headers";
        public static final String HTTP_BODY_CONTENT = "Content";
        public static final String HTTP_CONTENT_TYPE = "Content-type";
        public static final String HTTP_PRIORITY = "Priority";
    }

    /**
     * A callback interface indicates the status about the completion of HTTP
     * request.
     */
    public interface OnRequestCompletedListener {

        /**
         * Called when the JSON Object request has been completed.
         *
         * @param requestName  the String refers the request name
         * @param status       the status of the request either success or failure
         * @param response     the JSON Object returns from WebService Response. It may
         *                     be null if request failed.
         * @param errorMessage the String refers the error message when request failed to
         *                     get the response
         */
        public void onRequestCompleted(String requestName, boolean status,
                                       JSONObject response, String errorMessage);

        /**
         * Called when the JSON Array request has been completed.
         *
         * @param requestName  the String refers the request name
         * @param status       the status of the request either success or failure
         * @param response     the JSON Array returns from WebService Response. It may be
         *                     null if request failed.
         * @param errorMessage the String refers the error message when request failed to
         *                     get the response
         */
        public void onRequestCompleted(String requestName, boolean status,
                                       JSONArray response, String errorMessage);

        /**
         * Called when the String request has been completed.
         *
         * @param requestName  the String refers the request name
         * @param status       the status of the request either success or failure
         * @param response     the String response returns from the Webservice. It may be
         *                     null if request failed.
         * @param errorMessage the String refers the error message when request failed to
         *                     get the response
         */
        public void onRequestCompleted(String requestName, boolean status,
                                       String response, String errorMessage);

    }

    /**
     * Used to call web service and get response as JSON using post method.
     *
     * @param callback - The callback reference.
     */
    public VolleyRequestHelper(OnRequestCompletedListener callback) {
        mRequestCompletedListener = callback;
    }

    /**
     * Request JSON Object from the Web API.
     *
     * @param requestName   the String refers the request name
     * @param webserviceUrl the String refers the web service URL.
     * @param requestParams the list of request parameters in hash map including Http
     *                      Parameters, Headers, Contents and Content Type.
     * @param requestJson   the JSONObject indicates the request JSON object.
     * @param webMethod     the integer indicates the web method.
     * @param getCache      the boolean indicates whether cache can enable/disabl.e
     */
    public void requestJsonObject(final String requestName,
                                  final String webserviceUrl,
                                  final HashMap<Object, Object> requestParams,
                                  final JSONObject requestJson, final int webMethod,
                                  final boolean getCache) {
        final JsonObjectRequest jsonRequest = new JsonObjectRequest(webMethod,
                webserviceUrl, requestJson,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v(TAG, requestName + " Json Response: "
                                + response.toString());
                        mRequestCompletedListener.onRequestCompleted(
                                requestName, true, response, null);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Error Message
                String errorResponse = getVolleyErrorMessage(error);
                Log.v(TAG, requestName
                        + "Error Message: " + errorResponse);
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                @SuppressWarnings("unchecked")
                final HashMap<String, String> params = (HashMap<String, String>) requestParams
                        .get(VolleyRequestConstants.HTTP_PARAMS);
                if (params != null) {
                    return params;
                }
                return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                @SuppressWarnings("unchecked")
                final HashMap<String, String> headers = (HashMap<String, String>) requestParams
                        .get(VolleyRequestConstants.HTTP_HEADERS);
                if (headers != null) {
                    return headers;
                }
                return super.getHeaders();
            }

            @Override
            public Priority getPriority() {
                final Priority priority = (Priority) requestParams
                        .get(VolleyRequestConstants.HTTP_PRIORITY);
                if (priority != null) {
                    return priority;
                }
                return super.getPriority();
            }

            @Override
            public String getBodyContentType() {
                final String contentType = (String) requestParams
                        .get(VolleyRequestConstants.HTTP_CONTENT_TYPE);
                if (contentType != null) {
                    return contentType;
                }
                return super.getBodyContentType();
            }

            @Override
            public byte[] getBody() {
                byte[] body = null;
                try {
                    body = (requestParams
                            .get(VolleyRequestConstants.HTTP_BODY_CONTENT).toString()).getBytes();
                } catch (Exception e) {
                    Log.v(TAG, e.getMessage(), e);
                }
                if (body != null) {
                    return body;
                }
                return super.getBody();
            }
        };
        jsonRequest.setTag(requestName);
         //Adding JSON Object request to request queue
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

    /**
     * Request JSON Array from the Web API.
     *
     * @param requestName   the String refers the request name
     * @param webserviceUrl the String refers the web service URL.
     * @param requestParams the list of parameters in hash map.
     * @param getCache      the boolean indicates whether cache can enable/disabl.e
     */
    public void requestJsonArray(final String requestName,
                                 final String webserviceUrl,
                                 final HashMap<Object, Object> requestParams,
                                 final boolean getCache) {
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                webserviceUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.v(TAG, requestName + " Json Array Response: "
                        + response.toString());
                mRequestCompletedListener.onRequestCompleted(
                        requestName, true, response, null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                JSONArray responseJson = new JSONArray();
                String errorResponse = getVolleyErrorMessage(error);
                Log.v(TAG, requestName
                        + "Error Message: " + errorResponse);
                mRequestCompletedListener
                        .onRequestCompleted(requestName, false,
                                responseJson, errorResponse);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                @SuppressWarnings("unchecked")
                final HashMap<String, String> params = (HashMap<String, String>) requestParams
                        .get(VolleyRequestConstants.HTTP_PARAMS);
                if (params != null) {
                    return params;
                }
                return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                @SuppressWarnings("unchecked")
                final HashMap<String, String> headers = (HashMap<String, String>) requestParams
                        .get(VolleyRequestConstants.HTTP_HEADERS);
                if (headers != null) {
                    return headers;
                }
                return super.getHeaders();
            }

            @Override
            public Priority getPriority() {
                final Priority priority = (Priority) requestParams
                        .get(VolleyRequestConstants.HTTP_PRIORITY);
                if (priority != null) {
                    return priority;
                }
                return super.getPriority();
            }

            @Override
            public String getBodyContentType() {
                final String contentType = (String) requestParams
                        .get(VolleyRequestConstants.HTTP_CONTENT_TYPE);
                if (contentType != null) {
                    return contentType;
                }
                return super.getBodyContentType();
            }

            @Override
            public byte[] getBody() {
                final byte[] body = (byte[]) requestParams
                        .get(VolleyRequestConstants.HTTP_BODY_CONTENT);
                if (body != null) {
                    return body;
                }
                return super.getBody();
            }
        };
        jsonArrayRequest.setTag(requestName);
        // Adding JsonArray request to request queue
        //AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    /**
     * Request String response from the Web API.
     *
     * @param requestName   the String refers the request name
     * @param webserviceUrl the String refers the web service URL.
     * @param requestParams the list of parameters in hash map.
     * @param webMethod     the integer indicates the web method.
     * @param getCache      the boolean indicates whether cache can enable/disabl.e
     */
    public void requestString(final String requestName,
                              final String webserviceUrl,
                              final HashMap<Object, Object> requestParams, final int webMethod,
                              final boolean getCache) {
        StringRequest stringRequest = new StringRequest(webMethod,
                webserviceUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.v(TAG, requestName + " String Response Success: "
                        + response.toString());
                mRequestCompletedListener.onRequestCompleted(
                        requestName, true, response, null);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String response = null;
                String errorResponse = getVolleyErrorMessage(error);
                Log.v(TAG, requestName
                        + "Error Message: " + errorResponse);
                mRequestCompletedListener.onRequestCompleted(
                        requestName, false, response, errorResponse);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                @SuppressWarnings("unchecked")
                final HashMap<String, String> params = (HashMap<String, String>) requestParams
                        .get(VolleyRequestConstants.HTTP_PARAMS);
                if (params != null) {
                    return params;
                }
                return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                @SuppressWarnings("unchecked")
                final HashMap<String, String> headers = (HashMap<String, String>) requestParams
                        .get(VolleyRequestConstants.HTTP_HEADERS);
                if (headers != null) {
                    return headers;
                }
                return super.getHeaders();
            }

            @Override
            public Priority getPriority() {
                final Priority priority = (Priority) requestParams
                        .get(VolleyRequestConstants.HTTP_PRIORITY);
                if (priority != null) {
                    return priority;
                }
                return super.getPriority();
            }

            @Override
            public String getBodyContentType() {
                final String contentType = (String) requestParams
                        .get(VolleyRequestConstants.HTTP_CONTENT_TYPE);
                if (contentType != null) {
                    return contentType;
                }
                return super.getBodyContentType();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                final byte[] body = (byte[]) requestParams
                        .get(VolleyRequestConstants.HTTP_BODY_CONTENT);
                if (body != null) {
                    return body;
                }
                return super.getBody();
            }
        };
        stringRequest.setTag(requestName);
        // Adding String request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    /**
     * Gets the volley error message.
     *
     * @param error the VolleyError object.
     * @return the String refers the volley error message
     */
    public static String getVolleyErrorMessage(VolleyError error) {
        // Error Message
        String errorResponse = "";
        if (error instanceof NoConnectionError
                || error instanceof TimeoutError
                || error instanceof NetworkError) {
            errorResponse = "uanble to reach server";
        } else {
            try {
                VolleyError responseError = new VolleyError(
                        new String(error.networkResponse.data));
                errorResponse = responseError.getMessage();
            } catch (Exception e) {
                Log.v(TAG, e.getMessage(), e);
            }
        }
        return errorResponse;
    }
}
