package com.example.vcare.vcare.Service;

import android.app.AlertDialog;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.vcare.vcare.R;
import com.example.vcare.vcare.activity.DialogActivity;
import com.example.vcare.vcare.activity.MainActivity;
import com.example.vcare.vcare.dao.Alert;
import com.example.vcare.vcare.dao.CarDetail;
import com.example.vcare.vcare.dao.CarDetailManager;
import com.example.vcare.vcare.dao.UserDetail;
import com.example.vcare.vcare.dao.WarningData;
import com.example.vcare.vcare.database.AppDatabase;
import com.example.vcare.vcare.database.InsertData;
import com.example.vcare.vcare.http.NetworkConnectManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by VCare_kmutt on 1/29/2018.
 */

public class UpdateDataService extends IntentService {
    private AppDatabase mDb;
    private CarDetail temp,data;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    String text;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public UpdateDataService(){
        super("");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bundle extras = intent.getExtras();
        int flag = extras.getInt("flag");
        Log.d("flag",String.valueOf(flag));
        sendDataToServer(flag);
    }

    public void sendDataToServer(int flag)
    {
        text = "1 01-07-E1-06\r\n"+
                "4 120\r\n" +
                "5 90\r\n" +
                "6 0.78\r\n" +
                "7 1.56\r\n" +
                "11 28\r\n" +
                "12 747\r\n" +
                "13 0\r\n" +
                "14 4\r\n" +
                "15 54\r\n" +
                "16 2\r\n" +
                "17 13.73\r\n" +
                "21 0 -2\r\n" +
                "31 1521\r\n" +
                "33 14\r\n" +
                "44 0\r\n" +
                "45 -100\r\n" +
                "46 23.14\r\n" +
                "47 50.59\r\n" +
                "48 255\r\n" +
                "49 12525\r\n" +
                "51 0\r\n" +
                "52 0 -256\r\n" +
                "60 465\r\n" +
                "66 13\r\n" +
                "67 28\r\n" +
                "68 0\r\n" +
                "69 4.71\r\n" +
                "71 31.76\r\n" +
                "73 19.22\r\n" +
                "74 9.41\r\n" +
                "76 4.71\r\n" +
                "81 03\r\n" +
                "82 -1\r\n" +
                "DTC: P-1\r\n" +
                "Lat: -1\r\n" +
                "Long: -1\r\n" +
                "Distance: -1\r\n";
        //data = new CarDetail(text);
         shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
         editor = shared.edit();
        Gson gson = new Gson();
        String json = shared.getString("dataBT", "");
        if(flag == 1)
        {
            data = gson.fromJson(json, CarDetail.class);
            /*editor.putString("dcMaf",data.getPid16());
            editor.putString("dcEGR",data.getPid45());
            editor.putString("dcEngineLoad",data.getPid4());
            editor.putString("dcFuelLevel",data.getPid47());
            editor.putString("dcTemp",data.getPid5());
            editor.putString("dcThrottle",data.getPid17());
            editor.commit();*/
            connectServer();
            //data = new CarDetail(text);
        }
    }
    private void connectServer()
    {
        Call<WarningData> call = NetworkConnectManager.getInstance().getService().loadAlert(data);
        call.enqueue(new Callback<WarningData>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<WarningData> call, Response<WarningData> response) {
                if (response.isSuccessful()) {
                   /* WarningData temp = response.body();
                    List<com.example.vcare.vcare.dao.Response> resdata = temp.getResponse();
                    Log.d("checkAPI",resdata.get(0).getLoad());
                    if(resdata.get(0).getLoad().equals("1")) {
                        String CHANNEL_ID = "my_channel_01";
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(UpdateDataService.this, CHANNEL_ID)
                                            .setSmallIcon(R.drawable.engine)
                                            .setContentTitle("Warning")
                                            .setDefaults(Notification.DEFAULT_SOUND |Notification.DEFAULT_VIBRATE)
                                            .setContentText("Your car has a problem. Check your Engine")
                                            .setAutoCancel(true);
                        NotificationManager mNotificationManager = (NotificationManager)
                                    getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(1100, mBuilder.build());
                        Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivities(new Intent[]{intent});
                    }*/
                }
                else
                {
                    Log.d("CheckAPI", "Error");
                    // Toast.makeText(getApplicationContext(),response.errorBody().toString(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<WarningData> call, Throwable t) {
                Log.d("CheckAPI", "Connection Failed");
                // Toast.makeText(getApplicationContext(),"Connection Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
