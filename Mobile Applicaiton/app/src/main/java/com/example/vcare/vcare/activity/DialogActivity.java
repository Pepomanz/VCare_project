package com.example.vcare.vcare.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.example.vcare.vcare.R;

/**
 * Created by PepoManZ on 2/16/2018.
 */

public class DialogActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder alert = new AlertDialog.Builder(DialogActivity.this);
        alert.setTitle("Hot Promotion !");
        alert.setMessage("Our tire discount now 20%.Don't forget to come our service");
        alert.setIcon(R.drawable.ic_car);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Your action here
                finish();
            }
        });

        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }
                });
        alert.show();
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }

}
