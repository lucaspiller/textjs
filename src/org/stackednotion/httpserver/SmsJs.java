package org.stackednotion.httpserver;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SmsJs extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	@Override
	public void onResume() {
		ServerService.startService(this);
		
		Settings.init(getApplicationContext());
		String ip = Network.getLocalIpAddress();
		String port = "8080"; // TODO
		String authCode = "1234"; //String.valueOf(Settings.getAccessCode()); 
		
		TextView ipText = (TextView) findViewById(R.id.ip_text);
		ipText.setText("http://" + ip + ":" + port + "/");
		
		TextView authText = (TextView) findViewById(R.id.auth_text);
		authText.setText(authCode);

		super.onResume();
	}
}