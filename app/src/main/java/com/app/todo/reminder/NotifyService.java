package com.app.todo.reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.bridgeit.todoapp.R;


public class NotifyService extends Service {
    Bundle bundle;


    public class ServiceBinder extends Binder {
        NotifyService getService() {
            return NotifyService.this;
        }
    }

    // Unique id to identify the notification.
    private static final int NOTIFICATION = 123;
    // Name of an intent extra we can use to identify if this service was started to create a notification
    public static final String INTENT_NOTIFY = "com.blundell.tut.service.INTENT_NOTIFY";
    // The system notification manager
    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        Log.i("NotifyService", "onCreate()");
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        bundle=intent.getExtras();

        // If this service was started by out AlarmTask intent then we want to show our notification
        if(intent.getBooleanExtra(INTENT_NOTIFY, false))
            showNotification();

        // We don't care if this service is stopped as we have already delivered our notification
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();


    //Creates a notification and shows it in the OS drag-down status bar
    private void showNotification() {
        // This is the 'title' of the notification
        CharSequence title = bundle.getString("title");
        CharSequence description=bundle.getString("description");
        // This is the icon to use on the notification
        int icon = R.drawable.ic_action_alert;

        // This is the scrolling text of the notification
        CharSequence text = "reminder note";
        // What time to show on the notification
        long time = System.currentTimeMillis();

        Notification notification = new Notification(icon, text, time);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this);

        // The PendingIntent to launch our activity if the user selects this notification
        Intent intent=new Intent(this, NoteNotificationActivity.class);
        intent.putExtras(bundle);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Bitmap bmp= BitmapFactory.decodeResource(getResources(),
                R.drawable.alarm_bell);
        // Set the info for the views that show in the notification panel.
       //notification.setLatestEventInfo(this, title, text, contentIntent);
        notification=builder.setContentIntent(contentIntent)
                .setSmallIcon(icon)
                .setTicker(text)
                .setWhen(time)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(description)
                .setLargeIcon(bmp)
                .build();
        // Send the notIfication to the system.
        notificationManager.notify(NOTIFICATION, notification);
        // Stop the service when we are finished
        stopSelf();
    }
}
