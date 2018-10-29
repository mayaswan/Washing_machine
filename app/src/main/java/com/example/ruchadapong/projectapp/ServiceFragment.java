package com.example.ruchadapong.projectapp;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class ServiceFragment extends Fragment {

    Button btnConnect,btnOn,btnOff,numOne,numTwo,numThree,numFour,numFive,numSix,numSeven,numEigth,numNine;

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_CONNECT_BT = 2;
    private static final int MESSAGE_READ = 3;

    Handler mHandler;
    StringBuilder stringBuilder = new StringBuilder();

    ConnectedThread connectedThread;

    BluetoothAdapter myBluetoothAdapter = null;
    BluetoothDevice myBluetoothDevice = null;
    BluetoothSocket myBluetoothSocket = null;

    boolean ConnectBT = false;
    private static String MAC = null;

    UUID myUUID = UUID.fromString("4753d1c1-9d00-4435-b4af-994a417ed995");


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        btnConnect = getView().findViewById(R.id.btnConnect);
        btnOn = getView().findViewById(R.id.btnOn);
        btnOff = getView().findViewById(R.id.btnOff);
        numOne = getView().findViewById(R.id.numOne);
        numTwo = getView().findViewById(R.id.numTwo);
        numThree = getView().findViewById(R.id.numThree);
        numFour = getView().findViewById(R.id.numFour);
        numFive = getView().findViewById(R.id.numFive);
        numSix = getView().findViewById(R.id.numSix);
        numSeven = getView().findViewById(R.id.numSeven);
        numEigth = getView().findViewById(R.id.numEigth);
        numNine = getView().findViewById(R.id.numNine);

       btnOn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (myBluetoothAdapter == null) {
                   Toast.makeText(getActivity(),"Bluetooth Not Open",Toast.LENGTH_LONG).show();
               } else if (!myBluetoothAdapter.isEnabled()) {
                   Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                   startActivityForResult(enableBT,REQUEST_ENABLE_BT);
               }
           }
       });

       btnOff.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (myBluetoothAdapter.isEnabled()) {

                   myBluetoothAdapter.disable();
                   Toast.makeText(getActivity(),"Bluetooth Disable",Toast.LENGTH_LONG).show();

               }
           }
       });

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ConnectBT){
                    //disconnect

                    try {

                        myBluetoothSocket.close();

                        ConnectBT = false;
                        Toast.makeText(getActivity(),"Bluetooth is Disconnect",Toast.LENGTH_LONG).show();

                    }catch (IOException error){
                        Toast.makeText(getActivity(),"Fail" + error,Toast.LENGTH_LONG).show();
                    }
                }else {
//                    connect
                    Intent intentBT = new Intent(getActivity(),DeviceList.class);
                    startActivityForResult(intentBT,REQUEST_CONNECT_BT);
                }

            }
        });

        numOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ConnectBT) {

                    connectedThread.write("one");

                }else {

                    Toast.makeText(getActivity(),"Bluetooth is Not Connect",Toast.LENGTH_LONG).show();

                }

            }
        });

        numTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ConnectBT) {

                    connectedThread.write("two");

                }else {

                    Toast.makeText(getActivity(),"Bluetooth is Not Connect",Toast.LENGTH_LONG).show();

                }

            }
        });

        numThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ConnectBT) {

                    connectedThread.write("three");

                }else {

                    Toast.makeText(getActivity(),"Bluetooth is Not Connect",Toast.LENGTH_LONG).show();

                }

            }
        });

        numFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ConnectBT) {

                    connectedThread.write("four");

                }else {

                    Toast.makeText(getActivity(),"Bluetooth is Not Connect",Toast.LENGTH_LONG).show();

                }

            }
        });

        numFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ConnectBT) {

                    connectedThread.write("five");

                }else {

                    Toast.makeText(getActivity(),"Bluetooth is Not Connect",Toast.LENGTH_LONG).show();

                }

            }
        });

        numSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ConnectBT) {

                    connectedThread.write("six");

                }else {

                    Toast.makeText(getActivity(),"Bluetooth is Not Connect",Toast.LENGTH_LONG).show();

                }

            }
        });

        numSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ConnectBT) {

                    connectedThread.write("seven");

                }else {

                    Toast.makeText(getActivity(),"Bluetooth is Not Connect",Toast.LENGTH_LONG).show();

                }

            }
        });

        numEigth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ConnectBT) {

                    connectedThread.write("eigth");

                }else {

                    Toast.makeText(getActivity(),"Bluetooth is Not Connect",Toast.LENGTH_LONG).show();

                }

            }
        });

        numNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ConnectBT) {

                    connectedThread.write("nine");

                }else {

                    Toast.makeText(getActivity(),"Bluetooth is Not Connect",Toast.LENGTH_LONG).show();

                }

            }
        });




        createToolbar();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){

            case REQUEST_ENABLE_BT:

                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getActivity(),"Bluetooth Enable",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getActivity(),"Bluetooth Not Enable",Toast.LENGTH_LONG).show();
                }
                break;

            case REQUEST_CONNECT_BT:

                if (resultCode == Activity.RESULT_OK) {

                    MAC = data.getExtras().getString(DeviceList.MAC_Address);


                    myBluetoothDevice = myBluetoothAdapter.getRemoteDevice(MAC);

                    try {

                        myBluetoothSocket = myBluetoothDevice.createRfcommSocketToServiceRecord(myUUID);

                        myBluetoothSocket.connect();

                        ConnectBT = true;

                        connectedThread = new ConnectedThread(myBluetoothSocket);
                        connectedThread.start();

                        Toast.makeText(getActivity(),"Connect Success" + MAC,Toast.LENGTH_LONG).show();

                    }catch (IOException error){

                        ConnectBT =false;

                        Toast.makeText(getActivity(),"Connect Fail" + error,Toast.LENGTH_LONG).show();

                    }


                }else {

                    Toast.makeText(getActivity(),"Fail MAC",Toast.LENGTH_LONG).show();

                }

        }

    }

    private class ConnectedThread extends Thread {

        private final InputStream mmInStream;
        private final OutputStream mmOutStream;


        public ConnectedThread(BluetoothSocket socket) {
            myBluetoothSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {

            }


            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

//             Keep listening to the InputStream until an exception occurs.
//            while (true) {
//                try {
//                    // Read from the InputStream.
//                    numBytes = mmInStream.read(mmBuffer);
//
//                    String dadosBt = new String(mmBuffer , 0 ,numBytes)
//                    // Send the obtained bytes to the UI activity.
//                    mHandler.obtainMessage(MESSAGE_READ,numBytes,-1,dadosBt)
//                            .sendToTarget();
//                } catch (IOException e) {
//
//                    break;
//                }
//            }
        }

        // Call this from the main activity to send data to the remote device.
        public void write(String s) {

            byte[] msgbuffer = s.getBytes();

            try {
                mmOutStream.write(msgbuffer);

//                // Share the sent message with the UI activity.
//                Message writtenMsg = mHandler.obtainMessage(
//                        MessageConstants.MESSAGE_WRITE, -1, -1, mmBuffer);
//                writtenMsg.sendToTarget();
            } catch (IOException e) {
//                Log.e(TAG, "Error occurred when sending data", e);
//
//                // Send a failure message back to the activity.
//                Message writeErrorMsg =
//                        mHandler.obtainMessage(MessageConstants.MESSAGE_TOAST);
//                Bundle bundle = new Bundle();
//                bundle.putString("toast",
//                        "Couldn't send data to the other device");
//                writeErrorMsg.setData(bundle);
//                mHandler.sendMessage(writeErrorMsg);
            }
        }

        // Call this method from the main activity to shut down the connection.

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
