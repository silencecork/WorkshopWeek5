package com.android.demo.game;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;

public class Region extends ToggleButton {
	
	private boolean isCircle;
	
	public Region(Context context) {
		super(context);
		init();
	}
	
	public Region(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public Region(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		setTextOff("");
		setTextOn("");
		setText("");
		setBackgroundResource(R.drawable.square);
	}
	
	public boolean isCircle() {
		return isCircle;
	}
	
	public void setRegionToCircle(boolean circle) {
		isCircle = circle;
		if (circle) {
			setBackgroundResource(R.drawable.circle_side);
		} else {
			setBackgroundResource(R.drawable.block_side);
		}
	}

	@Override
	public void setChecked(boolean checked) {
		if (!checked) {
			return;
		}
		super.setChecked(checked);
	}

	public void reset() {
		super.setChecked(false);
		isCircle = false;
	}

}
