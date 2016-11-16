package com.dandabby.lelux;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ALL on 11/15/2016.
 */

public class AlarmManagerSetter {
    private Context context;
    public AlarmManagerSetter(Context context){
        this.context = context;
    }

    // Function that sets up or refreshes the Alarm manager.
    // Will be called by boot receiver and when updates are meade to BrightnessCommand settings.
    // Returns the Brightness command that might be applicable now, caller decides whether to act on this.
    public  BrightnessCommand Refresh(){
        BrightnessCommands brightnessCommands = new com.dandabby.lelux.BrightnessCommands(context);
        brightnessCommands.Populate();
        Log.e(context.getString(R.string.Tag),"Model populated...");

        List<SetterTime> setterTimes =  new ArrayList<SetterTime>();

        try {
            Iterator<BrightnessCommand> itr = brightnessCommands.brightnessCommands.iterator();
            while (itr.hasNext()) {
                BrightnessCommand bc = itr.next();
                if (bc.getOn()) {
                    Log.e(context.getString(R.string.Tag),"BrightnessCommand: " + bc.getTitle());
                    Date d = new Date();

                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.HOUR_OF_DAY ,bc.getHour());
                    c.set(Calendar.MINUTE,bc.getMinute());

                    setterTimes.add(new SetterTime(c.getTime(), false, bc));
                }
            }

            SetterTime currentTime =new SetterTime(Calendar.getInstance().getTime(), true, null);
            setterTimes.add(currentTime);

            Collections.sort( setterTimes, new Comparator<SetterTime>() {
                @Override
                public int compare(SetterTime setterTime, SetterTime t1) {
                    //Log.e(context.getString(R.string.Tag), "compare: " + setterTime.DateFormat() + " to " + t1.DateFormat() );

                    int result =setterTime.time.compareTo(t1.time);

                    if (setterTime.time.before(t1.time) ){
                        result = -1;
                    } else if (setterTime.time.after(t1.time) ){
                        result = 1;
                    } else {
                        result = 0;
                    }

                    //Log.e(context.getString(R.string.Tag), "time compare: " + Integer.toString(result));
                    return result;
                }
            });

            BrightnessCommand currentBrightnessCommand = null;
            BrightnessCommand nextBrightnessCommand = null;
            int currentTimeIndex =  setterTimes.indexOf(currentTime);
            Log.e(context.getString(R.string.Tag), "Setter item count: " + Integer.toString(setterTimes.size()));

            if (setterTimes.size() == 1) {
                //no brightness commands set...
                Log.e(context.getString(R.string.Tag), "no brightness commands set...");
                return null;
            } else if (setterTimes.size() == 2) {
                int brightnessCommandIndex = currentTimeIndex==0?1:0;
                currentBrightnessCommand =setterTimes.get(brightnessCommandIndex).brightnessCommand;
                nextBrightnessCommand =setterTimes.get(brightnessCommandIndex).brightnessCommand;
            } else if (setterTimes.size() == 3) {
                switch (currentTimeIndex){
                    case 0:
                   //     Log.e(context.getString(R.string.Tag), "Time ordering: 1");
                        currentBrightnessCommand =setterTimes.get(2).brightnessCommand;
                        nextBrightnessCommand =setterTimes.get(1).brightnessCommand;
                        break;
                    case 1:
                   //     Log.e(context.getString(R.string.Tag), "Time ordering: 2");
                        currentBrightnessCommand =setterTimes.get(0).brightnessCommand;
                        nextBrightnessCommand =setterTimes.get(2).brightnessCommand;
                        break;
                    case 2:
                   //     Log.e(context.getString(R.string.Tag), "Time ordering: 3");
                        currentBrightnessCommand =setterTimes.get(1).brightnessCommand;
                        nextBrightnessCommand =setterTimes.get(0).brightnessCommand;
                        break;
                    default:
                        throw new Exception("Current setting time not set");
                }
            }

            SetAlarm(context,nextBrightnessCommand);
            return currentBrightnessCommand;
        }
        catch (Exception e){
            Log.e(context.getString(R.string.Tag), "Receiver exception: " + e.getMessage());
        }
        return null;
    }


    public  void SetAlarm(Context context, BrightnessCommand brightnessCommand){
        AlarmManager am =(AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        Intent newIntent =new Intent ("com.dandabby.lelux.LeLuxReceiver");

        String next = brightnessCommand.brightnessSetting.toString();

        newIntent.putExtra("BRIGHTNESS_SETTING", next);

        PendingIntent pi = PendingIntent.getBroadcast(context, 0, newIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        //long futureinms = SystemClock.elapsedRealtime() +50000;
        long futureinms = getMsUntilAlarm(brightnessCommand.getTime());
        //long futureinms = (brightnessCommand.getTime().getTime().getTime());

        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureinms, pi);

        DateFormat df = new DateFormat();
        Log.e(context.getString(R.string.Tag), "Set alarm to " + next + " in " +  Long.toString(futureinms) +"ms" );
        Log.e(context.getString(R.string.Tag), "Current time " + df.format("yyyy-MM-dd hh:mm:ss",  Calendar.getInstance().getTime().getTime()) );
    }

    private static long getMsUntilAlarm(Calendar targetTime){
        Calendar currentTime = Calendar.getInstance();
        if(targetTime.before(currentTime)){
            targetTime.add(Calendar.DAY_OF_YEAR,1);
        }

        Log.e("DebugMessage", "Target time : " + Long.toString(targetTime.getTime().getTime()));
        Log.e("DebugMessage", "Current time: " + Long.toString(currentTime.getTime().getTime()));
        Log.e("DebugMessage", "Elapsed time: " + SystemClock.elapsedRealtime());

        return (targetTime.getTime().getTime()-currentTime.getTime().getTime()) + SystemClock.elapsedRealtime();
    }

    private class SetterTime{
        Date time;
        Boolean isCurrentTime;
        BrightnessCommand brightnessCommand;

        public SetterTime(Date time, Boolean isCurrentTime, BrightnessCommand brightnessCommand){
            this.time=time;
            this.isCurrentTime=isCurrentTime;
            this.brightnessCommand=brightnessCommand;
        }

        public String DateFormat(){
            DateFormat df = new DateFormat();
            return df.format("yyyy-MM-dd hh:mm:ss", time ).toString();
        }
    }
}
