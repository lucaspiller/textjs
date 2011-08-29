package org.stackednotion.httpserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class AutoStarter extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// init settings from context
		Settings.init(context);
		
		SharedPreferences sharedPreferences = PreferenceManager
		.getDefaultSharedPreferences(context);
		
		// check if start on boot is enabled
		if (sharedPreferences.getBoolean("start_on_boot", false))
		{
			// check if service is enabled	
			if (sharedPreferences.getBoolean("service_enabled", false))
			{
				// start service
				ServerService.startService();
			} else {
				Log.d("HttpServer", "Service not enabled");
			}
		} else {
			Log.d("HttpServer", "Start on boot not enabled");
		}
	}
}
