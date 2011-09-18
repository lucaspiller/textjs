package org.stackednotion.textjs;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.preference.PreferenceManager;

public class Settings {
	public static final String LOG_TAG = "HttpServer";
	public static final String WAKELOCK_TAG = "HttpServer";
	public static final long WAKELOCK_TIMEOUT = 10000;

	private static PowerManager.WakeLock wakeLock = null;
	private static Context context = null;

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

	public static void wakeUpDevice() {
		if (wakeLock == null) {
			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_TAG);
		}
		wakeLock.acquire(WAKELOCK_TIMEOUT);
	}
}