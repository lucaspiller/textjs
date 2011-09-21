package org.stackednotion.textjs.rosebud.adapters;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONObject;
import org.stackednotion.textjs.rosebud.Settings;

import android.database.Cursor;
import android.net.Uri;

public class ThreadsAdapter {
	public static final String[] THREADS_PROJECTION = { ThreadColumns.ID,
			ThreadColumns.ADDRESS, ThreadColumns.DATE, ThreadColumns.BODY,
			ThreadColumns.READ };

	public static final Uri THREADS_URI = Uri
			.parse("content://mms-sms/conversations");

	public static Collection<Thread> all() {
		Cursor cursor = Settings.getContext().getContentResolver()
				.query(THREADS_URI, THREADS_PROJECTION, null, null, ThreadColumns.DATE + " DESC");

		ArrayList<Thread> threads = new ArrayList<Thread>();
		cursor.moveToPosition(-1);
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

		thread.body = cursor.getString(cursor
				.getColumnIndex(ThreadColumns.BODY));

		thread.date = cursor.getLong(cursor.getColumnIndex(ThreadColumns.DATE));

		thread.read = cursor.getInt(cursor.getColumnIndex(ThreadColumns.READ));

		return thread;
	}

	public static class Thread {
		public int id;
		public String address;
		public String body;
		public long date;
		public int read;

		public JSONObject toJson() {
			try {
				JSONObject json = new JSONObject();

				json.put("id", id);
				json.put("address", address);
				json.put("body", body);
				json.put("date", date);
				json.put("read", read);

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

		/**
		 * Indicates whether all messages of the thread have been read.
		 * <P>
		 * Type: INTEGER (boolean)
		 * </P>
		 */
		public static final String READ = "read";
	}

}
