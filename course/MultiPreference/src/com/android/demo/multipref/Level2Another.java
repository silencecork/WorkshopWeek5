package com.android.demo.multipref;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Level2Another extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.level2_another);
	}
	
}
