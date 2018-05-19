package com.example.vcare.vcare.Service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.vcare.vcare.dao.CarDetail;
import com.example.vcare.vcare.database.AppDatabase;
import com.example.vcare.vcare.database.InsertData;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by VCare_kmutt on 1/25/2018.
 */

public class MyBluetoothService extends Service{

    final int handlerState = 0;
    private static Handler bluetoothIn;
    private BluetoothAdapter btAdapter = null;
    private String text;
    private ConnectingThread mConnectingThread;
    private ConnectedThread mConnectedThread;
    private boolean stopThread;
    private String index[]={"1","4","6","7","11","12","13","14","15","16","17","21","31","33","44","45","46","47",
    "48","49","51","52","60","66","67","68","69","71","73","74","76","81","82","DTC:","Long:","Lat:","Distance:"};
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private String [] var = {};
    private String [] buffer = new String[37];
    private String MAC_ADDRESS ="" ;
    private StringBuilder recDataString = new StringBuilder();
    private StringBuilder recvDataString = new StringBuilder();
    private AppDatabase mDb;
    private int flag;

    @Override
    public void onCreate()
    {
        super.onCreate();
        flag = 0;
        //mDb = AppDatabase.getINSTANCE(getApplicationContext());
        Toast.makeText(getApplicationContext(),"Service started",Toast.LENGTH_LONG).show();
        Log.d("BT SERVICE", "SERVICE CREATED");
        stopThread = false;
    }

    @SuppressLint("HandlerLeak")
    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        MAC_ADDRESS = intent.getStringExtra("mac");
        bluetoothIn = new Handler(){
            public void handleMessage(android.os.Message msg) {
                Log.d("DEBUG", "handleMessage");
                if (msg.what == handlerState) {
                    byte [] result = (byte []) msg.obj;//if message is what we want
                    String readMessage = new String(result, 0, result.length);
                   // String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                    String[] data = readMessage.split("\\s+");
                    Log.d("DEBUG BT",readMessage);
                    if (data[0].equals("1")){
                        flag = 1;
                    }
                    if(flag == 1){
                        recDataString.append(readMessage);
                        recDataString.append("\r\n");
                        if (data[0].equals("Dist:"))
                        {
                            text = recDataString.toString();
                            Log.d("keytext",text);
                        /*InsertData insert = new InsertData(text);
                        insert.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,mDb);*/
                            SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = shared.edit();
                            Gson gson = new Gson();
                            String obj = gson.toJson(new CarDetail(text));
                            editor.putString("dataBT",obj);
                            editor.commit();
                            recDataString.delete(0, recDataString.length());
                        }
                        Log.d("RECORDED", recDataString.toString());
                    }
                }
               // recDataString.delete(0, recDataString.length());                    //clear all string data
            }
        };
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Toast.makeText(getApplicationContext(),"Service stopped",Toast.LENGTH_LONG).show();
        bluetoothIn.removeCallbacksAndMessages(null);
        stopThread = true;
        if (mConnectedThread != null) {
            mConnectedThread.closeStreams();
        }
        if (mConnectingThread != null) {
            mConnectingThread.closeSocket();
        }
        Log.d("SERVICE", "onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void checkData(String data){
        int i;
        int param = -1;
        for (i=0;i<index.length;i++)
        {
            if(data.equals(index[i]))
            {
                param =  i;
            }
        }
        if(param != -1)
        {
            buffer[param] = data;

        }

    }
    private void checkBTState() {

        if (btAdapter == null) {
            stopSelf();
        } else {
            if (btAdapter.isEnabled()) {
                Log.d("DEBUG BT", "BT ENABLED! BT ADDRESS : " + btAdapter.getAddress() + " , BT NAME : " + btAdapter.getName());
                try {
                    BluetoothDevice device = btAdapter.getRemoteDevice(MAC_ADDRESS);
                    Log.d("DEBUG BT", "ATTEMPTING TO CONNECT TO REMOTE DEVICE : " + MAC_ADDRESS);
                    mConnectingThread = new ConnectingThread(device);
                    mConnectingThread.start();
                } catch (IllegalArgumentException e) {
                    Log.d("DEBUG BT", "PROBLEM WITH MAC ADDRESS : " + e.toString());
                    Log.d("BT SEVICE", "ILLEGAL MAC ADDRESS, STOPPING SERVICE");
                    stopSelf();
                }
            } else {
                Log.d("BT SERVICE", "BLUETOOTH NOT ON, STOPPING SERVICE");
                stopSelf();
            }
        }
    }

    // New Class for Connecting Thread
    private class ConnectingThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectingThread(BluetoothDevice device) {
            Log.d("DEBUG BT", "IN CONNECTING THREAD");
            mmDevice = device;
            BluetoothSocket temp = null;
            Log.d("DEBUG BT", "MAC ADDRESS : " + MAC_ADDRESS);
            Log.d("DEBUG BT", "BT UUID : " + BTMODULEUUID);
            try {
                temp = mmDevice.createRfcommSocketToServiceRecord(BTMODULEUUID);
                Log.d("DEBUG BT", "SOCKET CREATED : " + temp.toString());
            } catch (IOException e) {
                Log.d("DEBUG BT", "SOCKET CREATION FAILED :" + e.toString());
                Log.d("BT SERVICE", "SOCKET CREATION FAILED, STOPPING SERVICE");
                stopSelf();
            }
            mmSocket = temp;
        }

        @Override
        public void run() {
            super.run();
            Log.d("DEBUG BT", "IN CONNECTING THREAD RUN");
            // Establish the Bluetooth socket connection.
            // Cancelling discovery as it may slow down connection
            btAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
                Log.d("DEBUG BT", "BT SOCKET CONNECTED");
                mConnectedThread = new ConnectedThread(mmSocket);
                mConnectedThread.start();
                Log.d("DEBUG BT", "CONNECTED THREAD STARTED");
                //I send a character when resuming.beginning transmission to check device is connected
                //If it is not an exception will be thrown in the write method and finish() will be called
            } catch (IOException e) {
                try {
                    Log.d("DEBUG BT", "SOCKET CONNECTION FAILED : " + e.toString());
                    Log.d("BT SERVICE", "SOCKET CONNECTION FAILED, STOPPING SERVICE");
                    mmSocket.close();
                    stopSelf();
                } catch (IOException e2) {
                    Log.d("DEBUG BT", "SOCKET CLOSING FAILED :" + e2.toString());
                    Log.d("BT SERVICE", "SOCKET CLOSING FAILED, STOPPING SERVICE");
                    stopSelf();
                    //insert code to deal with this
                }
            } catch (IllegalStateException e) {
                Log.d("DEBUG BT", "CONNECTED THREAD START FAILED : " + e.toString());
                Log.d("BT SERVICE", "CONNECTED THREAD START FAILED, STOPPING SERVICE");
                stopSelf();
            }
        }

        public void closeSocket() {
            try {
                mmSocket.close();
            } catch (IOException e2) {
                Log.d("DEBUG BT", e2.toString());
                Log.d("BT SERVICE", "SOCKET CLOSING FAILED, STOPPING SERVICE");
                stopSelf();
            }
        }
    }
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d("DEBUG BT", "IN CONNECTED THREAD");
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.d("DEBUG BT", e.toString());
                Log.d("BT SERVICE", "UNABLE TO READ/WRITE, STOPPING SERVICE");
                stopSelf();
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.d("DEBUG BT", "IN CONNECTED THREAD RUN");
            byte[] buffer = new byte[1024];
            ArrayList<Integer> arr_byte = new ArrayList<Integer>();

            int bytes;

            // Keep looping to listen for received messages
            while (true && !stopThread) {
                try {
                    int data = mmInStream.read();
                    if(data == 0x0A)
                    {}
                    else if(data == 0x0D) {
                        buffer = new byte[arr_byte.size()];
                        for(int i = 0 ; i < arr_byte.size() ; i++) {
                            buffer[i] = arr_byte.get(i).byteValue();
                        }
                        bluetoothIn.obtainMessage(handlerState
                                , buffer.length, -1, buffer).sendToTarget();
                        arr_byte = new ArrayList<Integer>();
                    } else {
                        arr_byte.add(data);
                    }
                } catch (IOException e) {
                    Log.d("DEBUG BT", e.toString());
                    Log.d("BT SERVICE", "UNABLE TO READ/WRITE, STOPPING SERVICE");
                    stopSelf();
                    break;
                }
            }
        }

        //write method
        public void write(byte [] input){
            try {
                mmOutStream.write(input);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Log.d("DEBUG BT", "UNABLE TO READ/WRITE " + e.toString());
                Log.d("BT SERVICE", "UNABLE TO READ/WRITE, STOPPING SERVICE");
                stopSelf();
            }
        }

        public void closeStreams() {
            try {
                //Don't leave Bluetooth sockets open when leaving activity
                mmInStream.close();
                mmOutStream.close();
            } catch (IOException e2) {
                //insert code to deal with this
                Log.d("DEBUG BT", e2.toString());
                Log.d("BT SERVICE", "STREAM CLOSING FAILED, STOPPING SERVICE");
                stopSelf();
            }
        }
    }

}
