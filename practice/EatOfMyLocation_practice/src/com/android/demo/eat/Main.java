package com.android.demo.eat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.android.demo.eat.feed.Place;
import com.android.demo.eat.feed.PlaceApi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class Main extends Activity {
	
	
	private PlaceApi place;
	private LocationManager locationManager;
	private String locationProvider;
	private ProgressDialog progress;
	private AsyncTask<Double, Void, String> getAddressTask;
	private AsyncTask<Double, Void, Place[]> getPlaceTask;
	private LocationAdapter adapter;
	private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        locationProvider = initLocationManager();
        loadCurrentLocation();
        place = new PlaceApi();
        
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(listener);
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	if (locationManager != null) {
    		locationManager.removeUpdates(locationListener);
    	}
    	if (getAddressTask != null) {
    		getAddressTask.cancel(true);
    	}
    	if (getPlaceTask != null) {
    		getPlaceTask.cancel(true);
    	}
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.menu_refresh) {
			loadCurrentLocation();
		}
		return true;
	}
    
    private String initLocationManager() {
    	locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return locationManager.getBestProvider(criteria, true);
    }
    
    private void loadCurrentLocation() {
    	progress = new ProgressDialog(Main.this);
    	progress.setMessage(getString(R.string.wait_load_location));
    	progress.show();
    	locationManager.requestLocationUpdates(locationProvider, 2000, 10, locationListener);
    }
    
    private String getAddress(double latitude, double longitude) {
    	Geocoder gc = new Geocoder(Main.this, Locale.getDefault());
		try {
			List<Address> addrs = gc.getFromLocation(latitude, longitude, 1);
			StringBuilder sb = new StringBuilder();
			for (Address addr : addrs) {
                int index = addr.getMaxAddressLineIndex();
                sb.append(addr.getAddressLine(index));
            }
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    private LocationListener locationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			progress.dismiss();
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			
			getAddressTask = new AsyncTask<Double, Void, String>() {

				@Override
				protected void onPreExecute() {
					super.onPreExecute();
				}

				@Override
				protected String doInBackground(Double... params) {
					return getAddress(params[0], params[1]);
				}

				@Override
				protected void onPostExecute(String result) {
					super.onPostExecute(result);
					TextView text = (TextView) findViewById(R.id.my_loc);
					text.setText(result);
				}
				
			};
			getAddressTask.execute(latitude, longitude);
			
			
			getPlaceTask = new AsyncTask<Double, Void, Place[]>() {
				
				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					progress = new ProgressDialog(Main.this);
					progress.setMessage(getString(R.string.wait_load_place));
					progress.show();
				}

				@Override
				protected Place[] doInBackground(Double... params) {
					try {
						return place.search(params[0], params[1], 2000, "food");
					} catch (IOException e) {
						e.printStackTrace();
					}					
					return null;
				}
				
				@Override
				protected void onPostExecute(Place[] result) {
					super.onPostExecute(result);
					if (progress != null) {
						progress.dismiss();
					}
					adapter = new LocationAdapter(Main.this, result);
					listView.setAdapter(adapter);
				}
				
			};
			getPlaceTask.execute(latitude, longitude);
		}

		@Override
		public void onProviderDisabled(String provider) { }

		@Override
		public void onProviderEnabled(String provider) { }

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) { }
    	
    };
    
    private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
			Place place = (Place) adapter.getItem(pos);
			Intent intent = new Intent("com.android.demo.map.VIEW");
			intent.setClassName("com.android.demo.eat", "com.android.demo.eat.Map");
			intent.putExtra("name", place.name);
			intent.putExtra("address", place.vicinity);
			intent.putExtra("rating", place.rating);
			intent.putExtra("latitude", place.latitude);
			intent.putExtra("longitude", place.longitude);
			startActivity(intent);
		}
    	
    };
}