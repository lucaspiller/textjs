package org.stackednotion.httpserver;

import org.stackednotion.httpserver.server.ServerApplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class ServerService extends Service {
	private static final int NOTIFICATION_ID = 43429;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void startService() {
		Context context = Settings.getContext();
		Intent service = new Intent(context, ServerService.class);
		context.startService(service);
	}
	
	public static void stopService() {
		Context context = Settings.getContext();
		Intent service = new Intent(context, ServerService.class);
		context.stopService(service);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Settings.init(getApplicationContext());
		ServerApplication.startServer(8080);

		startForeground(NOTIFICATION_ID, createServiceNotification());
		return START_STICKY;
	}

	private Notification createServiceNotification() {
		Context context = Settings.getContext();
		Notification notification = new Notification(R.drawable.stat_service, null, 0);
		
		String ip = Network.getLocalIpAddress();
		String port = "8080"; 
		String url = "http://" + ip + ":" + port + "/ ";

		CharSequence contentTitle = context.getText(R.string.notification_title);
		CharSequence contentText = context.getText(R.string.notification_text_pre) + url + context.getText(R.string.notification_text_post);
		Intent notificationIntent = new Intent(this, SettingsActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		notification.setLatestEventInfo(Settings.getContext(), contentTitle, contentText, contentIntent);
		
		return notification;
	}

	@Override
	public void onDestroy() {
		ServerApplication.stopServer();
		
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager notificationManager = (NotificationManager) getSystemService(ns);
		notificationManager.cancel(NOTIFICATION_ID);
	}

}
