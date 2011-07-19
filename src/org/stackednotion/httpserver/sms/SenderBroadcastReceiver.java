package org.stackednotion.httpserver.sms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Telephony.Sms;
import android.telephony.SmsManager;
import android.util.Log;

public class SenderBroadcastReceiver extends BroadcastReceiver {
	public static final String MESSAGE_SEND_QUEUED_ACTION = "com.stackednotion.httpserver.sms.SEND_QUEUED";
	public static final String MESSAGE_SENT_ACTION = "com.stackednotion.httpserver.sms.MESSAGE_SENT";
	public static final String EXTRA_MESSAGE_SENT_SEND_NEXT = "message_sent_send_next";
	public static final String EXTRA_RESULT_CODE = "result_code";
	public static final String EXTRA_ERROR_CODE = "errorCode";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v("HttpServer", "Received intent: " + intent.getAction());
		if (intent.getAction().equals(MESSAGE_SEND_QUEUED_ACTION)) {
			Sender.handleSendQueuedMessage(context, intent);
		} else if (intent.getAction().equals(MESSAGE_SENT_ACTION)) {
			intent.putExtra(EXTRA_RESULT_CODE, getResultCode());
			handleSentMessage(context, intent);
		}
	}

	private void handleSentMessage(Context context, Intent intent) {
		Uri uri = intent.getData();
		int resultCode = intent.getIntExtra(EXTRA_RESULT_CODE, 0);
		int error = intent.getIntExtra(EXTRA_ERROR_CODE, 0);
		boolean sendNextMsg = intent.getBooleanExtra(
				EXTRA_MESSAGE_SENT_SEND_NEXT, false);

		switch (resultCode) {
		case Activity.RESULT_OK:
			sentMessageSuccess(context, uri, error);
			if (sendNextMsg) {
				sendNextMessage(context);
			}
			break;
		case SmsManager.RESULT_ERROR_RADIO_OFF:
		case SmsManager.RESULT_ERROR_NO_SERVICE:
			sentMessageFailedRadio(context, uri, error);
			// TODO handle radio being off
			break;
		default:
			sentMessageFailedGeneric(context, uri, resultCode, error);
			if (sendNextMsg) {
				sendNextMessage(context);
			}
			break;
		}
	}

	private void sentMessageSuccess(Context context, Uri uri, int error) {
		Log.v("HttpServer", "Successfully sent: " + uri);
		Sms.moveMessageToFolder(context, uri, Sms.MESSAGE_TYPE_SENT, error);
	}

	private void sentMessageFailedRadio(Context context, Uri uri, int error) {
		Log.v("HttpServer", "Failed to sent: " + uri + " no service");
		Sms.moveMessageToFolder(context, uri, Sms.MESSAGE_TYPE_QUEUED, error);
	}

	private void sentMessageFailedGeneric(Context context, Uri uri,
			int resultCode, int error) {
		Log.v("HttpServer", "Failed to sent: " + uri + " unknown result: "
				+ resultCode + " error: " + error);
		Sms.moveMessageToFolder(context, uri, Sms.MESSAGE_TYPE_FAILED, error);
	}
	
	private void sendNextMessage(Context context) {
		context.sendBroadcast(
				new Intent(
						SenderBroadcastReceiver.MESSAGE_SEND_QUEUED_ACTION,
						null, context,
						SenderBroadcastReceiver.class));
	}
}
