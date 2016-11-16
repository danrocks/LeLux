package com.dandabby.lelux;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.method.DateKeyListener;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.e(context.getString(R.string.Tag),"boot receiveation...");

        AlarmManagerSetter alarmManagerSetter=new AlarmManagerSetter(context);
        BrightnessCommand currentBrightnessCommand = alarmManagerSetter.Refresh();
        if(currentBrightnessCommand!=null){
            BrightnessSetter brightnessSetter = new BrightnessSetter();
            brightnessSetter.SetBrightness(context, currentBrightnessCommand);

        }

        Log.e(context.getString(R.string.Tag),"Boot receiver done");
    }




}
