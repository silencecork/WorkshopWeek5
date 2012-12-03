package com.android.demo.eat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class LocationOverlay extends Overlay {

	private double latitude;
	private double longitude;
	private Bitmap d;
	
	public LocationOverlay(Context context, double lati, double longi) {
		latitude = lati * 1E6;
		longitude = longi * 1E6;
		d = BitmapFactory.decodeResource(context.getResources(), R.drawable.pin);
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		Projection projection = mapView.getProjection();
		Point point = new Point();
	    GeoPoint geoPoint = new GeoPoint((int)latitude, (int)longitude);
	    projection.toPixels(geoPoint, point);
	    
	    canvas.drawBitmap(d, point.x, point.y, null);
	}

}
