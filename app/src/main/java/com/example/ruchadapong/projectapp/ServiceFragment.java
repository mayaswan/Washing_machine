package com.example.ruchadapong.projectapp;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class ServiceFragment extends Fragment {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_CONBECT_BT = 2;

    BluetoothDevice myDevice = null;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket mySocket = null;

    boolean connect = false;

    UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private  static String MAC = null;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {

            Toast.makeText(getActivity(),"Device Not Supported",Toast.LENGTH_LONG).show();
            getActivity().finish();
        }

        final Button button = getView().findViewById(R.id.btnOn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bluetoothAdapter.isEnabled()){

                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent,REQUEST_ENABLE_BT);
                }else {
                    Toast.makeText(getActivity(),"Bluetooth is already on",Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
            }
        });

        final Button button1 = getView().findViewById(R.id.btnOff);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.disable();
                    Toast.makeText(getActivity(),"TURN OFF",Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }else {
                    Toast.makeText(getActivity(),"Bluetooth is already off",Toast.LENGTH_LONG).show();

                }

            }
        });

        final Button button2 = getView().findViewById(R.id.btnConnect);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (connect){

                    try {
                        mySocket.close();
                        connect = false;
                        button2.setText("Connected");
                        Toast.makeText(getActivity(),"Bluetooth for disconnected",Toast.LENGTH_LONG).show();

                    }catch (IOException error1){

                        Toast.makeText(getActivity(),"Error" + error1,Toast.LENGTH_LONG).show();

                    }

                    //Disconnect

                }else {
                    //Connect
                    Intent intent = new Intent(getActivity(),ListDevice.class);
                    startActivityForResult(intent,REQUEST_CONBECT_BT);
                }

            }
        });


        createToolbar();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){

            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getActivity(),"0 bluetooth for Active",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getActivity(),"0 bluetooth , 0 app", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
                break;

            case REQUEST_CONBECT_BT:
                if (resultCode == Activity.RESULT_OK) {

                    MAC = data.getExtras().getString(ListDevice.MAC_Adress);
//                    Toast.makeText(getActivity(),"MAC FINAL",Toast.LENGTH_LONG).show();
                    myDevice = myBluetooth.getRemoteDevice(MAC);

                    try {
                        mySocket = myDevice.createRfcommSocketToServiceRecord(myUUID);

                        mySocket.connect();

                        connect = true;

                        Button button2 = getView().findViewById(R.id.btnConnect);
                        button2.setText("Disconnect");

                        Toast.makeText(getActivity(),"Connect Success",Toast.LENGTH_LONG).show();

                    }catch (IOException error){
                        connect = false;
                        Toast.makeText(getActivity(),"Non Connect Success" + error,Toast.LENGTH_LONG).show();
                    }

                }else {

                    Toast.makeText(getActivity(),"0 MACaddress",Toast.LENGTH_LONG).show();

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
