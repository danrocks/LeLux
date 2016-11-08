package com.dantastic.lelux;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by ALL on 10/10/2016.
 */

public class BrightnessSetter {
    public void SetBrightness(Context context, BrightnessSetting brightnessSetting) {
        if (PermissionChecker.PermissionsOk(context)) {
            Log.e(context.getString(R.string.Tag), "Can write permission");
            if(brightnessSetting== BrightnessSetting.LIGHTEN)
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
            Log.e(context.getString(R.string.Tag), "some error");
            e2.printStackTrace();
        }
    Settings.System.putInt(context.getContentResolver(),
            Settings.System.SCREEN_BRIGHTNESS, brightnessLevel);
        Log.e(context.getString(R.string.Tag), "Brightness level " + Integer.toString(brightnessLevel));
    }
}
