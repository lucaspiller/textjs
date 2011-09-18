package org.stackednotion.textjs;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity {
	private OnSharedPreferenceChangeListener preferenceListener;
	private CheckBoxPreference mServiceStartedPreference;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.preferences_view);

		// create preference listener
		preferenceListener = new OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(
					SharedPreferences sharedPreferences, String key) {
				// start the service when checked
				// user clicked enable service
				if (key.equals("service_should_start")) {
					// state is enabled
					if (sharedPreferences.getBoolean(key, false)) {
						// start service
						ServerService.startService();
					} else {
						// stop service
						ServerService.stopService();
					}
				}
			}
		};
	}

	@Override
	public void onResume() {
		super.onResume();

		// update settings with current context
		Settings.init(getApplicationContext());
		
		// update started preference based on whether the service is started
		mServiceStartedPreference = (CheckBoxPreference) findPreference("service_should_start");
		mServiceStartedPreference.setChecked(Settings.isServiceRunning());

		// listen to sharedPreferences changes
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		sharedPreferences
				.registerOnSharedPreferenceChangeListener(preferenceListener);
	}

	@Override
	public void onPause() {
		// stop listening to sharedPreferences changes
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		sharedPreferences
				.unregisterOnSharedPreferenceChangeListener(preferenceListener);

		super.onPause();
	}
}