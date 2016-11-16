package com.dandabby.lelux;

import android.provider.BaseColumns;

/**
 * Created by ALL on 10/5/2016.
 */

public class BrightnessSettingContract {
    //to prevent accidental instanrtiation of the class, make constructor private
    private BrightnessSettingContract(){}

    /*Inner class defining table*/
    /*implementing */
    public static class BrightnessSettingTbl implements BaseColumns {
        public static final String TABLE_NAME="entry";
        public static final String COL_SETTING="setting";
        public static final String COL_HOUR="hour";
        public static final String COL_MINUTE="minute";
        public static final String COL_ON="switcheOn";
    }

    private static final String TEXT_TYPE = " TEXT ";
    private static final String INTEGER_TYPE = "  ";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + BrightnessSettingTbl.TABLE_NAME + "  ( " +
                    BrightnessSettingTbl._ID + " INTEGER PRIMARY KEY," +
                    BrightnessSettingTbl.COL_SETTING + TEXT_TYPE + ", " +
                    BrightnessSettingTbl.COL_HOUR + INTEGER_TYPE + ", " +
                    BrightnessSettingTbl.COL_MINUTE + INTEGER_TYPE + ", " +
                    BrightnessSettingTbl.COL_ON + INTEGER_TYPE + " ) ";

    public static final String SQL_DELETE_ENTRIES = " DROP TABLE IF EXISTS "  + BrightnessSettingTbl.TABLE_NAME;


}

