package org.stackednotion.textjs.rosebud;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.PowerManager;
import android.preference.PreferenceManager;

public class Settings {
	public static final String LOG_TAG = "TextJs";
	public static final String WAKELOCK_TAG = "TextJs";
	public static final long WAKELOCK_TIMEOUT = 10000;
	public static final int PORT = 5982;

	private static Analytics analytics = null;
	private static PowerManager.WakeLock wakeLock = null;
	private static Context context = null;

	public static void init(Context applicationContext) {
		context = applicationContext;
		
		// start service
		if (isWifiConnected() && startOnWifi()) {
			ServerService.startService();
		}
		
		if (analytics == null) {
			analytics = new Analytics(applicationContext);
		}
	}

	public static Context getContext() {
		return context;
	}
	
	public static void analyticsEvent(String name) {
		if (analytics != null) {
			analytics.event(name);
		}
	}

	public static String getAccessCode() {
		return "1234";
	}

	public static boolean startOnWifi() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPreferences.getBoolean("start_on_wifi", true);
	}

	public static boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager) Settings.getContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (ServerService.class.getName().equals(
					service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean skipIntroduction() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPreferences.getBoolean("skip_introduction", false);
	}
	
	public static void setSkipIntroduction() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getContext());
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("skip_introduction", true);
		editor.commit();
	}

	public static void wakeUpDevice() {
		if (wakeLock == null) {
			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_TAG);
		}
		wakeLock.acquire(WAKELOCK_TIMEOUT);
	}
	
	public static boolean isWifiConnected() {
		ConnectivityManager connectivityManager = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
	}
	
	public static String getAddress() {
		String ip = Network.getLocalIpAddress();
		return "http://" + ip + ":" + String.valueOf(PORT) + "/ ";
	}
}