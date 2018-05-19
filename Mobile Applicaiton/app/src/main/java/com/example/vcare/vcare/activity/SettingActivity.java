package com.example.vcare.vcare.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.vcare.vcare.R;
import com.example.vcare.vcare.fragment.SettingFragment;

/**
 * Created by VCare_kmutt on 1/25/2018.
 */


public class SettingActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getActionBar().setTitle("Setting");
        getActionBar().setDisplayHomeAsUpEnabled(true);
       /* if(savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.settingContents, new SettingFragment())
                    .commit();
        }*/
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }
}