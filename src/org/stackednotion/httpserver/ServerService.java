package org.stackednotion.httpserver;

import org.stackednotion.httpserver.server.ServerApplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class ServerService extends Service {
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Settings.init(getApplicationContext());
		ServerApplication.startServer(8080);
		
		Toast
		.makeText(this, R.string.server_service_started,
				Toast.LENGTH_SHORT).show();
		
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		ServerApplication.stopServer();

		// Tell the user we stopped.
		Toast
				.makeText(this, R.string.server_service_stopped,
						Toast.LENGTH_SHORT).show();
	}

}
