package com.example.imran.alarmclockapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class AlarmRemainderProvider extends ContentProvider {

    public  static final String LOG_TAG = AlarmRemainderProvider.class.getSimpleName();

    public  static  final int REMINDER = 100;

    public static final int REMINDER_ID = 101;

   private  static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

   static {

       sUriMatcher.addURI(AlarmRemainderContract.CONTENT_AUTHORITY,AlarmRemainderContract.PATH_VEHICLE,REMINDER);
       sUriMatcher.addURI(AlarmRemainderContract.CONTENT_AUTHORITY, AlarmRemainderContract.PATH_VEHICLE+"/#",REMINDER_ID);
   }

   private AlarmRemainderDBHelper dbHelper;
    @Override
    public boolean onCreate() {
        dbHelper = new AlarmRemainderDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase  database = dbHelper.getReadableDatabase();

        Cursor cursor = null;

        int match = sUriMatcher.match(uri);
        switch (match){
            case REMINDER:
                cursor = database.query(AlarmRemainderContract.AlarmRemainderEntry.TBLE_NAME,projection,selection
                        ,selectionArgs,null,null,sortOrder);
                break;
            case REMINDER_ID:
                selection = AlarmRemainderContract.AlarmRemainderEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(AlarmRemainderContract.AlarmRemainderEntry.TBLE_NAME,projection,selection,
                        selectionArgs,null,null,sortOrder);

                break;

              default:
                  throw new IllegalArgumentException("Content query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;


    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case REMINDER:
                return AlarmRemainderContract.AlarmRemainderEntry.COTENT_LIST_TYPE;
            case REMINDER_ID:
                return AlarmRemainderContract.AlarmRemainderEntry.COTENT_ITEM_TYPE;

             default:
                 throw  new IllegalStateException("Unknown URI " + uri + " with match "+match);
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case REMINDER:
                return insertRemainder(uri,values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for "+uri);
        }

    }

    private Uri insertRemainder(Uri uri, ContentValues values) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        long id = sqLiteDatabase.insert(AlarmRemainderContract.AlarmRemainderEntry.TBLE_NAME,
                null,values);
        if(id == -1){
            Log.e(null,"Failed to insert row for "+uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int rowsDeleted;

        final  int match = sUriMatcher.match(uri);
        switch (match){
            case REMINDER:
                rowsDeleted = sqLiteDatabase.delete(AlarmRemainderContract.AlarmRemainderEntry.TBLE_NAME,
                        selection,selectionArgs);
                break;

            case REMINDER_ID:
                selection = AlarmRemainderContract.AlarmRemainderEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};

                rowsDeleted = sqLiteDatabase.delete(AlarmRemainderContract.AlarmRemainderEntry.TBLE_NAME,selection,
                        selectionArgs);

                break;
            default:
                throw new IllegalArgumentException("Delete is not supported for "+uri);
        }

        if(rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        switch (match){
            case REMINDER:
                return updateRemainder(uri,values,selection,selectionArgs);
            case REMINDER_ID:
                selection = AlarmRemainderContract.AlarmRemainderEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                return updateRemainder(uri,values,selection,selectionArgs);

            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }


    }

    private int updateRemainder(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if(values.size() == 0){
            return  0;
        }

        SQLiteDatabase sqLiteDatabase  = dbHelper.getWritableDatabase();
        int rowsUpdate = sqLiteDatabase.update(AlarmRemainderContract.AlarmRemainderEntry.TBLE_NAME,
                values,selection,selectionArgs);

        if(rowsUpdate != 0)
            getContext().getContentResolver().notifyChange(uri,null);

        return rowsUpdate;
    }
}
