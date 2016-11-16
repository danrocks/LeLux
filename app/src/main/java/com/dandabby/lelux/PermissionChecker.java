package com.dandabby.lelux;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by ALL on 10/10/2016.
 */

public class PermissionChecker {
    public static boolean PermissionsOk(Context context){
            Log.e(context.getString(R.string.Tag), "Checking permissions");

            if (Build.VERSION.SDK_INT >= 23 && Settings.System.canWrite(context)) {
                return true;
            } else {
                return false;
            }
    }

}
