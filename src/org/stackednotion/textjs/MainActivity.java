package org.stackednotion.textjs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// update settings with current context
		Settings.init(getApplicationContext());

		if (Settings.skipIntroduction()) {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
		} else {
			Intent intent = new Intent(this, Introduction1Activity.class);
			startActivity(intent);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		// update settings with current context
		Settings.init(getApplicationContext());
	}
}
