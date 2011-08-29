package org.stackednotion.httpserver;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {
	public static final String LOG_TAG = "HttpServer";

	private static Context context;

	public static void init(Context applicationContext) {
		context = applicationContext;
	}

	public static Context getContext() {
		return context;
	}

	public static String getAccessCode() {
		return "1234";
	}

	public static boolean startOnWifi() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPreferences.getBoolean("start_on_wifi", false);
	}

	public static boolean isServiceRunning() {
	    ActivityManager manager = (ActivityManager) Settings.getContext().getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (ServerService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
}