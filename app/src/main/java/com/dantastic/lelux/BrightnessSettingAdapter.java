package com.dantastic.lelux;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import static com.dantastic.lelux.R.layout.row_view;

/**
 * Created by ALL on 10/11/2016.
 */

//public class BrightnessSettingAdapter extends Arr BaseAdapter{
public class BrightnessSettingAdapter extends ArrayAdapter<BrightnessCommand> {
    public BrightnessCommand[] brightnessCommandArray;

    private Context context;
//    public BrightnessSettingAdapter(Context context){
//        this.context=context;
//    }


    public BrightnessSettingAdapter(Context context, int resource, BrightnessCommand[] objects){
        super(context, resource,objects);
        this.context=context;
        this.brightnessCommandArray=objects;
        Log.e(context.getString(R.string.Tag),"Initialise base string adapter");
    }

    @Override
    public int getCount() {
        Log.e(context.getString(R.string.Tag),"BrightnessSettingAdapter getcount (" + Integer.toString( brightnessCommandArray.length) + ")");
        return brightnessCommandArray.length;

    }

    @Override
    public BrightnessCommand getItem(int i) {
        Log.e(context.getString(R.string.Tag),"BrightnessSettingAdapter getItem");
        return null;

    }

    @Override
    public long getItemId(int i) {
        Log.e(context.getString(R.string.Tag),"BrightnessSettingAdapter getItemId");
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Log.e(context.getString(R.string.Tag),"inflating");
        if(view==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_view, viewGroup,false);
        }

        BrightnessCommand brightnessCommand =brightnessCommandArray[i];

        ((TextView)view.findViewById(R.id.settingTitle)).setText(brightnessCommand.getTitle());

        ((SwitchCompat)view.findViewById(R.id.switcher)).setChecked(brightnessCommand.getOn());
        if(brightnessCommand.getOn()){
            ((TextView)view.findViewById(R.id.timeToAct)).setText(brightnessCommand.getTimeAsString());

        }else{
            ((TextView)view.findViewById(R.id.timeToAct)).setText("");
        }

        Log.e(context.getString(R.string.Tag),"inflated");

        return view;
    }
}
