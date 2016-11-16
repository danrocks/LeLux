package com.dandabby.lelux;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ALL on 10/4/2016.
 */

public class BrightnessCommands {
    public List<BrightnessCommand> brightnessCommands;
    private Context context;
    public BrightnessCommands (Context context){
        //todo get from database
        this.context=context;
        brightnessCommands = new ArrayList<BrightnessCommand>();
    }

    public void Add(BrightnessCommand brightnessCommand){
        brightnessCommands.add(brightnessCommand);
    }

    public int Size(){
        return brightnessCommands.size();
    }

    public BrightnessCommand GetBrightnessCommand(BrightnessSetting brightnessSetting){
        Iterator<BrightnessCommand> itr=brightnessCommands.iterator();
        while(itr.hasNext()){
            BrightnessCommand bc = itr.next();
            if(bc.brightnessSetting == brightnessSetting){
                return bc;
            }
        }
        return null;
    }

    public void Populate(){
        SQLiteOpenHelper mDbHelper = new com.dandabby.lelux.BrightnessSettingDbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {BrightnessSettingContract.BrightnessSettingTbl.COL_SETTING,
                BrightnessSettingContract.BrightnessSettingTbl.COL_HOUR,
                BrightnessSettingContract.BrightnessSettingTbl.COL_MINUTE,
                BrightnessSettingContract.BrightnessSettingTbl.COL_ON};

        String sortOrder = BrightnessSettingContract.BrightnessSettingTbl.COL_SETTING + " ASC ";

        Cursor c = db.query(
                BrightnessSettingContract.BrightnessSettingTbl.TABLE_NAME, //table to query
                projection, //columns to get
                null, //where clause cols
                null,//where clause args
                null, //row grouping
                null,//rowgorup filters
                sortOrder //sortorder
        );

        int i = 0;

        c.moveToPosition(-1);
        while (c.moveToNext()){
            i=i+1;
            BrightnessSetting bs =BrightnessSetting.valueOf(c.getString(c.getColumnIndexOrThrow(BrightnessSettingContract.BrightnessSettingTbl.COL_SETTING)));
            int hour =c.getInt(c.getColumnIndexOrThrow(BrightnessSettingContract.BrightnessSettingTbl.COL_HOUR));
            int minute = c.getInt(c.getColumnIndexOrThrow(BrightnessSettingContract.BrightnessSettingTbl.COL_MINUTE));
            Boolean onOff =c.getInt(c.getColumnIndexOrThrow(BrightnessSettingContract.BrightnessSettingTbl.COL_ON))>0;

            if(context instanceof AppCompatActivity){
                brightnessCommands.add(new BrightnessCommand(
                    bs,
                    hour,
                    minute,
                    onOff,(AppCompatActivity) context));
            }
            else{
                brightnessCommands.add(new BrightnessCommand(
                        bs,
                        hour,
                        minute,
                        onOff));
            }
        }

        c.close();
    }
}
