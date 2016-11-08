package com.dantastic.lelux;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ALL on 10/5/2016.
 */

// SQLLiteOpenHelper has useful api to aid database access.
// Long-running ops (create and update) are only called when needed, not at app start up
public class BrightnessSettingDbHelper extends SQLiteOpenHelper {
    // If the data base schema is changed, you must increment the database version.
    public static final int DATABASE_VERSION =1;
    public static final String DATABASE_NAME ="BrightnessSetting.db";

    Context ctx;

    public BrightnessSettingDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx=context;
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(BrightnessSettingContract.SQL_CREATE_ENTRIES);
        ContentValues cv = new ContentValues();

        BrightnessSetting bsl = BrightnessSetting.LIGHTEN;
        cv.put(BrightnessSettingContract.BrightnessSettingTbl.COL_SETTING, bsl.name());
        cv.put(BrightnessSettingContract.BrightnessSettingTbl.COL_HOUR, 9);
        cv.put(BrightnessSettingContract.BrightnessSettingTbl.COL_MINUTE, 19);
        cv.put(BrightnessSettingContract.BrightnessSettingTbl.COL_ON,false);

        db.beginTransaction();
        long l1 = db.insert(BrightnessSettingContract.BrightnessSettingTbl.TABLE_NAME, BrightnessSettingContract.BrightnessSettingTbl.COL_MINUTE,cv );
        db.setTransactionSuccessful();
        db.endTransaction();
        Log.e(ctx.getString(R.string.Tag), "First index: " + String.valueOf(l1));

        cv = new ContentValues();
        BrightnessSetting bsd = BrightnessSetting.DARKEN;
        cv.put(BrightnessSettingContract.BrightnessSettingTbl.COL_SETTING, bsd.name());
        cv.put(BrightnessSettingContract.BrightnessSettingTbl.COL_HOUR, 10);
        cv.put(BrightnessSettingContract.BrightnessSettingTbl.COL_MINUTE, 20);
        cv.put(BrightnessSettingContract.BrightnessSettingTbl.COL_ON,false);

        db.beginTransaction();
        long l2 = db.insert(BrightnessSettingContract.BrightnessSettingTbl.TABLE_NAME, BrightnessSettingContract.BrightnessSettingTbl.COL_MINUTE,cv );
        Log.e(ctx.getString(R.string.Tag), "Second index: " + String.valueOf(l2));
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // todo For the moment, if db is upgraded just bin the old version.
        // Make no effort to copy foward data.
        db.execSQL(BrightnessSettingContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}