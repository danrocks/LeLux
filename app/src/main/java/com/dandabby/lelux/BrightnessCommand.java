package com.dandabby.lelux;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.icu.text.SimpleDateFormat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ALL on 10/4/2016.
 */

// A class implementing Observable interface will allow the binding to attach a single listener to a bound object
// to listen for changes of all properties on that object.
// https://developer.android.com/topic/libraries/data-binding/index.html
public class BrightnessCommand extends BaseObservable {
    public final BrightnessSetting brightnessSetting;
    private Integer hour;
    private Integer minute;
    private Boolean isOn;
    private AppCompatActivity  activity;


    public BrightnessCommand( BrightnessSetting brightnessSetting, Integer hour, Integer minute, Boolean isOn,AppCompatActivity  activity){
        this.brightnessSetting=brightnessSetting;
        this.hour=hour;
        this.minute=minute;
        this.isOn=isOn;
        this.activity = activity;
    }

    public BrightnessCommand( BrightnessSetting brightnessSetting, Integer hour, Integer minute, Boolean isOn){
        this.brightnessSetting=brightnessSetting;
        this.hour=hour;
        this.minute=minute;
        this.isOn=isOn;

    }

    @Bindable
    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        Log.e("DebugMessage" , "setting hour");
        this.hour = hour;
        notifyPropertyChanged(BR.timeAsString);
    }

    @Bindable
    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
        notifyPropertyChanged(BR.timeAsString);
    }

    @Bindable
    public Boolean getOn() {
        return isOn;
    }

    public void setOn(Boolean on) {
        isOn = on;

        notifyPropertyChanged(BR.on);
    }

    public void Update( Context context, Integer hour, Integer minute, Boolean isOn){
        if(this.getHour()!=hour
                || this.getMinute()!=minute
                || this.getOn()!=isOn){
            this.setHour(hour);
            this.setMinute(minute);
            this.setOn(isOn);
            UpdateDatabase(context);
        }
        else{
            Log.e("DebugMessage" ,  "No database update reuired");
        }
    }


    private void Update(Context context){
        UpdateDatabase(context);
    }

    private void UpdateDatabase(Context context){
        BrightnessSettingDbHelper dbHelper=new BrightnessSettingDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //new column values
        ContentValues cv = new ContentValues();
        cv.put(BrightnessSettingContract.BrightnessSettingTbl.COL_HOUR, this.getHour());
        cv.put(BrightnessSettingContract.BrightnessSettingTbl.COL_MINUTE, this.getMinute());
        cv.put(BrightnessSettingContract.BrightnessSettingTbl.COL_ON , this.getOn());

        // which rows to update
        String selection = BrightnessSettingContract.BrightnessSettingTbl.COL_SETTING + " = ? ";
        String[] selectionArgs = { this.brightnessSetting.toString() };

        int count= db.update( BrightnessSettingContract.BrightnessSettingTbl.TABLE_NAME,
                cv,
                selection,
                selectionArgs);
        Log.e("DebugMessage" , Integer.toString(count) + " rows updated");



        //Intent intent = new Intent("com.dandabby.lelux.LeLuxReceiver");
        //intent.putExtra("BRIGHTNESS_SETTING", "LIGHTEN");
        //context.sendBroadcast(intent);
        AlarmManagerSetter alarmManagerSetter=new AlarmManagerSetter(context);
        alarmManagerSetter.Refresh();
    }

    @Bindable
    public String getTimeAsString(){
       // String minute = Integer.toString(getMinute());
        String minute =  String.format("%02d", getMinute());
        String hour =  String.format("%02d", getHour());
        return hour + ":" + minute ;
    }

    public Calendar getTime(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY ,getHour());
        c.set(Calendar.MINUTE,getMinute());
        c.set(Calendar.SECOND,0);
        return c;

    }

    @Bindable
    public String getTitle(){
        return brightnessSetting.toString();
    }

    public void showTimePickerDialog(TextView v, BrightnessCommand brightnessCommand){
        TimePickerFragment newFragment = new TimePickerFragment(v,brightnessCommand);

        newFragment.show(activity.getSupportFragmentManager(),"time Picker");
    }

    public void showTimePickerDialog(TextView v){
        showTimePickerDialog(v,this);
    }
}

