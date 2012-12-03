package com.android.demo.eat;

import java.util.List;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

public class Map extends MapActivity {

	private double latitude;
	private double longitude;
	private String placeName;
	private String address;
	private MapController controller;
	private MyLocationOverlay myLocationOverlay;
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.map_layout);
		Intent intent = getIntent();
		placeName = intent.getStringExtra("name");
		address = intent.getStringExtra("address");
		float rating = intent.getFloatExtra("rating", 0);
		latitude = intent.getDoubleExtra("latitude", 0);
		longitude = intent.getDoubleExtra("longitude", 0);
		
		TextView titleView = (TextView) findViewById(R.id.place_name);
		titleView.setText(placeName);
		
		TextView addrView = (TextView) findViewById(R.id.place_address);
		addrView.setText(address);
		
		TextView ratingView = (TextView) findViewById(R.id.rating);
		ratingView.setText("" + rating);
		
		MapView map = (MapView) findViewById(R.id.map);
		
		double doubleLat = latitude * 1E6;
		double doubleLng = longitude * 1E6;
		GeoPoint point = new GeoPoint((int)doubleLat, (int)doubleLng);
		controller = map.getController();
		controller.animateTo(point);
		controller.setZoom(17);
		
		List<Overlay> overlays = map.getOverlays();
		myLocationOverlay = new MyLocationOverlay(Map.this, map);
		if (!myLocationOverlay.isMyLocationEnabled()) {
			myLocationOverlay.enableMyLocation();
		}
		overlays.add(myLocationOverlay);
		LocationOverlay locationOverlay = new LocationOverlay(Map.this, latitude, longitude);
		overlays.add(locationOverlay);
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (myLocationOverlay != null) {
			myLocationOverlay.disableMyLocation();
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.map_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.menu_my_location) {
			GeoPoint point = myLocationOverlay.getMyLocation();
			if (point != null) {
				controller.animateTo(point);
			}
		} else if (id == R.id.menu_search) {
			Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
	    	intent.putExtra(SearchManager.QUERY, placeName + " " + address);
			startActivity(intent);
		}
		return true;
	}
	
}