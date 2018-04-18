package com.example.imran.alarmclockapp.remainder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.imran.alarmclockapp.AlarmRemainderActivity;
import com.example.imran.alarmclockapp.R;
import com.example.imran.alarmclockapp.data.AlarmRemainderContract;

public class RemainderAlarmService extends IntentService {
    private  static final String TAG = RemainderAlarmService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 42;

   public  static  PendingIntent getReminderPendingIntent(Context context, Uri uri){
       Intent action = new Intent (context,RemainderAlarmService.class);
       action.setData(uri);
       return PendingIntent.getService(context,0, action,PendingIntent.FLAG_UPDATE_CURRENT);
   }

    public RemainderAlarmService() {
       super((TAG));
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        NotificationManager manager  = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Uri uri = intent.getData();
        Intent action  = new Intent(this,AlarmRemainderActivity.class);
        action.setData(uri);
        PendingIntent operation  = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        Cursor cursor = getContentResolver().query(uri, null,null,null,
                null);
        String description = "";
        try {
            if (cursor != null && cursor.moveToFirst()) {
                description = AlarmRemainderContract.getColumnString(cursor,
                        AlarmRemainderContract.AlarmRemainderEntry.KEY_TITLE);

            }
        } finally {
                if(cursor != null){
                    cursor.close();
                }
            }
            Notification note = new NotificationCompat.Builder(this)
                    .setContentTitle("AlarmReminder")
                    .setContentText(description)
                    .setSmallIcon(R.drawable.ic_add_alert_white_24px)
                    .setContentIntent(operation)
                    .setVibrate(new long[]{1000,1000,1000,1000})
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setAutoCancel(true)
                    .build();

            manager.notify(NOTIFICATION_ID, note);
        }
    }

