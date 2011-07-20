package org.stackednotion.httpserver.sms;

import java.util.ArrayList;

import org.stackednotion.httpserver.Settings;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony.Sms;
import android.telephony.SmsManager;
import android.util.Log;

public class Sender {
	private static final Uri QUEUED_MESSAGES_URI = Uri.withAppendedPath(
			Sms.CONTENT_URI, "queued");

	public static Uri send(String destination, String body) {
		if (destination == null || body == null || destination.length() == 0
				|| body.length() == 0) {
			throw new IllegalArgumentException();
		}

		// write sms to queued messages
		ContentValues values = new ContentValues();
		values.put(Sms.ADDRESS, destination);
		values.put(Sms.BODY, body);
		Uri uri = Settings.getContext().getContentResolver().insert(
				QUEUED_MESSAGES_URI, values);

		// dispatch send event
		sendMessage(uri);

		return uri;
	}

	public static Uri resendMessage(String id) {
		Uri uri = Uri.withAppendedPath(Sms.CONTENT_URI, id);
		Cursor cursor = Settings.getContext().getContentResolver().query(uri,
				null, Sms.TYPE + " = " + Sms.MESSAGE_TYPE_FAILED, null, null);
		if (cursor.moveToFirst()) {
			// queue message, then send
			Sms.moveMessageToFolder(Settings.getContext(), uri, Sms.MESSAGE_TYPE_QUEUED, 0);
			sendMessage(uri);
			return uri;
		} else {
			return null;
		}
	}

	public static void sendMessage(Uri uri) {
		// dispatch send event
		Log.v("HttpServer", "Queued message: " + uri);
		Settings.getContext().sendBroadcast(
				new Intent(SenderBroadcastReceiver.MESSAGE_SEND_QUEUED_ACTION,
						null, Settings.getContext(),
						SenderBroadcastReceiver.class));
	}

	public static void handleSendQueuedMessage(Context context, Intent intent) {
		// get oldest queued message
		Cursor cursor = context.getContentResolver().query(QUEUED_MESSAGES_URI,
				null, null, null, "date ASC");
		if (cursor.moveToFirst() == false) {
			Log.v("HttpServer", "No more messages to send");
			return;
		}

		int id = cursor.getInt(cursor.getColumnIndex(Sms._ID));
		Uri uri = Uri.withAppendedPath(Sms.CONTENT_URI, String.valueOf(id));
		String destination = cursor.getString(
				cursor.getColumnIndex(Sms.ADDRESS)).replaceAll(" ", "");
		String body = cursor.getString(cursor.getColumnIndex(Sms.BODY));

		// prepare to send
		SmsManager manager = SmsManager.getDefault();
		ArrayList<String> messages = manager.divideMessage(body);
		Sms.moveMessageToFolder(context, uri, Sms.MESSAGE_TYPE_OUTBOX, 0);

		// setup intents so we know whether message delivery fails
		int messageCount = messages.size();
		ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>(
				messageCount);
		for (int i = 0; i < messageCount; i++) {
			Intent sendIntent = new Intent(
					SenderBroadcastReceiver.MESSAGE_SENT_ACTION, uri, Settings
							.getContext(), SenderBroadcastReceiver.class);

			int requestCode = 0;
			if (i == messageCount - 1) {
				// Changing the requestCode so that a different pending intent
				// is created for the last fragment with
				// EXTRA_MESSAGE_SENT_SEND_NEXT set to true.
				requestCode = 1;
				sendIntent.putExtra(
						SenderBroadcastReceiver.EXTRA_MESSAGE_SENT_SEND_NEXT,
						true);
			}
			sentIntents.add(PendingIntent.getBroadcast(context, requestCode,
					sendIntent, 0));
		}

		// send the message
		try {
			manager.sendMultipartTextMessage(destination, null, messages,
					sentIntents, null);
			Log.v("HttpServer", "Message dispatched: " + uri.toString());
		} catch (Exception e) {
			Log.v("HttpServer", "Error sending message: " + uri.toString()
					+ " Exception: " + e.getClass().getName() + ":"
					+ e.getMessage());
		}
	}
}
