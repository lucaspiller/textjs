package org.stackednotion.httpserver.adapters;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONObject;
import org.stackednotion.httpserver.Settings;

import android.database.Cursor;
import android.net.Uri;

public class ThreadsAdapter {
	public static final String[] THREADS_PROJECTION = { ThreadColumns.ID,
			ThreadColumns.ADDRESS, ThreadColumns.DATE, ThreadColumns.BODY };

	public static Collection<Thread> all() {

		Uri contentUri = Uri.parse("content://mms-sms/conversations");
		Cursor cursor = Settings.getContext().getContentResolver().query(
				contentUri, THREADS_PROJECTION, null, null, null);

		ArrayList<Thread> threads = new ArrayList<Thread>();
		cursor.moveToFirst();
		while (cursor.moveToNext()) {
			Thread t = createThreadFromCursor(cursor);
			threads.add(t);
		}
		cursor.close();
		return threads;
	}

	public static Thread createThreadFromCursor(Cursor cursor) {
		Thread thread = new Thread();

		thread.id = cursor.getInt(cursor.getColumnIndex(ThreadColumns.ID));

		thread.address = cursor.getString(cursor
				.getColumnIndex(ThreadColumns.ADDRESS));

		thread.sender_key = ContactsAdapter
				.find_key_from_phone_number(thread.address);

		thread.body = cursor.getString(cursor
				.getColumnIndex(ThreadColumns.BODY));

		thread.date = cursor.getLong(cursor.getColumnIndex(ThreadColumns.DATE));

		return thread;
	}

	public static class Thread {
		public int id;
		public String address;
		public String sender_key;
		public String body;
		public long date;

		public JSONObject toJson() {
			try {
				JSONObject json = new JSONObject();

				json.put("id", id);
				json.put("address", address);
				json.put("sender_key", sender_key);
				json.put("body", body);
				json.put("date", date);

				return json;
			} catch (Exception e) {
				return null;
			}
		}
	}

	/**
	 * Columns for the "threads" table used by MMS and SMS.
	 */
	public interface ThreadColumns {
		public static final String ID = "thread_id";

		/**
		 * The address of the other party
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String ADDRESS = "address";

		/**
		 * The snippet of the latest message in the thread.
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String BODY = "body";

		/**
		 * The date the last message in the thread was sent or received
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String DATE = "date";
	}

}
