package com.dantastic.lelux;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

public class LeLuxReceiver extends BroadcastReceiver {
    public LeLuxReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
        Log.e(context.getString(R.string.Tag), "Broadcast received " + SystemClock.elapsedRealtime());

        BrightnessSetter brightnessSetter = new BrightnessSetter();

        Bundle extras = intent.getExtras();
        String value ="";
        if (extras != null) {
            //value = extras.getString("BRIGHTNESS_SETTING");
            value = intent.getStringExtra("BRIGHTNESS_SETTING");
            Log.e(context.getString(R.string.Tag), "Broadcast value " + value);
            //The key argument here must match that used in the other activity
        }

        if(value.equals("darken")) {
            brightnessSetter.SetBrightness(context, BrightnessSetting.DARKEN);
            Log.e(context.getString(R.string.Tag), "Set alarm lighten");
            SetAlarm(context, "lighten");
        }else{
            brightnessSetter.SetBrightness(context, BrightnessSetting.LIGHTEN);
            Log.e(context.getString(R.string.Tag), "Set alarm darken");
            SetAlarm(context, "darken");
        }
    }

    private void SetAlarm(Context context, String brightnessCommand){
        AlarmManager am =(AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        Intent newIntent =new Intent ("com.dantastic.lelux.LeLuxReceiver");
        newIntent.putExtra("BRIGHTNESS_SETTING", brightnessCommand);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, newIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        //am.set(AlarmManager.RTC_WAKEUP,20000,pi);
        long futureinms = SystemClock.elapsedRealtime() +50000;
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,futureinms,pi);
    }
}
