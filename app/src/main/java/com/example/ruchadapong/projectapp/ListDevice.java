package com.example.ruchadapong.projectapp;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class ListDevice extends ListActivity {

    private BluetoothAdapter bluetoothAdapter2 = null;

    static String MAC_Adress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> ArrayBluetooth = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);

       bluetoothAdapter2 = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> bluetoothDevices = bluetoothAdapter2.getBondedDevices();

        if (bluetoothDevices.size() > 0) {

            for (BluetoothDevice divice : bluetoothDevices){

                String name = divice.getName();
                String MacAddress = divice.getAddress();
                ArrayBluetooth.add(name + "/n" + MacAddress);

            }

        }
        setListAdapter(ArrayBluetooth);


    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String information = ((TextView)v).getText().toString();
        String AddreasMac = information.substring(information.length() - 17);

        Intent returnMac = new Intent();
        returnMac.putExtra(MAC_Adress,AddreasMac);
        setResult(RESULT_OK,returnMac);
        finish();


    }
}
