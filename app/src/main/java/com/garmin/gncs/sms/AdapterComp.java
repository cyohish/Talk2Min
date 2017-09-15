package com.garmin.gncs.sms;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cyoh on 9/12/17.
 */

public class AdapterComp extends RecyclerView.Adapter<AdapterComp.ViewHolder> {
    private static final String TAG = "AdapterComp";
    private ArrayList<BluetoothDevice> bluetoothDevices;
    private OnBluetoothClickListener listener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;

        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
//            mTextView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.e(TAG, "onClick:");
//                }
//            });
        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterComp(ArrayList<BluetoothDevice> bluetoothDevices, OnBluetoothClickListener listener) {
        this.bluetoothDevices = bluetoothDevices;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterComp.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);

        ViewHolder vh = new ViewHolder((TextView) v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(bluetoothDevices.get(position).getName());
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + position);
//                        //This is the previous selection
//                        notifyItemChanged(position);
//                        itemViewHolder.getItemSelectionIndicator().setSelected(true);
//                        mSelectedPosition = position;
//                        //This is the new selection
//                        notifyItemChanged(position);

                BluetoothDevice bluetoothDevice =  bluetoothDevices.get(position);
                Log.v(TAG, "bluetoothDevice = "+bluetoothDevice.getName());

                AdapterComp.this.listener.onBluetoothClick(bluetoothDevice);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return bluetoothDevices.size();
    }
}


