package org.stackednotion.httpserver.adapters;

import java.util.ArrayList;
import java.util.Collection;

import org.stackednotion.httpserver.Settings;

import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony.MmsSms;
import android.text.TextUtils;
import android.util.Log;

public class ThreadsAdapter {
	public static Collection<String> all() {
		Uri contentUri = Uri.withAppendedPath(
				MmsSms.CONTENT_URI, "conversations");
		Cursor cursor = Settings
				.getContext()
				.getContentResolver()
				.query(contentUri, null, null, null, null);

		ArrayList<String> threads = new ArrayList<String>();
		cursor.moveToFirst();
		while (cursor.moveToNext()) {
			Log.v("HttpServer", "Columns: " + TextUtils.join(",", cursor.getColumnNames()));
			Integer threadId = cursor.getInt(cursor.getColumnIndex("thread_id"));
			Log.v("HttpServer", "Got id: " + threadId);
			String body = cursor.getString(cursor.getColumnIndex("body"));
			Log.v("HttpServer", "Got body: " + body);
			String person = cursor.getString(cursor.getColumnIndex("address"));
			Log.v("HttpServer", "Got person: " + person);
			String t = String.valueOf(threadId) + ": " + body + ":" + person;
			threads.add(t);
		}
		cursor.close();
		return threads;
	}
	
	/**
	 * Columns for the "threads" table used by MMS and SMS.
	 */
	public interface ThreadColumns {
		public static final String ID = "_id";
		
		/**
		 * The date at which the thread was created.
		 * 
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String DATE = "date";

		/**
		 * A string encoding of the recipient IDs of the recipients of the
		 * message, in numerical order and separated by spaces.
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String RECIPIENT_IDS = "recipient_ids";

		/**
		 * The message count of the thread.
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String MESSAGE_COUNT = "message_count";
		/**
		 * Indicates whether all messages of the thread have been read.
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String READ = "read";

		/**
		 * The snippet of the latest message in the thread.
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String SNIPPET = "snippet";
		/**
		 * The charset of the snippet.
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String SNIPPET_CHARSET = "snippet_cs";
		/**
		 * Type of the thread, either Threads.COMMON_THREAD or
		 * Threads.BROADCAST_THREAD.
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String TYPE = "type";
		/**
		 * Indicates whether there is a transmission error in the thread.
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String ERROR = "error";
		/**
		 * Indicates whether this thread contains any attachments.
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String HAS_ATTACHMENT = "has_attachment";
	}

	/**
	 * Helper functions for the "threads" table used by MMS and SMS.
	 */
	public static final class Threads implements ThreadColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(
				MmsSms.CONTENT_URI, "conversations");
		public static final Uri OBSOLETE_THREADS_URI = Uri.withAppendedPath(
				CONTENT_URI, "obsolete");

		public static final int COMMON_THREAD = 0;
		public static final int BROADCAST_THREAD = 1;

		// No one should construct an instance of this class.
		private Threads() {
		}
	}

}
