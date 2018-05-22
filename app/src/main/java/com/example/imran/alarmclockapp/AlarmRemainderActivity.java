package com.example.imran.alarmclockapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imran.alarmclockapp.data.AlarmRemainderContract;
import com.example.imran.alarmclockapp.remainder.AlarmSeheduler;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class AlarmRemainderActivity extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener,LoaderManager.LoaderCallbacks<Cursor>{
    private static final int EXISTING_VEHICLE_LOADER = 0;
    private Toolbar toolbar;
    private EditText titleEdittext;
    private TextView mDateText, mTimeText, mRepeatText, mRepeatNoText, mRepeatTypeText;
    private FloatingActionButton mFAB1;
    private FloatingActionButton mFAB2;
    private Calendar mClander;
    private int myear, mMonth, mHour, mMinute, mDay;
    private long mRepeatTime;
    private Switch mRepeatSwitch;
    private String mTitle;
    private String mTime;
    private String mDate;
    private String mRepeat;
    private String mRepeatNo;
    private String mRepeatType;
    private String mActive;

    private Uri mCurrentRemainderUri;
    private boolean mVehicleHasChanged = false;

    public final static String KEY_TITLE = "title_Key";
    public final static String KEY_DATE = "date_Key";
    public final static String KEY_TIME = "time_Key";
    public final static String KEY_REPEAT = "repeat_Key";
    public final static String KEY_REPEAT_NO = "repeat_no_Key";
    public final static String KEY_REPEAT_TYPE = "repeat_type_Key";
    public final static String KEY_ACTIVE = "active_Key";


    public final static long milMinute = 60000L;
    public final static long milHour = 3600000L;
    public final static long milDay = 86400000L;
    public final static long milWeek = 604800000L;
    public final static long milMonth = 25920000L;

    @SuppressLint("ClickableViewAccessibility")
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mVehicleHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_remainder);
        Intent intent = getIntent();
        mCurrentRemainderUri = intent.getData();

        if (mCurrentRemainderUri == null) {
            setTitle("Add Remainder Details");

            invalidateOptionsMenu();
        } else {

            setTitle("Edit Remainder");

            getLoaderManager().initLoader(EXISTING_VEHICLE_LOADER, null, this);
        }

        toolbar = findViewById(R.id.toolbar);
        titleEdittext = findViewById(R.id.remainder_title);
        mDateText = findViewById(R.id.set_date);
        mTimeText = findViewById(R.id.set_time);
        mRepeatText = findViewById(R.id.set_repeat);
        mRepeatNoText = findViewById(R.id.set_repeat_no);
        mRepeatTypeText = findViewById(R.id.set_repeat_type);
        mRepeatSwitch = findViewById(R.id.repeat_switch);
        mFAB1 = findViewById(R.id.starred1);
        mFAB2 = findViewById(R.id.starred2);

        mActive = "true";
        mRepeat = "true";
        mRepeatNo = Integer.toString(1);
        mRepeatType = "Hour";

        mClander = Calendar.getInstance();
        mHour = mClander.get(Calendar.HOUR_OF_DAY);
        mMinute = mClander.get(Calendar.MINUTE);
        myear = mClander.get(Calendar.YEAR);
        mMonth = mClander.get(Calendar.MONTH) + 1;
        mDay = mClander.get(Calendar.DATE);


        mDate = mDay + "/" + mMonth + "/" + myear;
        mTime = mHour + ":" + mMinute;

        titleEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitle = s.toString().trim();
                titleEdittext.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });

        mDateText.setText(mDate);
        mTimeText.setText(mTime);
        mRepeatNoText.setText(mRepeatNo);
        mRepeatTypeText.setText(mRepeatType);
        mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");

        if (savedInstanceState != null) {
            String savedTitle = savedInstanceState.getString(KEY_TITLE);
            titleEdittext.setText(savedTitle);
            mTitle = savedTitle;

            String savedTime = savedInstanceState.getString(KEY_TIME);
            mTimeText.setText(savedTime);
            mTime = savedTime;

            String savedDate = savedInstanceState.getString(KEY_DATE);
            mDateText.setText(savedDate);
            mDate = savedDate;

            String savedRepeat = savedInstanceState.getString(KEY_REPEAT);
            mRepeatText.setText(savedRepeat);
            mRepeat = savedRepeat;

            String savedRepeatNo = savedInstanceState.getString(KEY_REPEAT_NO);
            mRepeatNoText.setText(savedRepeatNo);
            mRepeatNo = savedRepeatNo;

            String savedRepeatType = savedInstanceState.getString(KEY_REPEAT_TYPE);

            mRepeatTypeText.setText(savedRepeatType);
            mRepeatType = savedRepeatType;
            mActive = savedInstanceState.getString(KEY_ACTIVE);

        }

        if (mActive.equals("false")) {
            mFAB1.setVisibility(View.VISIBLE);
            mFAB2.setVisibility(View.GONE);
        }

        if (mActive.equals("true")) {
            mFAB1.setVisibility(View.GONE);
            mFAB2.setVisibility(View.VISIBLE);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Reminder");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(KEY_TITLE, titleEdittext.getText());
        outState.putCharSequence(KEY_TIME, mTimeText.getText());
        outState.putCharSequence(KEY_DATE, mDateText.getText());
        outState.putCharSequence(KEY_REPEAT, mRepeatText.getText());
        outState.putCharSequence(KEY_REPEAT_NO, mRepeatNoText.getText());
        outState.putCharSequence(KEY_REPEAT_TYPE, mRepeatTypeText.getText());
        outState.putCharSequence(KEY_ACTIVE, mActive);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String [] projection = {
                AlarmRemainderContract.AlarmRemainderEntry._ID,
                AlarmRemainderContract.AlarmRemainderEntry.KEY_TITLE,
                AlarmRemainderContract.AlarmRemainderEntry.KEY_DATE,
                AlarmRemainderContract.AlarmRemainderEntry.KEY_TIME,
                AlarmRemainderContract.AlarmRemainderEntry.KEY_REPEAT,
                AlarmRemainderContract.AlarmRemainderEntry.KEY_REPEAT_NO,
                AlarmRemainderContract.AlarmRemainderEntry.KEY_REPEAT_TYPE,
                AlarmRemainderContract.AlarmRemainderEntry.KEY_ACTIVE


        };
        return new CursorLoader(this, AlarmRemainderContract.AlarmRemainderEntry.CONTENT_URI,
                projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data == null || data.getCount()<1){
            return;
        }

        if(data.moveToFirst()){
            int titleColumnIndex = data.getColumnIndex(AlarmRemainderContract.AlarmRemainderEntry.KEY_TITLE);
            int dateColumnIndex = data.getColumnIndex(AlarmRemainderContract.AlarmRemainderEntry.KEY_DATE);
            int timeColumnIndex = data.getColumnIndex(AlarmRemainderContract.AlarmRemainderEntry.KEY_TIME);
            int repeatColumnIndex = data.getColumnIndex(AlarmRemainderContract.AlarmRemainderEntry.KEY_REPEAT);
            int repeatNoColumnIndex = data.getColumnIndex(AlarmRemainderContract.AlarmRemainderEntry.KEY_REPEAT_NO);
            int repeatTypeColumnIndex = data.getColumnIndex(AlarmRemainderContract.AlarmRemainderEntry.KEY_REPEAT_TYPE);
            int activeColumnIndex = data.getColumnIndex(AlarmRemainderContract.AlarmRemainderEntry.KEY_ACTIVE);

            String title = data.getString(titleColumnIndex);
            String date = data.getString(dateColumnIndex);
            String time = data.getString(timeColumnIndex);
            String repeat = data.getString(repeatColumnIndex);
            String repeatNo = data.getString(repeatNoColumnIndex);
            String repeatType = data.getString(repeatTypeColumnIndex);
            String active = data.getString(activeColumnIndex);

            titleEdittext.setText(title);
            mDateText.setText(date);
            mTimeText.setText(time);
            mRepeatText.setText("Every "+ repeatNo + " " +repeatType + "(s)");
            mRepeatNoText.setText(repeatNo);
            mRepeatTypeText.setText(repeatType);

            if(repeat.equals("false")){
                mRepeatSwitch.setChecked(false);
                mRepeatText.setText("off");
            }else if(repeat.equals("true")){
                mRepeatSwitch.setChecked(true);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear++;
        mDay = dayOfMonth;
        mMonth = monthOfYear;
        myear = year;
        mDate = dayOfMonth + "/" + "/" + year;
        mDateText.setText(mDate);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        mHour = hourOfDay;
        mMinute = minute;
        if (mMinute < 10) {
            mTime = hourOfDay + ":" + "0" + minute;
        }
        {
            mTime = hourOfDay + ":" + mMinute;
        }

        mTimeText.setText(mTime);
    }

    public void setDate(View view) {

        Calendar now = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.
                newInstance(
                        this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));


        datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
    }

    public void setTime(View view) {

        Calendar now = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.time.TimePickerDialog timePickerDialog =
                com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                        this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE), false);

        timePickerDialog.setThemeDark(false);
        timePickerDialog.show(getFragmentManager(), "TimePickerDialog");
    }

    public void setRepeat(View view) {
    }

    public void onSwitchRepeat(View view) {
        boolean on = ((Switch) view).isChecked();
        if (on) {
            mRepeat = "true";
            mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");
        } else {
            mRepeat = "false";
            mRepeatText.setText("off");
        }
    }

    public void setRepeatInterval(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Number");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().length() == 0) {
                    mRepeatNo = Integer.toString(1);
                    mRepeatNoText.setText(mRepeatNo);
                    mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");
                } else {
                    mRepeatNo = input.getText().toString().trim();
                    mRepeatNoText.setText(mRepeatNo);
                    mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");
                }
            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    public void setTypeRepetitions(View view) {
        final String[] items = new String[5];

        items[0] = "Minute";
        items[1] = "Hour";
        items[2] = "Day";
        items[3] = "Week";
        items[4] = "Month";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Type");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                mRepeatType = items[item];
                mRepeatTypeText.setText(mRepeatType);
                mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void selectFab1(View view) {
        mFAB1 = findViewById(R.id.starred1);
        mFAB1.setVisibility(View.GONE);
        mFAB2 = findViewById(R.id.starred2);
        mFAB2.setVisibility(View.VISIBLE);
        mActive = "true";
    }

    public void selectFab2(View view) {
        mFAB2 = findViewById(R.id.starred2);
        mFAB2.setVisibility(View.GONE);
        mFAB1 = findViewById(R.id.starred1);
        mFAB1.setVisibility(View.VISIBLE);
        mActive = "false";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_remainder, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentRemainderUri == null) {
            MenuItem menuItem = menu.findItem(R.id.discard_remainder);
            menuItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.save_remainder:
                if (titleEdittext.getText().toString().length() == 0) {
                    titleEdittext.setError("Remainder title can not be blank!");
                } else {
                    savedRemainder();
                    finish();
                }

                return true;

            case R.id.discard_remainder:
                showDeleteConformationDialog();
                return true;

            case android.R.id.home:
                if(mVehicleHasChanged){
                    NavUtils.navigateUpFromSameTask(AlarmRemainderActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavUtils.navigateUpFromSameTask(AlarmRemainderActivity.this);
                    }
                };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard your changes and quit editing?");
        builder.setPositiveButton("Discard",discardButtonClickListener);
        builder.setNegativeButton("Keep Editing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialog != null){
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConformationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this Remainder?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteRemainder();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialog != null){
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteRemainder() {

        if(mCurrentRemainderUri != null){
            int rowsDeleted = getContentResolver().delete(mCurrentRemainderUri,null,null);

            if(rowsDeleted == 0){
                Toast.makeText(this,"Error with deleting remainder",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"Remainder Deleted",Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }


    private void savedRemainder() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(AlarmRemainderContract.AlarmRemainderEntry.KEY_TITLE,mTitle);
        contentValues.put(AlarmRemainderContract.AlarmRemainderEntry.KEY_DATE,mDate);
        contentValues.put(AlarmRemainderContract.AlarmRemainderEntry.KEY_TIME,mTime);
        contentValues.put(AlarmRemainderContract.AlarmRemainderEntry.KEY_REPEAT,mRepeat);
        contentValues.put(AlarmRemainderContract.AlarmRemainderEntry.KEY_REPEAT_NO,mRepeatNo);
        contentValues.put(AlarmRemainderContract.AlarmRemainderEntry.KEY_REPEAT_TYPE,mRepeatType);
        contentValues.put(AlarmRemainderContract.AlarmRemainderEntry.KEY_ACTIVE,mActive);

        mClander.set(Calendar.MONTH, --mMonth);
        mClander.set(Calendar.YEAR, --myear);
        mClander.set(Calendar.DAY_OF_MONTH, --mDay);
        mClander.set(Calendar.HOUR_OF_DAY, --mHour);
        mClander.set(Calendar.MINUTE, --mMinute);
        mClander.set(Calendar.SECOND, 0);







        long seletedTimestamp = mClander.getTimeInMillis();

        if(mRepeatType.equals("Minute")){
            mRepeatTime = Integer.parseInt(mRepeatNo)*milMinute;
        } else if(mRepeatType.equals("Hour")){
            mRepeatTime = Integer.parseInt(mRepeatNo)*milHour;
        } else if(mRepeatType.equals("Day")){
            mRepeatTime = Integer.parseInt(mRepeatNo)*milDay;
        } else if(mRepeatType.equals("Week")){
            mRepeatTime = Integer.parseInt(mRepeatNo)*milWeek;
        } else if(mRepeatType.equals("Month")){
            mRepeatTime = Integer.parseInt(mRepeatNo)*milMonth;
        }

        if(mCurrentRemainderUri == null){
            Uri newUri = getContentResolver().insert(AlarmRemainderContract.AlarmRemainderEntry.CONTENT_URI,contentValues);

            if(newUri == null){
                Toast.makeText(this,"Error with saving reminder",Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(this,"Remainder Saved",Toast.LENGTH_SHORT).show();
            }
        }else {
            int rowsAffected = getContentResolver().update(mCurrentRemainderUri,contentValues,null,null);

            if(rowsAffected  == 0){
                Toast.makeText(this,"Error with updating reminder",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"Remainder Updated",Toast.LENGTH_SHORT).show();
            }
        }

        if(mActive.equals("true")){
            if(mRepeat.equals("true")){
                new AlarmSeheduler().setRepeatAlarm(getApplicationContext(),seletedTimestamp,
                        mCurrentRemainderUri,mRepeatTime);
            }else if(mRepeat.equals("false")){
                new AlarmSeheduler().setAlarm(getApplicationContext(),seletedTimestamp,
                        mCurrentRemainderUri);
            }

            Toast.makeText(this,"Alarm time is "+seletedTimestamp,Toast.LENGTH_LONG).show();
        }

        Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_LONG).show();



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
