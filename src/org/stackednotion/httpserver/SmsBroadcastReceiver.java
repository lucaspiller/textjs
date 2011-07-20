package org.stackednotion.httpserver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Telephony.Sms;
import android.util.Log;

public class SmsBroadcastReceiver extends BroadcastReceiver {
	public static final String SMS_SEND_QUEUED_ACTION = "com.stackednotion.httpserver.SEND_QUEUED";
	public static final String SMS_SENT_ACTION = "com.stackednotion.httpserver.SMS_SENT";
	public static final String EXTRA_SMS_SENT_SEND_NEXT = "sms_sent_send_next";
	public static final String EXTRA_RESULT_CODE = "result_code";
	public static final String EXTRA_ERROR_CODE = "errorCode";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v("HttpServer", "Received intent: " + intent.getAction());
		if (intent.getAction().equals(SMS_SEND_QUEUED_ACTION)) {
			handleSendQueuedSms(context, intent);
		} else if (intent.getAction().equals(SMS_SENT_ACTION)) {
			intent.putExtra(EXTRA_RESULT_CODE, getResultCode());
			handleSentSms(context, intent);
		}
	}

	private void handleSentSms(Context context, Intent intent) {
		Uri uri = intent.getData();
		int resultCode = intent.getIntExtra(EXTRA_RESULT_CODE, 0);
		int error = intent.getIntExtra(EXTRA_ERROR_CODE, 0);
		boolean sendNextMsg = intent.getBooleanExtra(
				EXTRA_SMS_SENT_SEND_NEXT, false);

		if (resultCode == Activity.RESULT_OK) {
			sentSmsSuccess(context, uri, error);
		} else {
			sentSmsFailedGeneric(context, uri, resultCode, error);
		}

		if (sendNextMsg) {
			sendNextSms(context);
		}
	}

	private void sentSmsSuccess(Context context, Uri uri, int error) {
		Log.v("HttpServer", "Successfully sent: " + uri);
		Sms.moveMessageToFolder(context, uri, Sms.MESSAGE_TYPE_SENT, error);
	}

	private void sentSmsFailedGeneric(Context context, Uri uri,
			int resultCode, int error) {
		Log.v("HttpServer", "Failed to sent: " + uri + " unknown result: "
				+ resultCode + " error: " + error);
		Sms.moveMessageToFolder(context, uri, Sms.MESSAGE_TYPE_FAILED,
				error);
	}

	private void sendNextSms(Context context) {
		context.sendBroadcast(new Intent(SMS_SEND_QUEUED_ACTION, null,
				context, SmsBroadcastReceiver.class));
	}
	
	private void handleSendQueuedSms(Context context, Intent intent) {
		SmsAdapter.sendQueuedSms();
	}
}