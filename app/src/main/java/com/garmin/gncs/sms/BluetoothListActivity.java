package com.garmin.gncs.sms;

import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

public class BluetoothListActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener, OnBluetoothClickListener, DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {
    private static final String TAG = "BluetoothListActivity";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate:");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.bluetooth_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)


        mAdapter = new AdapterComp(new ArrayList<>(Util.getBondedDevices()), this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(this);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.v(TAG, "onInterceptTouchEvent: rv" + rv + ", e = " + e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.v(TAG, "onTouchEvent: rv" + rv + ", e = " + e);
        if (e.getAction() ==  MotionEvent.ACTION_UP) {

        }
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        Log.v(TAG, "onRequestDisallowInterceptTouchEvent: disallowIntercept" + disallowIntercept);
    }

    @Override
    public void onBluetoothClick(BluetoothDevice bluetoothDevice) {
        Log.v(TAG, "onBluetoothClick: bluetoothDevice = "+bluetoothDevice.getName());
        showDialog(bluetoothDevice);
    }

    private void showDialog(final BluetoothDevice bluetoothDevice) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(bluetoothDevice.getName());
        builder.setMessage(R.string.message_dialog_select_garmin);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Util.setGarminName(bluetoothDevice.getName());
                startActivity(new Intent(BluetoothListActivity.this, SettingsActivity.class));
                BluetoothListActivity.this.finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog dialog = builder.create();

        dialog.setOnDismissListener(this);
        dialog.setOnCancelListener(this);

        dialog.show();
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        Log.v(TAG, "onCancel");
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        Log.v(TAG, "onDismiss");
    }
}
