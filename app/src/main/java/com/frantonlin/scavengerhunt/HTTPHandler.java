package com.frantonlin.scavengerhunt;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Handles the HTTP requests
 * Created by Franton on 10/1/15.
 */
public class HTTPHandler {

    public static final String APPID = "olinscavenge";
    // The request queue for Volley
    public RequestQueue queue;

    /**
     * Constructor
     * @param context the context of the handler
     */
    public HTTPHandler(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    /**
     * Search the specified query from the specified page with a callback
     * @param callback the callback
     */
    public void getInfo(final InfoCallback callback, final int clueNumber) {
        String url = "http://45.55.65.113/scavengerhunt";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // we got a response, success!
                        try {
                            JSONArray items = response.getJSONArray("path");
                            HashMap<String, String> clueInfo = new HashMap<>();
//                            Log.d("HTTPHandler", String.valueOf(items));

                            for(int i = 0; i < items.length(); i++) {
                                JSONObject item = items.getJSONObject(i);
                                if (item.getInt("id") == clueNumber) {
                                    clueInfo.put("id", String.valueOf(clueNumber));
                                    clueInfo.put("s3id", item.getString("s3id"));
                                    clueInfo.put("latitude", item.getString("latitude"));
                                    clueInfo.put("longitude", item.getString("longitude"));
                                    clueInfo.put("numClues", String.valueOf(items.length()));
                                    break;
                                }
                            }
                            callback.callback(true, clueInfo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.callback(false, null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // we had an error, failure!
                        callback.callback(false, null);
                    }
                }
        );

        queue.add(request);
    }

    public void postInfo(final PostCallback callback, String imageKey, String imageNumber) {
        String url = "http://45.55.65.113/userdata/"+APPID;

        final JSONObject postObject = new JSONObject();
        try {
            postObject.put("imageKey", imageKey);
            postObject.put("imageLocation", imageNumber);
        } catch (JSONException e) {
            callback.callback(false);
        }

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // we got a response, success!
                        callback.callback(true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // we had an error, failure!
                        callback.callback(false);
                    }
                }
        ){
            @Override
            public byte[] getBody() throws AuthFailureError {
                return postObject.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        queue.add(request);
    }
}