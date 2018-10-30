package com.example.ruchadapong.projectapp;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class ServiceFragment extends Fragment {

    private static final String TAG = "ServiceFragment";

    Button btnConnect,btnOnOff,btnDiscovery,numOne,numTwo,numThree,numFour,numFive,numSix,numSeven,numEigth,numNine;

    BluetoothAdapter mBluetoothAdapter;
    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    public DeviceList mDeviceListAdapter;
    ListView lvNewDevices;

    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)){
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,mBluetoothAdapter.ERROR);

                switch (state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };
    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED)){
                final int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE,mBluetoothAdapter.ERROR);

                switch (mode){
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Enable");
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disable. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disable. Not Able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "mBroadcastReceiver2: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "mBroadcastReceiver2: Connected.");
                        break;
                }
            }
        }
    };

    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final  String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                mDeviceListAdapter = new DeviceList(context, R.layout.device_adapter_view, mBTDevices);
                lvNewDevices.setAdapter(mDeviceListAdapter);
                mBTDevices = new ArrayList<>();
            }

        }
    };


    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: called");
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver1);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();



        btnConnect = getView().findViewById(R.id.btnConnect);
        btnOnOff = getView().findViewById(R.id.btnOnOff);
        btnDiscovery = getView().findViewById(R.id.btnDiscovery);
        numOne = getView().findViewById(R.id.numOne);
        numTwo = getView().findViewById(R.id.numTwo);
        numThree = getView().findViewById(R.id.numThree);
        numFour = getView().findViewById(R.id.numFour);
        numFive = getView().findViewById(R.id.numFive);
        numSix = getView().findViewById(R.id.numSix);
        numSeven = getView().findViewById(R.id.numSeven);
        numEigth = getView().findViewById(R.id.numEigth);
        numNine = getView().findViewById(R.id.numNine);


        btnOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: enabling/disabling bluetooth");

                enableDisableBT();

            }
        });

        btnDiscovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: Making device discoverable for 300 seconds.");

                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
                startActivity(discoverableIntent);

                IntentFilter intentFilter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
                getActivity().registerReceiver(mBroadcastReceiver2,intentFilter);
                Toast.makeText(getActivity(),"discoverable for 300 seconds.",Toast.LENGTH_LONG).show();

            }
        });

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mBluetoothAdapter.isDiscovering()) {
                    
                    mBluetoothAdapter.cancelDiscovery();
                    Log.d(TAG, "onClick: Canceling Discovery");



                    mBluetoothAdapter.startDiscovery();
                    IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    getActivity().registerReceiver(mBroadcastReceiver3,discoverDevicesIntent);
                }
                if (!mBluetoothAdapter.isDiscovering()) {



                    mBluetoothAdapter.startDiscovery();
                    IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    getActivity().registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
                }

            }
        });






        createToolbar();
    }


    private void enableDisableBT() {

        if (mBluetoothAdapter == null){
            Log.d(TAG, "enableDisableBT: Does not have BT");
            Toast.makeText(getActivity(),"Bluetooth is Not Open",Toast.LENGTH_LONG).show();
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "enableDisableBT: enabling BT.");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            getActivity().registerReceiver(mBroadcastReceiver1,BTIntent);
            Toast.makeText(getActivity(),"Bluetooth is Enable",Toast.LENGTH_LONG).show();
        }
        if (mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "enableDisableBT: disabling BT.");
            mBluetoothAdapter.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            getActivity().registerReceiver(mBroadcastReceiver1,BTIntent);
            Toast.makeText(getActivity(),"Bluetooth is Disable",Toast.LENGTH_LONG).show();

        }



    }


    private void createToolbar() {

        Toolbar toolbar = getView().findViewById(R.id.toolbarService);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Bluetooth Controller");


        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service,container,false);
        return view;
    }
}
