package com.dandabby.lelux;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ALL on 10/10/2016.
 */

public class BrightnessSetter {
    public void SetBrightness(Context context,BrightnessCommand brightnessCommand) {
        if (com.dandabby.lelux.PermissionChecker.PermissionsOk(context)) {
            Log.e(context.getString(R.string.Tag), "Can write permission");
            if(brightnessCommand.brightnessSetting== com.dandabby.lelux.BrightnessSetting.LIGHTEN)
            {Set(context,255);}
            else{Set(context,0);}

        } else {
            Log.e(context.getString(R.string.Tag), "No write permission");
        }
    }

    private void Set(Context context, int brightnessLevel){
        try {
            Settings.System.putInt( context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            //get current brightness
            //brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e2) {
            Log.e(context.getString(R.string.Tag), "Exception setting brightness");
            e2.printStackTrace();
        }
        Settings.System.putInt(context.getContentResolver(),
            Settings.System.SCREEN_BRIGHTNESS, brightnessLevel);
        Log.e(context.getString(R.string.Tag), "Brightness level set to " + Integer.toString(brightnessLevel));
    }


}
