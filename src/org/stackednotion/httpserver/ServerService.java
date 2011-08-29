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

	public static void startService(Context context) {
		Intent service = new Intent(context, ServerService.class);
		context.startService(service);
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
		CharSequence tickerText = context.getText(R.string.notification_ticker);
		Notification notification = new Notification(R.drawable.icon, tickerText, 0);

		CharSequence contentTitle = context.getText(R.string.notification_title);
		CharSequence contentText = context.getText(R.string.notification_text);
		Intent notificationIntent = new Intent(this, SmsJs.class);
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
