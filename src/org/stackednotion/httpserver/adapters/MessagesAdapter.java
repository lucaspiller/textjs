package org.stackednotion.httpserver.adapters;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONObject;
import org.stackednotion.httpserver.Settings;

import android.database.Cursor;
import android.provider.Telephony.Sms;

// o hai: this could break sometime in the future
// because as-of 2.2 there isn't yet a stable API
// for reading SMS messages
// based upon: http://android.git.kernel.org/?p=platform/frameworks/base.git;a=blob;f=core/java/android/provider/Telephony.java;h=bf9e8549aaaf406230cff15346eb1ff2add862e1;hb=HEAD
public class MessagesAdapter {
	public static final Integer THREAD_PAGE_SIZE = 50;
	public static final Integer MESSAGE_PAGE_SIZE = 5;
	
	public static final String[] MESSAGES_RECENT_PROJECTION = { Sms._ID, Sms.ADDRESS, Sms.BODY };
	public static final String[] MESSAGES_PROJECTION = { Sms._ID, Sms.TYPE, Sms.DATE, Sms.READ, Sms.BODY };
	
	public static Collection<Message> recentSent() {
		long recentTime = System.currentTimeMillis() - (5 * 60 * 1000); // 5 mins ago
		Cursor cursor = Settings.getContext().getContentResolver().query(
				Sms.CONTENT_URI, MESSAGES_RECENT_PROJECTION,
				Sms.TYPE + " !=" + Sms.MESSAGE_TYPE_INBOX + " AND " + Sms.DATE + " > " + recentTime,
				null, Sms.DATE + " DESC LIMIT " + MESSAGE_PAGE_SIZE);
		
		ArrayList<Message> messages = new ArrayList<Message>();

		cursor.moveToPosition(-1);
		while (cursor.moveToNext()) {
			Message c = createRecentMessageFromCursor(cursor);
			messages.add(c);
		}
		cursor.close();

		return messages;
	}
	
	public static Message find_by_id(String id) {
		Cursor cursor = Settings.getContext().getContentResolver().query(
				Sms.CONTENT_URI, MESSAGES_PROJECTION,
				Sms._ID + " = " + id, null, null);

		cursor.moveToPosition(-1);
		if (cursor.moveToNext()) {
			return createMessageFromCursor(cursor);
		} else {
			return null;
		}

	}

	public static Collection<Message> find_by_thread_id(Integer threadId, Integer latestMessageId) {
		Cursor cursor = Settings.getContext().getContentResolver().query(
				Sms.CONTENT_URI, MESSAGES_PROJECTION,
				"thread_id=" + Integer.toString(threadId) + " AND " + Sms._ID + " < " + latestMessageId,
				null, Sms.DATE + " DESC LIMIT " + THREAD_PAGE_SIZE);
		return generate_find_by_thread_id_response(cursor);		
	}
	
	public static Collection<Message> find_by_thread_id(Integer threadId) {
		Cursor cursor = Settings.getContext().getContentResolver().query(
				Sms.CONTENT_URI, MESSAGES_PROJECTION,
				"thread_id=" + Integer.toString(threadId), null, Sms.DATE + " DESC LIMIT " + THREAD_PAGE_SIZE);
		return generate_find_by_thread_id_response(cursor);
	}
	
	private static Collection<Message> generate_find_by_thread_id_response(Cursor cursor) {
		ArrayList<Message> messages = new ArrayList<Message>();

		cursor.moveToPosition(-1);
		while (cursor.moveToNext()) {
			Message c = createMessageFromCursor(cursor);
			messages.add(c);
		}
		cursor.close();

		return messages;
	}
	

	public static Message createRecentMessageFromCursor(Cursor cursor) {
		Message message = new Message();

		message.id = cursor.getInt(cursor.getColumnIndex(Sms._ID));
		
		message.address = cursor
				.getString(cursor.getColumnIndex(Sms.ADDRESS));

		message.body = cursor.getString(cursor
				.getColumnIndex(Sms.BODY));

		return message;
	}

	public static Message createMessageFromCursor(Cursor cursor) {
		Message message = new Message();

		message.id = cursor.getInt(cursor.getColumnIndex(Sms._ID));
		
		message.type = cursor
				.getInt(cursor.getColumnIndex(Sms.TYPE));
		
		message.date = cursor.getLong(cursor
				.getColumnIndex(Sms.DATE));

		message.read = cursor
				.getInt(cursor.getColumnIndex(Sms.READ));

		message.body = cursor.getString(cursor
				.getColumnIndex(Sms.BODY));

		return message;
	}

	public static class Message {
		public int id;
		public int type;
		public long date;
		public int read;
		public String body;
		public String address;

		public JSONObject toJson() {
			try {
				JSONObject json = new JSONObject();

				json.put("id", id);
				json.put("type", type);
				json.put("date", date);
				json.put("read", read);
				json.put("body", body);

				return json;
			} catch (Exception e) {
				return null;
			}
		}
	}
}
