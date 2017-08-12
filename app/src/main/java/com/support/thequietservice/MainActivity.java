package com.support.thequietservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    BluetoothSPP bt;
    String placeid = "place";
    Boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = new BluetoothSPP(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIRequest.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final APIRequest apiRequest = retrofit.create(APIRequest.class);
        bt.setBluetoothStateListener(new BluetoothSPP.BluetoothStateListener() {
            @Override
            public void onServiceStateChanged(int state) {
                if (state == 3)
                    bt.send("1", false);
            }
        });
        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener()

        {
            public void onDeviceConnected(String name, String address) {
            }

            public void onDeviceDisconnected() {
                isConnected = false;
            }

            public void onDeviceConnectionFailed() {
            }
        });
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] data, String message) {
                Log.e("message", message);
                if (isConnected) {
                    apiRequest.Update(placeid, Integer.parseInt(message)).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                } else {
                    placeid = message;
                    isConnected = true;
                }
            }
        });
        bt.setAutoConnectionListener(new BluetoothSPP.AutoConnectionListener() {
            @Override
            public void onAutoConnectionStarted() {
                Log.e("autoconnection", "bluetoothConnect");
            }

            @Override
            public void onNewConnection(String name, String address) {
            }
        });
        Log.e("onCreate", "onCreate");
    }

    public void onDestroy() {
        isConnected = false;
        bt.stopService();
        super.onDestroy();
        Log.e("onDestroy", "onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();
        bt.setupService();
        bt.startService(BluetoothState.DEVICE_OTHER);
        bt.autoConnect("sibal");
        Log.e("onStart", "onStart");
    }
}