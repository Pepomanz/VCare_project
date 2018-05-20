package com.example.vcare.vcare.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.vcare.vcare.R;
import com.example.vcare.vcare.Service.FetchData;
import com.example.vcare.vcare.Service.MyAlarmService;
import com.example.vcare.vcare.Service.UpdateDataService;
import com.example.vcare.vcare.database.AppDatabase;
import com.example.vcare.vcare.fragment.MainFragment;
import com.example.vcare.vcare.fragment.MapsFragment;
import com.example.vcare.vcare.fragment.ProfileFragment;
import com.example.vcare.vcare.fragment.SettingFragment;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.Console;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    private Runnable run;
    private Handler handler;
    private Timer timer;
    private SharedPreferences shared;
    private int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            new FetchData(getApplicationContext()).execute();
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, new MainFragment()).commit();
        }
        instantiate();
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void instantiate() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView);
        shared = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
       // FirebaseMessaging.getInstance().subscribeToTopic("Alert");
        setRepeatingAsyncTask();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.item_mainPage:
            {
                new FetchData(MainActivity.this).execute();
                selectedFragment = MainFragment.newInstance();
                break;
            }
            case R.id.item_setting: {
                selectedFragment = new SettingFragment();
                break;
            }
            case R.id.item_maps:
            {
                selectedFragment = MapsFragment.newInstance();
                break;
            }
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentContainer, selectedFragment);
        transaction.commit();
        return true;
    }

    private void setRepeatingAsyncTask() {
        state = shared.getInt("state",0);
        handler = new Handler(Looper.getMainLooper());
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                run = new Runnable() {
                    public void run() {
                        try {
                            Intent i = new Intent(MainActivity.this, UpdateDataService.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("flag",state);
                            i.putExtras(bundle);
                            startService(i);
                        } catch (Exception e) {
                            Log.d("error", "error at handle");
                            // error, do something
                        }
                    }
                };
                handler.post(run);
            }
        };
        timer.schedule(task, 0, 10 * 1000);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        handler.removeCallbacksAndMessages(null);
        super.onStop();
    }
}
