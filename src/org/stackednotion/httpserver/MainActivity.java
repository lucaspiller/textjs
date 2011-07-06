package org.stackednotion.httpserver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	@Override
	public void onResume() {
		Intent service = new Intent(this, ServerService.class);
		startService(service);
		
		Settings.init(getApplicationContext());
		String ip = Network.getLocalIpAddress().replace(".", ".\n");
		
		TextView statusText = (TextView) findViewById(R.id.status_text);
		statusText.setText(ip);

		super.onResume();
	}
}