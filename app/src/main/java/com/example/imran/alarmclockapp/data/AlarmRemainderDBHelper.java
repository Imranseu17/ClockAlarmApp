package com.example.imran.alarmclockapp.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmRemainderDBHelper extends SQLiteOpenHelper{

    public static  final  String DATABASE_NAME = "alarmremainder.db";

    public  static  final int DATABASE_VERSION = 1;


    public AlarmRemainderDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_ALARM_TABLE = "CREATE TABLE " + AlarmRemainderContract.AlarmRemainderEntry.
                TBLE_NAME + " (" + AlarmRemainderContract.AlarmRemainderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                +AlarmRemainderContract.AlarmRemainderEntry.KEY_TITLE + " TEXT NOT NULL,"
                +AlarmRemainderContract.AlarmRemainderEntry.KEY_DATE + " TEXT NOT NULL,"
                +AlarmRemainderContract.AlarmRemainderEntry.KEY_TIME + " TEXT NOT NULL,"
                +AlarmRemainderContract.AlarmRemainderEntry.KEY_REPEAT + " TEXT NOT NULL,"
                +AlarmRemainderContract.AlarmRemainderEntry.KEY_REPEAT_NO + " TEXT NOT NULL,"
                +AlarmRemainderContract.AlarmRemainderEntry.KEY_REPEAT_TYPE + " TEXT NOT NULL,"
                +AlarmRemainderContract.AlarmRemainderEntry.KEY_ACTIVE + " TEXT NOT NULL," + " );";

        db.execSQL(SQL_CREATE_ALARM_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
