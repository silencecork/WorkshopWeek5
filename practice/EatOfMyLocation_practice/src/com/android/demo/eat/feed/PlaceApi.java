package com.android.demo.eat.feed;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class PlaceApi {
    
    private static final String TAG = "PlaceApi";
    private static final String API_KEY = "AIzaSyC6YUr9nhUCTB1YiCSQgAvFtltrY50EaUM";
    private static final Uri SEARCH_BASE_URL = Uri.parse("https://maps.googleapis.com/maps/api/place/search/json");
    private static final Uri DETAIL_BASE_URI = Uri.parse("https://maps.googleapis.com/maps/api/place/details/json");
    
    
    /* support type: http://code.google.com/intl/fr/apis/maps/documentation/places/supported_types.html */
    public Place[] search(double latitude, double longitude, int radius, String type) throws IOException {
    	// enter your code here
    	// You can use Utils.streamToString() make InputStream turn to String
        return null;
    }
    
    public static String detailInfo(String reference, boolean chinese) throws IOException {
        if (TextUtils.isEmpty(API_KEY)) {
            throw new NullPointerException("API_KEY can not be null");
        }
        if (TextUtils.isEmpty(reference)) {
            throw new NullPointerException("reference can not be null");
        }
        
        Uri.Builder builder = DETAIL_BASE_URI.buildUpon();
        builder.appendQueryParameter("reference", reference);
        builder.appendQueryParameter("sensor", "false");
        if (chinese) {
            builder.appendQueryParameter("language", "zh-TW");
        }
        builder.appendQueryParameter("key", API_KEY);
        
        return Util.get(builder.build().toString());
    }
    
    
}

