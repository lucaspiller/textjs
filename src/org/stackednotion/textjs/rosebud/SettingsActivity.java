package org.stackednotion.textjs.rosebud;

import org.stackednotion.textjs.rosebud.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class SettingsActivity extends PreferenceActivity {
	private OnSharedPreferenceChangeListener preferenceListener;
	private CheckBoxPreference mServiceStartedPreference;
	
	private Preference aboutPreference;
	private Preference introductionPreference;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// update settings with current context
		Settings.init(getApplicationContext());
		
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
		
		addPreferencesFromResource(R.layout.preferences_view);
		introductionPreference = findPreference("introduction");
		aboutPreference = findPreference("about");
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
	
	@Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            Preference preference) {
        if (preference == introductionPreference) {
        	Intent intent = new Intent(this, Introduction1Activity.class);
			startActivity(intent);
        } else if (preference == aboutPreference) {
        	Intent intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}