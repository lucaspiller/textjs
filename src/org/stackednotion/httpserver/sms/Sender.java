package org.stackednotion.httpserver.sms;

import java.util.ArrayList;

import org.stackednotion.httpserver.Settings;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony.Sms;
import android.telephony.SmsManager;
import android.util.Log;

public class Sender {
	private static final Uri QUEUED_MESSAGES_URI = Uri
			.parse("content://sms/queued");

	private static final String ID_KEY = "_id";
	private static final String BODY_KEY = "body";
	private static final String DESTINATION_KEY = "sender";
	private static final String THREAD_ID_KEY = "thread_id";
	private static final String STATUS_KEY = "status";

	public static Uri send(String destination, String body) {
		if (destination == null || body == null || destination.length() == 0
				|| body.length() == 0) {
			throw new IllegalArgumentException();
		}

		// write sms to queued messages
		ContentValues values = new ContentValues();
		values.put(DESTINATION_KEY, destination);
		values.put(BODY_KEY, body);
		Uri result = Settings.getContext().getContentResolver()
				.insert(QUEUED_MESSAGES_URI, values);
		Log.v("HttpServer", "Queued message: " + result);

		// send
		sendQueuedMessage();

		return result;
	}

	private static void sendQueuedMessage() {
		// get first queued message
		Cursor cursor = Settings.getContext().getContentResolver()
				.query(QUEUED_MESSAGES_URI, null, null, null, "date ASC");
		cursor.moveToFirst();

		int id = cursor.getInt(cursor.getColumnIndex(ID_KEY));
		Uri uri = Uri.withAppendedPath(Sms.CONTENT_URI, String.valueOf(id));
		String destination = cursor.getString(cursor
				.getColumnIndex(DESTINATION_KEY));
		String body = cursor.getString(cursor.getColumnIndex(BODY_KEY));
		int threadId = cursor.getInt(cursor.getColumnIndex(THREAD_ID_KEY));
		int status = cursor.getInt(cursor.getColumnIndex(STATUS_KEY));

		Log.v("HttpServer", "Sending queued message: " + uri.toString());
		sendMessage(destination, body, threadId, status, uri);
		Log.v("HttpServer", "Sent queued message: " + uri.toString());
	}

	private static void sendMessage(String destination, String body,
			int threadId, int status, Uri uri) {
		SmsManager manager = SmsManager.getDefault();
		ArrayList<String> messages = manager.divideMessage(body);
		destination = destination.replaceAll(" ", ""); // remove spaces in destination
		
		// Sms.moveMessageToFolder(Settings.getContext(), uri,
		// Sms.MESSAGE_TYPE_OUTBOX, 0);

		try {
			manager.sendMultipartTextMessage(destination, null, messages, null,
					null);
			Log.v("HttpServer", "Message dispatched: " + uri.toString());
		} catch (Exception e) {
			Log.v("HttpServer",
					"Error sending message: " + uri.toString()
							+ " Exception: " + e.getClass().getName() + ":"
							+ e.getMessage());
		}
	}
}
