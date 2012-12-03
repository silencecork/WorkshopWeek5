package com.android.demo.multipref;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class Main extends PreferenceActivity {
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.level1);
        
        Preference pref1 = findPreference(getString(R.string.level1_pref1_key));
        pref1.setOnPreferenceClickListener(onPrefClick);
        Preference pref2 = findPreference(getString(R.string.level1_pref2_key));
        pref2.setOnPreferenceClickListener(onPrefClick);
    }
    
    private OnPreferenceClickListener onPrefClick = new OnPreferenceClickListener() {

		@Override
		public boolean onPreferenceClick(Preference preference) {
			if (preference.getKey().equals(getString(R.string.level1_pref1_key))) {
				Intent intent = new Intent();
				intent.setClassName("com.android.demo.multipref", "com.android.demo.multipref.Level2");
				startActivity(intent);
			} else if (preference.getKey().equals(getString(R.string.level1_pref2_key))) {
				Intent intent = new Intent();
				intent.setClassName("com.android.demo.multipref", "com.android.demo.multipref.Level2Another");
				startActivity(intent);
			}
			return true;
		}
    	
    };
}