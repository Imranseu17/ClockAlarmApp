package com.example.imran.alarmclockapp;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.imran.alarmclockapp.data.AlarmRemainderContract;

public class AlarmCursorAdapter extends CursorAdapter {
        private TextView mTitletext,mDateAndTimeText,mRepeatInfoText;
        private ImageView mActiveImage,mThumbnailImage;
        private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
        private TextDrawable mDrawableBuilder;




    public AlarmCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.alarm_items,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor data) {
            mTitletext = view.findViewById(R.id.recycleTitle);
            mDateAndTimeText = view.findViewById(R.id.recycle_date_time);
            mActiveImage = view.findViewById(R.id.activate_image);
            mThumbnailImage = view.findViewById(R.id.thumbnail_image);
            mRepeatInfoText = view.findViewById(R.id.recycle_repeat_info);

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

        setRemainderTitle(title);
        if(data != null){
            String dateTime = date + " "+time;
            setRemainderDateTime(dateTime);
        }

        else {
            mDateAndTimeText.setText("Date not set");
        }

        if(repeat != null){
            setRemainderRepeatInfo(repeat,repeatNo,repeatType);
        }else {
            mRepeatInfoText.setText("Repeat Not Set");
        }

        if(active != null){
            setActivateImage(active);
        }else {
            mActiveImage.setImageResource(R.drawable.ic_notifications_off_black_24px);
        }


    }

    public void setRemainderTitle(String title){
        mTitletext.setText(title);
        String letter = "A";
        if(title != null && !title.isEmpty()){
            letter = title.substring(0,1);
        }

        int color = mColorGenerator.getRandomColor();

        mDrawableBuilder = TextDrawable.builder().buildRound(letter,color);
        mThumbnailImage.setImageDrawable(mDrawableBuilder);
    }

    public void setRemainderDateTime(String dateTime){

        mDateAndTimeText.setText(dateTime);
    }

    public void  setRemainderRepeatInfo(String repeat, String repeatNo,String repeatInfo){

        if(repeat.equals("true")){
            mRepeatInfoText.setText("Every "+repeatNo+" "+repeatInfo+"(s)");
        }else if(repeat.equals("false")){
            mRepeatInfoText.setText("Repeat off");
        }
    }

    public void setActivateImage(String activateImage){
        if(activateImage.equals("true")){
            mActiveImage.setImageResource(R.drawable.ic_notifications_black_24px);
        }else if(activateImage.equals("false")){
            mActiveImage.setImageResource(R.drawable.ic_notifications_off_black_24px);
        }

    }
}
