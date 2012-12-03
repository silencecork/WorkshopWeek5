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
        if (TextUtils.isEmpty(API_KEY)) {
            Log.e(TAG, "API_KEY can not null");
            throw new NullPointerException("API_KEY can not null");
        }
        
        Uri.Builder builder = SEARCH_BASE_URL.buildUpon();
        
        builder.appendQueryParameter("location", latitude + "," + longitude);
        builder.appendQueryParameter("radius", Integer.toString(radius));
        
        if (!TextUtils.isEmpty(type)) {
            builder.appendQueryParameter("types", type);
        }
        
        /*if(!TextUtils.isEmpty(detailName)) {
            builder.appendQueryParameter("name", detailName);
        }*/
        
        builder.appendQueryParameter("language", "zh-TW");
        
        builder.appendQueryParameter("sensor", "false");
        builder.appendQueryParameter("key", API_KEY);
        
        String data = Util.get(builder.build().toString());
        if (data != null) {
            try {
                JSONObject json = new JSONObject(data);
                JSONArray array = json.getJSONArray("results");
                int count = array.length();
                ArrayList<Place> list = new ArrayList<Place>();
                for (int i = 0; i < count; i++) {
                    JSONObject entry = array.getJSONObject(i);
                    String id = entry.getString("id");
                    String icon = entry.getString("icon");
                    String name = entry.getString("name");
                    String reference = entry.getString("reference");
                    String vicinity = entry.getString("vicinity");
                   
                    float rating = 0;
                    if (entry.has("rating")) {
	                    String strRating = entry.getString("rating");
	                    if (!TextUtils.isEmpty(strRating)) {
	                    	rating = Float.parseFloat(strRating);
	                    }
                    }
                    JSONObject geoObj = entry.getJSONObject("geometry");
                    double lat = -1;
                    double lng = -1;
                    if (geoObj != null) {
                        JSONObject locationObj = geoObj.getJSONObject("location");
                        if (locationObj != null) {
                            String strLat = locationObj.getString("lat");
                            String strLng = locationObj.getString("lng");
                            lat = Double.parseDouble(strLat);
                            lng = Double.parseDouble(strLng);
                        }
                    }
                    Place place = new Place();
                    place.id = id;
                    place.name = name;
                    place.iconUrl = icon;
                    place.latitude = lat;
                    place.longitude = lng;
                    place.vicinity = vicinity;
                    place.reference = reference;
                    place.rating = rating;
                    list.add(place);
                }
                if (list.size() > 0) {
                	Place places[] = new Place[list.size()];
                	list.toArray(places);
                	return places;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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

