package com.adeebnqo.alarmapp.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.adeebnqo.alarmapp.R;
import com.adeebnqo.alarmapp.activity.LandingActivityLoader;

/**
 * Created by adeeb on 5/20/15.
 */
public class AppNotification {

    private int notificationIdentifier = 2323;

    private Context context;
    NotificationCompat.Builder mBuilder;


    private static AppNotification instance;

    public static void Init(Context context){
        instance = new AppNotification(context);
    }

    public static AppNotification getNotifier(){
        if (instance == null){
            throw new IllegalStateException("AppNotification needs to be initialized. Init(Context context) has not been called.");
        }
        return instance;
    }

    private AppNotification(Context context) {
        this.context = context;
    }

    public void sendNotification(String title, String contextText) {

        mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(contextText);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, LandingActivityLoader.class);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(LandingActivityLoader.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(notificationIdentifier, mBuilder.build());
    }

}
