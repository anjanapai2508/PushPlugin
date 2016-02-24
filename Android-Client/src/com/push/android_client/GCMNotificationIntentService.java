package com.push.android_client;

import com.google.android.gms.gcm.GcmListenerService;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GCMNotificationIntentService extends GcmListenerService  {

	private static final String TAG = "GCMNotificationIntentService";
	private NotificationManager mNotificationManager;
	public static final int NOTIFICATION_ID = 1;
	
	public GCMNotificationIntentService()
	{
		super();
	}
	
	@Override
    public void onMessageReceived(String from, Bundle data) 
	{
        for (int i = 0; i < 3; i++) {
			Log.i(TAG,
					"Working... " + (i + 1) + "/5 @ "
							+ SystemClock.elapsedRealtime());
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}

		}
		Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());

		sendNotification("Message Received from Google GCM Server: "
				+ data.toString());
		Log.i(TAG, "Received: " + data.toString());
		
    }
	@Override
    public void onDeletedMessages() 
	{
        sendNotification("Deleted messages on server");
    }
	@Override
    public void onMessageSent(String msgId)
	{
        sendNotification("Upstream message sent. Id=" + msgId);
    }
	@Override
    public void onSendError(String msgId, String error)
	{
        sendNotification("Upstream message send error. Id=" + msgId + ", error" + error);
    }
	
	//This part pops up the notification on the device
	private void sendNotification(String msg) 
	{
		Log.d(TAG, "Preparing to send notification...: " + msg);
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, MainActivity.class), 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.gcm_cloud)
				.setContentTitle("GCM Notification")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		Log.d(TAG, "Notification sent successfully.");
	}
	
	
	
	
}
