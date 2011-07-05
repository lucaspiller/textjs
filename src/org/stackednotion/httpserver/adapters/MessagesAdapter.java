package org.stackednotion.httpserver.adapters;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONObject;
import org.stackednotion.httpserver.Settings;

import android.database.Cursor;
import android.net.Uri;

// o hai: this could break sometime in the future
// because as-of 2.2 there isn't yet a stable API
// for reading SMS messages
// based upon: http://android.git.kernel.org/?p=platform/frameworks/base.git;a=blob;f=core/java/android/provider/Telephony.java;h=bf9e8549aaaf406230cff15346eb1ff2add862e1;hb=HEAD
public class MessagesAdapter {
	public static Collection<Message> all() {
		Cursor cursor = Settings.getContext().getContentResolver()
				.query(Uri.parse("content://sms"), null, null, null, null);
		cursor.moveToFirst();

		ArrayList<Message> messages = new ArrayList<Message>();

		while (cursor.moveToNext()) {
			Message c = createMessageFromCursor(cursor);
			messages.add(c);
		}
		cursor.close();

		return messages;
	}

	public static Message createMessageFromCursor(Cursor cursor) {
		Message message = new Message();

		message.id = cursor.getInt(cursor.getColumnIndex(SmsColumns.ID));

		message.type = cursor.getInt(cursor.getColumnIndex(SmsColumns.TYPE));

		message.thread_id = cursor.getInt(cursor
				.getColumnIndex(SmsColumns.THREAD_ID));

		message.address = cursor.getString(cursor
				.getColumnIndex(SmsColumns.ADDRESS));

		message.sender_key = ContactsAdapter
				.find_key_from_phone_number(message.address);

		message.date = cursor.getLong(cursor.getColumnIndex(SmsColumns.DATE));

		message.read = cursor.getInt(cursor.getColumnIndex(SmsColumns.READ));

		message.status = cursor
				.getInt(cursor.getColumnIndex(SmsColumns.STATUS));

		message.body = cursor.getString(cursor.getColumnIndex(SmsColumns.BODY));

		return message;
	}

	public static class Message {
		public int id;
		public int type;
		public int thread_id;
		public String address;
		public String sender_key;
		public long date;
		public int read;
		public int status;
		public String body;

		public JSONObject toJson() {
			try {
				JSONObject json = new JSONObject();

				json.put("id", id);
				json.put("type", type);
				json.put("thread_id", thread_id);
				json.put("address", address);
				json.put("sender_key", sender_key);
				json.put("date", date);
				json.put("read", read);
				json.put("status", status);
				json.put("body", body);

				return json;
			} catch (Exception e) {
				return null;
			}
		}
	}
}
