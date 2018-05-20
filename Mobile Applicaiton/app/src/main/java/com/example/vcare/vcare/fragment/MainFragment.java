package com.example.vcare.vcare.fragment;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.vcare.vcare.R;
import com.example.vcare.vcare.Service.MyBluetoothService;
import com.example.vcare.vcare.Service.UpdateDataService;
import com.example.vcare.vcare.activity.DialogActivity;
import com.example.vcare.vcare.activity.LoginActivity;
import com.example.vcare.vcare.activity.MainActivity;
import com.example.vcare.vcare.activity.SettingActivity;
import com.example.vcare.vcare.activity.SummaryActivity;
import com.example.vcare.vcare.dao.MainPageDetail;
import com.example.vcare.vcare.database.AppDatabase;
import com.example.vcare.vcare.http.NetworkConnectManager;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


/**
 * Created by VCare_kmutt on 1/25/2018.
 */

public class MainFragment extends Fragment implements View.OnClickListener {
    @Nullable
    private PieChart fuel,battery,mileage;
    private CardView cdSummary,cdCarDetail;
    private int flag = 0;
    private ImageView profileImage;
    public String leftData,rightData,centerData,fuelLevel= "",mileageData= "",batterVoltage= "",picLink;
    private TextView tvLeft,tvCenter,tvRight;
    private SharedPreferences shared;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_main,container, false);
        initInstance(rootview);
        return  rootview;
    }

    private void initInstance(View rootview) {
        fuel = (PieChart) rootview.findViewById(R.id.fuelChart);
        battery = (PieChart) rootview.findViewById(R.id.battery);
        mileage = (PieChart) rootview.findViewById(R.id.mileage);
        profileImage = (ImageView) rootview.findViewById(R.id.profileImage);
        cdCarDetail = (CardView) rootview.findViewById(R.id.cdCarDetail);
        cdSummary = (CardView) rootview.findViewById(R.id.cdSummary);
        tvCenter = (TextView) rootview.findViewById(R.id.tvCenterData);
        tvLeft = (TextView) rootview.findViewById(R.id.tvLeftData);
        tvRight = (TextView) rootview.findViewById(R.id.tvCenterData);
        cdSummary.setOnClickListener(this);
        cdCarDetail.setOnClickListener(this);
        profileImage.setOnClickListener(this);
        shared = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String picture = shared.getString("picture","");
        leftData = shared.getString("fuelData","");
        rightData = shared.getString("mileageData","");
        centerData = shared.getString("batteryData","");
        Log.d("pepo","fuelLevel"+leftData);
        setPieChart(fuel,leftData);
        setPieChart(mileage,rightData);
        setPieChart(battery,centerData);
        Log.d("MainPage",fuelLevel);
        Glide.with(getContext())
                .load(picture)
                .apply(RequestOptions.circleCropTransform())
                .into(profileImage);
    }

    @Override
    public void onClick(View v) {
        if(v == cdCarDetail)
        {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, new CardetailFragment()).commit();
        }
        else if (v == cdSummary)
        {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, new SummaryFragment()).commit();
        }
        else if (v == profileImage)
        {
        }
        else if(v == tvRight)
        {
            fetchData();
        }
    }

    public void setNotification (){
        String CHANNEL_ID = "my_channel_01";
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.engine)
                        .setContentTitle("Hot promotion")
                        .setDefaults(Notification.DEFAULT_SOUND |Notification.DEFAULT_VIBRATE)
                        .setContentText("Our tire discount now 20%.Don't forget to come our service")
                        .setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager)
                getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1100, mBuilder.build());
        Intent intent = new Intent(getContext(), DialogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivities(new Intent[]{intent});
    }

    public void fetchData(){
        Call<MainPageDetail> call = NetworkConnectManager.getInstance().getService().getMainPage();
        call.enqueue(new Callback<MainPageDetail>(){
            @Override
            public void onResponse(Call<MainPageDetail> call, Response<MainPageDetail> response) {
                MainPageDetail temp = response.body() ;
                if (response.isSuccessful()) {
                    fuelLevel = temp.getFuelLevel();
                    mileageData = temp.getMileage();
                    batterVoltage = temp.getVoltage();
                    picLink = temp.getPicture();
                    Log.d("MainPage", "hello"+fuelLevel);
                }
                else
                {
                    Log.d("MainPage", response.toString());
                }
            }

            @Override
            public void onFailure(Call<MainPageDetail> call, Throwable t) {
                Log.d("MainPage", t.toString());
            }
        });
    }
    private void setPieChart(PieChart chart,String value) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(18.5f, ""));
        entries.add(new PieEntry(26.7f, ""));
        PieDataSet dataset = new PieDataSet(entries, "");
        dataset.setSelectionShift(10);
        dataset.setDrawValues(false);
        dataset.setValueTextSize(5);
        dataset.setValueFormatter(new PercentFormatter());
        dataset.setColors(new int[] { R.color.colorBgRegis, R.color.colorIcDefault}, getContext());
        PieData data = new PieData(dataset);
        chart.setData(data);
        chart.setCenterText(value);
        chart.setDrawSlicesUnderHole(true);
        chart.setHoleRadius(80.0f);
        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(255);
        chart.setDescription(null);
        chart.getLegend().setEnabled(false);
        chart.notifyDataSetChanged();
        chart.invalidate();
    }
}