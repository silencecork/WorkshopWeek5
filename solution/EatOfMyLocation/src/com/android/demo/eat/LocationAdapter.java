package com.android.demo.eat;

import com.android.demo.eat.feed.Place;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LocationAdapter extends BaseAdapter {
	
	private Place[] places;
	private LayoutInflater inflater;
	
	public LocationAdapter(Context c, Place[] p) {
		places = p;
		inflater = LayoutInflater.from(c);
	}
	
	@Override
	public int getCount() {
		return (places != null) ? places.length : 0;
	}

	@Override
	public Object getItem(int pos) {
		return (places != null) ? places[pos] : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item, null);
		}
		
		Place place = (Place) getItem(position);
		if (place == null) {
			return null;
		}
		
		TextView placeName = (TextView) convertView.findViewById(R.id.place_name);
		TextView placeAddress = (TextView) convertView.findViewById(R.id.place_address);
		TextView placeRating = (TextView) convertView.findViewById(R.id.rating);
		
		placeName.setText(place.name);
		placeAddress.setText(place.vicinity);
		placeRating.setText(String.valueOf(place.rating));
		
		return convertView;
	}

}
