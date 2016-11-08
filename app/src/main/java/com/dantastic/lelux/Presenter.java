package com.dantastic.lelux;

import android.util.Log;
import android.view.View;

/**
 * Created by ALL on 11/8/2016.
 */

public class Presenter {
    public void onClick(View view, BrightnessCommand brightnessCommand){
        Log.e(view.getContext().getString(R.string.Tag)  , "    View: " + view.getClass().getName());
        Log.e(view.getContext().getString(R.string.Tag)  , "    BrightnessCommand: " + brightnessCommand.getTitle());
    }
}
