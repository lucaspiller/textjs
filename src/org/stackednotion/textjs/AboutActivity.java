package org.stackednotion.textjs;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// update settings with current context
		Settings.init(getApplicationContext());

		setContentView(R.layout.about_view);

		TextView versionView = (TextView) findViewById(R.id.version);
		try {
			String version = getPackageManager().getPackageInfo(
					this.getPackageName(), 0).versionName;
			versionView.setText(version);
		} catch (NameNotFoundException e) {
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		// update settings with current context
		Settings.init(getApplicationContext());
	}
}
