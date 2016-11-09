package com.dantastic.lelux;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by ALL on 10/3/2016.
 */

public  class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    TextView v = null;
    BrightnessCommand brightnessCommand;

    public TimePickerFragment(TextView textBox, BrightnessCommand brightnessCommand){
        //todo follow instructions re standards for fragments...
        super();
        v = textBox;
        this.brightnessCommand=brightnessCommand;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
        //use the current time as default value for picker
        final Calendar c = Calendar.getInstance();
        int hour = brightnessCommand.getHour()==null? c.get(Calendar.HOUR_OF_DAY) : brightnessCommand.getHour();
        int minute = brightnessCommand.getMinute()==null? c.get(Calendar.MINUTE) : brightnessCommand.getMinute();

        //Create a new instance of TimePickerDialog and return it
       // note the current object is an activity that implements the TimePickerDialog.OnSetListener interface
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute, true);

//
        return timePickerDialog;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.e(getString(R.string.Tag), "dismiss 3");
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.e(getString(R.string.Tag), "cancel 3");

    }

    // Implement the onTimeSet method required by the OnTimeSetListener  interface.
    // Recall OnTimeSetListener is part of the TimePickerDialog class, which I find strange/interesting - is this normal in C# too?
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        //todo handle
        Log.e(v.getContext().getString(R.string.Tag) ,"onTimeSet invoked- " + Integer.toString(hourOfDay) + ":" +  Integer.toString(minute) );

        brightnessCommand.Update(v.getContext(),hourOfDay, minute, true);

        v.setText(Integer.toString(brightnessCommand.getHour()) + ":" +  Integer.toString(brightnessCommand.getMinute()) );
        Log.e(v.getContext().getString(R.string.Tag), "Finish on time set");
    }
}
