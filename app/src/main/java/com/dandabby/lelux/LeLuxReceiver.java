package com.dandabby.lelux;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;


import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LeLuxReceiver extends BroadcastReceiver {
    public LeLuxReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        DateFormat df = new DateFormat();
        Log.e(context.getString(R.string.Tag), "Broadcast received elapsed_time: " + df.format("yyyy-MM-dd hh:mm:ss", SystemClock.elapsedRealtime()) );
        Log.e(context.getString(R.string.Tag), "Broadcast received elapsed_time: " + df.format("yyyy-MM-dd hh:mm:ss", Calendar.getInstance().getTime().getTime()) );


        BrightnessSetter brightnessSetter = new BrightnessSetter();
        BrightnessCommands brightnessCommands = new com.dandabby.lelux.BrightnessCommands(context);
        brightnessCommands.Populate();

        Bundle extras = intent.getExtras();

        if (extras != null) {
            String suppliedBrightnessSetting =intent.getStringExtra("BRIGHTNESS_SETTING");
            BrightnessSetting brightnessSetting = BrightnessSetting.valueOf(suppliedBrightnessSetting);

            Log.e(context.getString(R.string.Tag), "Brightness setting received: " + suppliedBrightnessSetting);
            BrightnessCommand brightnessCommand = brightnessCommands.GetBrightnessCommand(brightnessSetting);
            Log.e(context.getString(R.string.Tag), "Brightness command set.");
            brightnessSetter.SetBrightness(context, brightnessCommand);

            AlarmManagerSetter alarmManagerSetter = new AlarmManagerSetter(context);
            alarmManagerSetter.Refresh();

        }
        else{
            Log.e(context.getString(R.string.Tag), "LeLuxReceiver invoked without brightness setting supplied.");
        }
    }



}
