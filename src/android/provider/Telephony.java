/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.provider;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

/**
 * The Telephony provider contains data related to phone operation.
 * 
 * @hide
 */
public final class Telephony {
	// Constructor
	public Telephony() {
	}

	/**
	 * Base columns for tables that contain text based SMSs.
	 */
	public interface TextBasedSmsColumns {
		/**
		 * The type of the message
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
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
		public static final int STATUS_PENDING = 32;
		public static final int STATUS_FAILED = 64;

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

	/**
	 * Contains all text based SMS messages.
	 */
	public static final class Sms implements BaseColumns, TextBasedSmsColumns {
		public static final Cursor query(ContentResolver cr, String[] projection) {
			return cr.query(CONTENT_URI, projection, null, null,
					DEFAULT_SORT_ORDER);
		}

		public static final Cursor query(ContentResolver cr,
				String[] projection, String where, String orderBy) {
			return cr.query(CONTENT_URI, projection, where, null,
					orderBy == null ? DEFAULT_SORT_ORDER : orderBy);
		}

		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTENT_URI = Uri.parse("content://sms");

		/**
		 * The default sort order for this table
		 */
		public static final String DEFAULT_SORT_ORDER = "date DESC";
	}
	
	/**
	 * Columns for the "threads" table used by MMS and SMS.
	 */
	public interface ThreadsColumns extends BaseColumns {
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
	public static final class Threads implements ThreadsColumns {
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

	/**
	 * Contains all MMS and SMS messages.
	 */
	public static final class MmsSms implements BaseColumns {
		/**
		 * The column to distinguish SMS &amp; MMS messages in query results.
		 */
		public static final String TYPE_DISCRIMINATOR_COLUMN = "transport_type";

		public static final Uri CONTENT_URI = Uri.parse("content://mms-sms/");

		public static final Uri CONTENT_CONVERSATIONS_URI = Uri
				.parse("content://mms-sms/conversations");

		public static final Uri CONTENT_FILTER_BYPHONE_URI = Uri
				.parse("content://mms-sms/messages/byphone");

		public static final Uri CONTENT_UNDELIVERED_URI = Uri
				.parse("content://mms-sms/undelivered");

		public static final Uri CONTENT_DRAFT_URI = Uri
				.parse("content://mms-sms/draft");

		public static final Uri CONTENT_LOCKED_URI = Uri
				.parse("content://mms-sms/locked");

		/***
		 * Pass in a query parameter called "pattern" which is the text to
		 * search for. The sort order is fixed to be thread_id ASC,date DESC.
		 */
		public static final Uri SEARCH_URI = Uri
				.parse("content://mms-sms/search");

		// Constants for message protocol types.
		public static final int SMS_PROTO = 0;
		public static final int MMS_PROTO = 1;

		// Constants for error types of pending messages.
		public static final int NO_ERROR = 0;
		public static final int ERR_TYPE_GENERIC = 1;
		public static final int ERR_TYPE_SMS_PROTO_TRANSIENT = 2;
		public static final int ERR_TYPE_MMS_PROTO_TRANSIENT = 3;
		public static final int ERR_TYPE_TRANSPORT_FAILURE = 4;
		public static final int ERR_TYPE_GENERIC_PERMANENT = 10;
		public static final int ERR_TYPE_SMS_PROTO_PERMANENT = 11;
		public static final int ERR_TYPE_MMS_PROTO_PERMANENT = 12;

		public static final class PendingMessages implements BaseColumns {
			public static final Uri CONTENT_URI = Uri.withAppendedPath(
					MmsSms.CONTENT_URI, "pending");
			/**
			 * The type of transport protocol(MMS or SMS).
			 * <P>
			 * Type: INTEGER
			 * </P>
			 */
			public static final String PROTO_TYPE = "proto_type";
			/**
			 * The ID of the message to be sent or downloaded.
			 * <P>
			 * Type: INTEGER
			 * </P>
			 */
			public static final String MSG_ID = "msg_id";
			/**
			 * The type of the message to be sent or downloaded. This field is
			 * only valid for MM. For SM, its value is always set to 0.
			 */
			public static final String MSG_TYPE = "msg_type";
			/**
			 * The type of the error code.
			 * <P>
			 * Type: INTEGER
			 * </P>
			 */
			public static final String ERROR_TYPE = "err_type";
			/**
			 * The error code of sending/retrieving process.
			 * <P>
			 * Type: INTEGER
			 * </P>
			 */
			public static final String ERROR_CODE = "err_code";
			/**
			 * How many times we tried to send or download the message.
			 * <P>
			 * Type: INTEGER
			 * </P>
			 */
			public static final String RETRY_INDEX = "retry_index";
			/**
			 * The time to do next retry.
			 */
			public static final String DUE_TIME = "due_time";
			/**
			 * The time we last tried to send or download the message.
			 */
			public static final String LAST_TRY = "last_try";
		}

		public static final class WordsTable {
			public static final String ID = "_id";
			public static final String SOURCE_ROW_ID = "source_id";
			public static final String TABLE_ID = "table_to_use";
			public static final String INDEXED_TEXT = "index_text";
		}
	}
}