package org.stackednotion.textjs.rosebud;

import org.stackednotion.textjs.rosebud.R;
import org.stackednotion.textjs.rosebud.server.ServerApplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;

public class ServerService extends Service {
	private static final int NOTIFICATION_ID = 43429;
	private static final String WIFI_LOCK_TAG = "HttpServer/ServerService";
	private WifiManager.WifiLock mWifiLock;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void startService() {
		if (!Settings.isServiceRunning()) {
			Context context = Settings.getContext();
			Intent service = new Intent(context, ServerService.class);
			context.startService(service);
			Settings.analyticsEvent("ServerService Start");
		}
	}

	public static void stopService() {
		if (Settings.isServiceRunning()) {
			Context context = Settings.getContext();
			Intent service = new Intent(context, ServerService.class);
			context.stopService(service);
			Settings.analyticsEvent("ServerService Stop");
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Settings.init(getApplicationContext());
		obtainWifiLock();
		ServerApplication.startServer(Settings.PORT);
		startForeground(NOTIFICATION_ID, createServiceNotification());
		return START_STICKY;

	}

	@Override
	public void onDestroy() {
		ServerApplication.stopServer();
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager notificationManager = (NotificationManager) getSystemService(ns);
		notificationManager.cancel(NOTIFICATION_ID);
		releaseWifiLock();
	}
	
	private void obtainWifiLock() {
		WifiManager wm = (WifiManager) Settings.getContext().getSystemService(Context.WIFI_SERVICE);
        mWifiLock = wm.createWifiLock(WIFI_LOCK_TAG);
        if(!mWifiLock.isHeld()){
            mWifiLock.acquire();
        }
	}
	
	private void releaseWifiLock() {
		if (mWifiLock != null) {
			mWifiLock.release();
		}
	}
	
	private Notification createServiceNotification() {
		Context context = Settings.getContext();
		Notification notification = new Notification(R.drawable.stat_service,
				null, 0);

		CharSequence contentTitle = context
				.getText(R.string.notification_title);
		CharSequence contentText = context
				.getText(R.string.notification_text_pre)
				+ Settings.getAddress()
				+ context.getText(R.string.notification_text_post);
		Intent notificationIntent = new Intent(this, SettingsActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		notification.setLatestEventInfo(Settings.getContext(), contentTitle,
				contentText, contentIntent);

		return notification;
	}

}
