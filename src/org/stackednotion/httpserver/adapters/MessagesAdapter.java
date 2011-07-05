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
	public static final String[] MESSAGES_PROJECTION = { MessageColumns.ID,
			MessageColumns.TYPE, MessageColumns.THREAD_ID,
			MessageColumns.ADDRESS, MessageColumns.DATE, MessageColumns.READ,
			MessageColumns.STATUS, MessageColumns.BODY };

	public static Collection<Message> all(Integer page, Integer limit) {
		Cursor cursor = Settings
				.getContext()
				.getContentResolver()
				.query(Uri.parse("content://sms"), MESSAGES_PROJECTION, null,
						null, null);

		ArrayList<Message> messages = new ArrayList<Message>();

		Integer offset = limit * (page - 1);
		Integer remaining = limit;

		cursor.moveToPosition(offset);
		while (remaining > 0 && cursor.moveToNext()) {
			Message c = createMessageFromCursor(cursor);
			messages.add(c);
			remaining--;
		}
		cursor.close();

		return messages;
	}

	public static Collection<Message> find_by_thread_id(Integer threadId) {
		Cursor cursor = Settings
				.getContext()
				.getContentResolver()
				.query(Uri.parse("content://sms"), MESSAGES_PROJECTION,
						"thread_id=" + Integer.toString(threadId), null, null);

		ArrayList<Message> messages = new ArrayList<Message>();

		cursor.moveToFirst();
		while (cursor.moveToNext()) {
			Message c = createMessageFromCursor(cursor);
			messages.add(c);
		}
		cursor.close();

		return messages;
	}

	public static Message createMessageFromCursor(Cursor cursor) {
		Message message = new Message();

		message.id = cursor.getInt(cursor.getColumnIndex(MessageColumns.ID));

		message.type = cursor
				.getInt(cursor.getColumnIndex(MessageColumns.TYPE));

		message.thread_id = cursor.getInt(cursor
				.getColumnIndex(MessageColumns.THREAD_ID));

		message.address = cursor.getString(cursor
				.getColumnIndex(MessageColumns.ADDRESS));

		message.sender_key = ContactsAdapter
				.find_key_from_phone_number(message.address);

		message.date = cursor.getLong(cursor
				.getColumnIndex(MessageColumns.DATE));

		message.read = cursor
				.getInt(cursor.getColumnIndex(MessageColumns.READ));

		message.status = cursor.getInt(cursor
				.getColumnIndex(MessageColumns.STATUS));

		message.body = cursor.getString(cursor
				.getColumnIndex(MessageColumns.BODY));

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

	public interface MessageColumns {
		/**
		 * The type of the message
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String ID = "_id";

		public static final String TYPE = "type";

		public static final int MESSAGE_TYPE_ALL = 0;
		public static final int MESSAGE_TYPE_INBOX = 1;
		public static final int MESSAGE_TYPE_SENT = 2;
		public static final int MESSAGE_TYPE_DRAFT = 3;
		public static final int MESSAGE_TYPE_OUTBOX = 4;
		public static final int MESSAGE_TYPE_FAILED = 5; // for failed outgoing
		// messages
		public static final int MESSAGE_TYPE_QUEUED = 6; // for messages to send
		// later

		/**
		 * The thread ID of the message
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String THREAD_ID = "thread_id";

		/**
		 * The address of the other party
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String ADDRESS = "address";

		/**
		 * The person ID of the sender
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String PERSON_ID = "person";

		/**
		 * The date the message was sent
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String DATE = "date";

		/**
		 * Has the message been read
		 * <P>
		 * Type: INTEGER (boolean)
		 * </P>
		 */
		public static final String READ = "read";

		/**
		 * Indicates whether this message has been seen by the user. The "seen"
		 * flag will be used to figure out whether we need to throw up a
		 * statusbar notification or not.
		 */
		public static final String SEEN = "seen";

		/**
		 * The TP-Status value for the message, or -1 if no status has been
		 * received
		 */
		public static final String STATUS = "status";

		public static final int STATUS_NONE = -1;
		public static final int STATUS_COMPLETE = 0;
		public static final int STATUS_PENDING = 64;
		public static final int STATUS_FAILED = 128;

		/**
		 * The subject of the message, if present
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String SUBJECT = "subject";

		/**
		 * The body of the message
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String BODY = "body";

		/**
		 * The id of the sender of the conversation, if present
		 * <P>
		 * Type: INTEGER (reference to item in content://contacts/people)
		 * </P>
		 */
		public static final String PERSON = "person";

		/**
		 * The protocol identifier code
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String PROTOCOL = "protocol";

		/**
		 * Whether the <code>TP-Reply-Path</code> bit was set on this message
		 * <P>
		 * Type: BOOLEAN
		 * </P>
		 */
		public static final String REPLY_PATH_PRESENT = "reply_path_present";

		/**
		 * The service center (SC) through which to send the message, if present
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String SERVICE_CENTER = "service_center";

		/**
		 * Has the message been locked?
		 * <P>
		 * Type: INTEGER (boolean)
		 * </P>
		 */
		public static final String LOCKED = "locked";

		/**
		 * Error code associated with sending or receiving this message
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String ERROR_CODE = "error_code";

		/**
		 * Meta data used externally.
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
		public static final String META_DATA = "meta_data";
	}
}
