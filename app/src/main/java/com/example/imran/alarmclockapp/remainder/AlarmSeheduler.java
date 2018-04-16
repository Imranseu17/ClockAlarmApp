package com.example.imran.alarmclockapp.remainder;

import android.app.AlarmManager;
import android.content.Context;
import android.net.Uri;

public class AlarmSeheduler {

    public void setAlarm(Context context, long alarmTime, Uri remainderTask){

        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);
    }
}
