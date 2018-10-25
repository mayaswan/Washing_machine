package com.example.ruchadapong.projectapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class ServiceFragment extends Fragment {

    Button btnOn,btnOff,btnConnect;

    ListView list_devices;

    BluetoothAdapter mybluetoothAdapter;
    int requestCodeForEnable;
    int REQUEST_CONNECT_DEVICE;

    Intent btEnable,ListDevice;

    boolean connect;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnOn = getView().findViewById(R.id.btnOn);
        btnOff = getView().findViewById(R.id.btnOff);
        btnConnect = getView().findViewById(R.id.btnConnect);
        mybluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestCodeForEnable = 1;
        REQUEST_CONNECT_DEVICE = 2;
        connect = false;

        bluetoothON();
        bluetoothOFF();
        bluetoothConnect();

        createToolbar();
    }

    private void bluetoothConnect() {

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (connect) {

                //Disconnect

                }else {
                    // Connected

                    ListDevice = new Intent(getActivity(),DeviceList.class);
                    startActivityForResult(ListDevice,REQUEST_CONNECT_DEVICE);

                }


            }
        });

    }

    private void bluetoothOFF() {

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mybluetoothAdapter.isEnabled()){

                    mybluetoothAdapter.disable();
                    Toast.makeText(getActivity(),"Bluetooth OFF",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void bluetoothON() {

        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mybluetoothAdapter == null) {

                    Toast.makeText(getActivity(),"Bluetooth OFF",Toast.LENGTH_LONG).show();

                }else {
                    if (!mybluetoothAdapter.isEnabled()) {

                    startActivityForResult(btEnable,requestCodeForEnable);

                    }
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == requestCodeForEnable){

            if (resultCode == Activity.RESULT_OK) {

                Toast.makeText(getActivity(),"Bluetooth ON",Toast.LENGTH_LONG).show();

            } else if (resultCode == Activity.RESULT_CANCELED) {

                Toast.makeText(getActivity(),"Bluetooth Cancel",Toast.LENGTH_LONG).show();

            }

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
