package com.dantastic.lelux;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.TextView;

import com.dantastic.lelux.databinding.WidgetViewBinding;

public class MainActivity extends AppCompatActivity  implements CompoundButton.OnCheckedChangeListener{
    private BrightnessCommands brightnessCommands;
    private RecyclerView mRecyclerView;
    private MyWidgetAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(getString(R.string.Tag), "Starting...");
        if (PermissionChecker.PermissionsOk(this)) {
            Log.e(getString(R.string.Tag), "Can write permission (Main)");
        } else {
            Log.e(getString(R.string.Tag), "No write permission (Main)");
            RequestPermissions();
        }
        PopulateBrightnessCommands();

        // WidgetBinding widgetBinding = new WidgetBinding();

        SetUpGridViews();
        SetUpDataBindingView();


        ((SwitchCompat)findViewById(R.id.lightenSwitch)).setOnCheckedChangeListener(this);
        ((SwitchCompat)findViewById(R.id.darkenSwitch)).setOnCheckedChangeListener(this);
    }

    private void PopulateBrightnessCommands(){
        brightnessCommands = new BrightnessCommands();

        SQLiteOpenHelper mDbHelper = new BrightnessSettingDbHelper(this);
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
            Log.e(getString(R.string.Tag), "Loop - " + String.valueOf(i));

            brightnessCommands.Add(new BrightnessCommand(
                    BrightnessSetting.valueOf(c.getString(c.getColumnIndexOrThrow(BrightnessSettingContract.BrightnessSettingTbl.COL_SETTING))),
                    c.getInt(c.getColumnIndexOrThrow(BrightnessSettingContract.BrightnessSettingTbl.COL_HOUR)) ,
                    c.getInt(c.getColumnIndexOrThrow(BrightnessSettingContract.BrightnessSettingTbl.COL_MINUTE)) ,
                    c.getInt(c.getColumnIndexOrThrow(BrightnessSettingContract.BrightnessSettingTbl.COL_ON))>0));
        }

        c.close();
    }

    private void SetUpGridViews(){
        // initialize the grid View
        GridView gridview = (GridView) findViewById(R.id.gv);
        // set Imageadapter in GridView
        //BrightnessSettingAdapter brightnessSettingAdapter= new BrightnessSettingAdapter(this);

        BrightnessSettingAdapter brightnessSettingAdapter=new BrightnessSettingAdapter(this, R.layout.row_view,
                brightnessCommands.brightnessCommands.toArray(new BrightnessCommand[brightnessCommands.brightnessCommands.size()] ));
        //brightnessSettingAdapter.brightnessCommandArray=;
        //((AdapterView<BrightnessSettingAdapter>)brightnessSettingAdapter).setOnItemClickListener
        gridview.setAdapter(brightnessSettingAdapter);
        Log.e(getString(R.string.Tag),"Set listener");

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,int position, long id)
            {
                // All this does is does is raise some debug messages
                Log.e("DebugMessage", "item clicked");
                Log.e("DebugMessage", "    parent: " + parent.toString());
                Log.e("DebugMessage", "    view: " + v.toString());
                Log.e("DebugMessage", "    position: " + Integer.toString(position));
                Log.e("DebugMessage", "    id: " + Long.toString(id));
                // Log.e("DebugMessage", "    getTag: " + v.getTag().toString());;
                //Toast.makeText(getApplicationContext(),
                //      ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                Log.e("DebugMessage", "nought selexted");
            }
        });

    }

    // Binds data to the RecyclerView via the adapter
    private void SetUpDataBindingView(){


        //initialize recycler view
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        //create adapter, passing the data we wish to display
        mAdapter = new MyWidgetAdapter( brightnessCommands.brightnessCommands);

        // attach adapter to recycler view to populate items
        mRecyclerView.setAdapter(mAdapter);



        //set Layout manager to position items
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1,LinearLayoutManager.VERTICAL,false);
        //mRecyclerView.setLayoutManager(gridLayoutManager);


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
        TextView timeText;
        BrightnessCommand brightnessCommand;

        switch(buttonView.getId()){
            case R.id.lightenSwitch:
                timeText= (TextView)findViewById(R.id.lightenTime);
                brightnessCommand = brightnessCommands.GetBrightnessCommand(BrightnessSetting.LIGHTEN);
                break;
            case R.id.darkenSwitch:
                timeText = (TextView)findViewById(R.id.darkenTime);
                brightnessCommand = brightnessCommands.GetBrightnessCommand(BrightnessSetting.DARKEN);
                break;
            default:
                return;
        }

        if( buttonView.isChecked()){
            timeText.setText( "checked");
            showTimePickerDialog(timeText,brightnessCommand);
        }
        else{
            timeText.setText( "unchecked");
            brightnessCommand.setOn(false);
        }
    }

    public void showTimePickerDialog(TextView v, BrightnessCommand brightnessCommand){
        DialogFragment newFragment = new TimePickerFragment(v,brightnessCommand);
        newFragment.show(getSupportFragmentManager(),"time Picker");
    }

    final Integer MY_PERMISSIONS_REQUEST_WRITE_SETTINGS = 343;

    private void RequestPermissions() {
        if (!PermissionChecker.PermissionsOk(this)) {
            try {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)this, Manifest.permission.WRITE_SETTINGS)) {
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    showMessageOKCancel(this, "You need to allow access to write settings for brightness",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions((Activity) getApplicationContext(),
                                            new String[]{Manifest.permission.WRITE_SETTINGS},
                                            MY_PERMISSIONS_REQUEST_WRITE_SETTINGS);
                                }
                            });
                } else {
                    // No explanation needed, we can request the permission./**/

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    if (Build.VERSION.SDK_INT >= 23) {
                        //// TODO: 9/27/2016 how do these permissions work prior to 23?
                        if (!Settings.System.canWrite(this)) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                            intent.setData(Uri.parse("package:" + this.getPackageName()));
                            Log.e(getString(R.string.Tag), " this.getPackageName(): " +  this.getPackageName());
                            startActivity(intent);
                        }
                    }
                }
            } catch (Exception e3) {
                Log.e(getString(R.string.Tag), "Error getting permission");
                e3.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 343:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Message", "onRequestPermissionsResult granted");
                } else {
                    Log.e("Message", "onRequestPermissionsResult denied");
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(Context context, String message, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}