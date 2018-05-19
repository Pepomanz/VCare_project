package com.example.vcare.vcare.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.vcare.vcare.R;
import com.example.vcare.vcare.Service.MyBluetoothService;

/**
 * Created by VCare_kmutt on 1/25/2018.
 */

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvShow;
    private EditText etcommand;
    private MyBluetoothService bt;
    String command = "null";
    private Button btnConnect,btnEnableBT,btnSendCommand;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initialize();
    }

    private void initialize() {
        tvShow = (TextView)findViewById(R.id.tvShow);
        btnConnect = (Button)findViewById(R.id.btnConnect);
        btnEnableBT = (Button) findViewById(R.id.btnEnableBT);
        btnSendCommand = (Button) findViewById(R.id.btnSendCommand);
        etcommand = (EditText)findViewById(R.id.etsend);
        btnEnableBT.setOnClickListener(this);
        btnConnect.setOnClickListener(this);
        btnSendCommand.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnConnect)
        {
            SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String macAddress = shared.getString("mac","0000-0000-0000");
            Log.d("check",macAddress);
            Intent intent = new Intent(getApplicationContext(), MyBluetoothService.class);
            intent.putExtra("mac",macAddress);
            startService(intent);
        }
    }
}