package org.stackednotion.httpserver.adapters;


/**
 * An interface for finding information about conversations and/or creating new ones.
 */
public class Conversation {
/*    private static final Uri sAllThreadsUri =
        Threads.CONTENT_URI.buildUpon().appendQueryParameter("simple", "true").build();

    private static final String[] ALL_THREADS_PROJECTION = {
        Threads._ID, Threads.DATE, Threads.MESSAGE_COUNT, Threads.RECIPIENT_IDS,
        Threads.SNIPPET, Threads.SNIPPET_CHARSET, Threads.READ, Threads.ERROR,
        Threads.HAS_ATTACHMENT
    };

    private boolean loadFromThreadId(long threadId, boolean allowQuery) {
        Cursor c = mContext.getContentResolver().query(sAllThreadsUri, ALL_THREADS_PROJECTION,
                "_id=" + Long.toString(threadId), null, null);
        try {
            if (c.moveToFirst()) {
                fillFromCursor(mContext, this, c, allowQuery);

                if (threadId != mThreadId) {
                    LogTag.error("loadFromThreadId: fillFromCursor returned differnt thread_id!" +
                            " threadId=" + threadId + ", mThreadId=" + mThreadId);
                }
            } else {
                LogTag.error("loadFromThreadId: Can't find thread ID " + threadId);
                return false;
            }
        } finally {
            c.close();
        }
        return true;
    }*/
}
