package com.example.imran.alarmclockapp.remainder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Build;

public class AlarmSeheduler {

    public void setAlarm(Context context, long alarmTime, Uri remainderTask){

        AlarmManager manager = AlarmManagerProvider.getAlarmMnager(context);
        PendingIntent Operation =
                RemainderAlarmService.getReminderPendingIntent(context,remainderTask);
        if (Build.VERSION.SDK_INT>=23){

            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, Operation);

        }else if (Build.VERSION.SDK_INT>=19){
            manager.setExact(AlarmManager.RTC_WAKEUP,alarmTime,Operation);
        }else {
            manager.set(AlarmManager.RTC_WAKEUP, alarmTime,Operation);
        }
    }
    public  void setRepeatAlarm(Context context, long alaramTime, Uri reminderTask, long RepeatTime){
        AlarmManager manager = AlarmManagerProvider.getAlarmMnager(context);
        PendingIntent operation =
                RemainderAlarmService.getReminderPendingIntent(context, reminderTask);
        manager.setRepeating(AlarmManager.RTC_WAKEUP,alaramTime,RepeatTime,operation);


    }
    public void cancelAlarm(Context context,Uri reminderTask){
        AlarmManager alarmmanager = AlarmManagerProvider.getAlarmMnager(context);
        PendingIntent operation =
                RemainderAlarmService.getReminderPendingIntent(context,reminderTask);
        alarmmanager.cancel(operation);

    }


}
