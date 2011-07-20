package org.stackednotion.httpserver.adapters;

import java.util.ArrayList;

import org.stackednotion.httpserver.Settings;
import org.stackednotion.httpserver.SmsBroadcastReceiver;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony.Sms;
import android.telephony.SmsManager;
import android.util.Log;

public class SmsAdapter {
	private static final Uri QUEUED_SMSS_URI = Uri.withAppendedPath(
			Sms.CONTENT_URI, "queued");


	public static void sendSms(Uri uri) {
		// dispatch send event
		Log.v("HttpServer", "Queued sms: " + uri);
		Settings.getContext().sendBroadcast(
				new Intent(SmsBroadcastReceiver.SMS_SEND_QUEUED_ACTION, null, Settings
						.getContext(), SmsBroadcastReceiver.class));
	}
	
	public static Uri sendSms(String destination, String body) {
		if (destination == null || body == null || destination.length() == 0
				|| body.length() == 0) {
			throw new IllegalArgumentException();
		}

		// write sms to queued smss
		ContentValues values = new ContentValues();
		values.put(Sms.ADDRESS, destination);
		values.put(Sms.BODY, body);
		Uri uri = Settings.getContext().getContentResolver().insert(
				QUEUED_SMSS_URI, values);

		// dispatch send event
		sendSms(uri);

		return uri;
	}

	public static Uri resendSms(String id) {
		Uri uri = Uri.withAppendedPath(Sms.CONTENT_URI, id);
		Cursor cursor = Settings.getContext().getContentResolver().query(uri,
				null, Sms.TYPE + " = " + Sms.MESSAGE_TYPE_FAILED, null, null);
		if (cursor.moveToFirst()) {
			// queue sms, then send
			Sms.moveMessageToFolder(Settings.getContext(), uri,
					Sms.MESSAGE_TYPE_QUEUED, 0);
			sendSms(uri);
			return uri;
		} else {
			return null;
		}
	}

	public static void sendQueuedSms() {
		// get oldest queued sms
		Cursor cursor = Settings.getContext().getContentResolver().query(QUEUED_SMSS_URI,
				null, null, null, "date ASC");
		if (cursor.moveToFirst() == false) {
			Log.v("HttpServer", "No more smss to send");
			return;
		}

		int id = cursor.getInt(cursor.getColumnIndex(Sms._ID));
		Uri uri = Uri.withAppendedPath(Sms.CONTENT_URI, String.valueOf(id));
		String destination = cursor.getString(
				cursor.getColumnIndex(Sms.ADDRESS)).replaceAll(" ", "");
		String body = cursor.getString(cursor.getColumnIndex(Sms.BODY));

		// prepare to send
		SmsManager manager = SmsManager.getDefault();
		ArrayList<String> smss = manager.divideMessage(body);
		Sms.moveMessageToFolder(Settings.getContext(), uri, Sms.MESSAGE_TYPE_OUTBOX, 0);

		// setup intents so we know whether sms delivery fails
		int smsCount = smss.size();
		ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>(
				smsCount);
		for (int i = 0; i < smsCount; i++) {
			Intent sendIntent = new Intent(
					SmsBroadcastReceiver.SMS_SENT_ACTION, uri, Settings
							.getContext(), SmsBroadcastReceiver.class);

			int requestCode = 0;
			if (i == smsCount - 1) {
				// Changing the requestCode so that a different pending intent
				// is created for the last fragment with
				// EXTRA_SMS_SENT_SEND_NEXT set to true.
				requestCode = 1;
				sendIntent.putExtra(
						SmsBroadcastReceiver.EXTRA_SMS_SENT_SEND_NEXT,
						true);
			}
			sentIntents.add(PendingIntent.getBroadcast(Settings.getContext(), requestCode,
					sendIntent, 0));
		}

		// send the sms
		try {
			manager.sendMultipartTextMessage(destination, null, smss,
					sentIntents, null);
			Log.v("HttpServer", "Sms dispatched: " + uri.toString());
		} catch (Exception e) {
			Log.v("HttpServer", "Error sending sms: " + uri.toString()
					+ " Exception: " + e.getClass().getName() + ":"
					+ e.getMessage());
		}
	}
	


}
