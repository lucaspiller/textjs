package org.stackednotion.httpserver;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity {
	private OnSharedPreferenceChangeListener preferenceListener;

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
				if (key.equals("service_enabled")) {
					// state is enabled
					if (sharedPreferences.getBoolean(key, false)) {
						// start service
						ServerService.startService();
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

		// if the service is enabled fire an intent to start it, if
		// it isn't already
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (sharedPreferences.getBoolean("service_enabled", false)) {
			// start service
			ServerService.startService();
		}

		// listen to sharedPreferences changes
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
	
	/*@Override
	public void onResume() {
		Settings.init(getApplicationContext());
		
		//String ip = Network.getLocalIpAddress();
		//String port = "8080"; // TODO
		//String authCode = "1234"; //String.valueOf(Settings.getAccessCode()); 
		
		//TextView ipText = (TextView) findViewById(R.id.ip_text);
		//ipText.setText("http://" + ip + ":" + port + "/");
		
		//TextView authText = (TextView) findViewById(R.id.auth_text);
		//authText.setText(authCode);

		super.onResume();
	}*/
}