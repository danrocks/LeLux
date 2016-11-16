package com.dandabby.lelux;

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

import com.dandabby.lelux.databinding.WidgetViewBinding;

public class MainActivity extends AppCompatActivity  {
    private com.dandabby.lelux.BrightnessCommands brightnessCommands;
    private RecyclerView mRecyclerView;
    private com.dandabby.lelux.MyWidgetAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(getString(R.string.Tag), "Starting...");
        if (com.dandabby.lelux.PermissionChecker.PermissionsOk(this)) {
            Log.e(getString(R.string.Tag), "Can write permission (Main)");
        } else {
            Log.e(getString(R.string.Tag), "No write permission (Main)");
            RequestPermissions();
        }
        PopulateBrightnessCommands();
        SetUpDataBindingView();
    }

    private void PopulateBrightnessCommands(){
        brightnessCommands = new com.dandabby.lelux.BrightnessCommands(this);
        brightnessCommands.Populate();
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
                            Log.e(getString(R.string.Tag), " ***HOW/WHY was this invoked***");
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
