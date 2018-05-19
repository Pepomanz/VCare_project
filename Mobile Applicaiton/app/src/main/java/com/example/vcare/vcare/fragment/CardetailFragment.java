package com.example.vcare.vcare.fragment;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.example.vcare.vcare.CustomView.DashBoard;
import com.example.vcare.vcare.CustomView.DetailCard;
import com.example.vcare.vcare.R;
import com.example.vcare.vcare.adapter.DetailCardAdapter;
import com.example.vcare.vcare.dao.CarDetail;
import com.example.vcare.vcare.dao.CarDetailManager;
import com.google.gson.Gson;

import java.util.ArrayList;


public class CardetailFragment extends Fragment {
    private DetailCard dcSpeed,dcRPM,dcMaf,dcEGR,dcEngineLoad,dcThrottle,dcTemp,dcFuelLevel;
    private String dcMafData,dcEGRData,dcEngineLoadData,dcThrottleData,dcTempData,dcFuelLevelData;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    private CarDetail data;
    private Handler mHandler;
    private String defValue;
    private ArrayList<String> header = new ArrayList<>();
    private DetailCardAdapter listAdapter;
    private ListView listView;
    private boolean mStopHandler = false;
    public static CardetailFragment newInstance() {
        CardetailFragment fragment = new CardetailFragment ();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_cardetail,container, false);
       /* dcMaf= (DetailCard) rootview.findViewById(R.id.dc_carDetail_Maf);
        dcEGR = (DetailCard) rootview.findViewById(R.id.dc_carDetail_EGR);
        dcEngineLoad = (DetailCard) rootview.findViewById(R.id.dc_carDetail_EngineLoad);
        dcTemp = (DetailCard) rootview.findViewById(R.id.dc_carDetail_Temperature);
        dcThrottle = (DetailCard) rootview.findViewById(R.id.dc_carDetail_Throttle);
        dcFuelLevel = (DetailCard) rootview.findViewById(R.id.dc_carDetail_FuelLevel); */
        defValue = "1 01-07-E1-06\r\n"+
                "4 -1\r\n" +
                "5 -1\r\n" +
                "6 -1\r\n" +
                "7 -1\r\n" +
                "11 -1\r\n" +
                "12 -1\r\n" +
                "13 -1\r\n" +
                "14 -1\r\n" +
                "15 -1\r\n" +
                "16 -1\r\n" +
                "17 -1\r\n" +
                "21 -1 -1\r\n" +
                "31 -1\r\n" +
                "33 -1\r\n" +
                "44 -1\r\n" +
                "45 -1\r\n" +
                "46 -1\r\n" +
                "47 -1\r\n" +
                "48 -1\r\n" +
                "49 -1\r\n" +
                "51 -1\r\n" +
                "52 -1 -1\r\n" +
                "60 -1\r\n" +
                "66 -1\r\n" +
                "67 -1\r\n" +
                "68 -1\r\n" +
                "69 -1\r\n" +
                "71 -1\r\n" +
                "73 -1\r\n" +
                "74 -1\r\n" +
                "76 -1\r\n" +
                "81 -1\r\n" +
                "82 -1\r\n" +
                "DTC: P-1\r\n" +
                "Lat: -1\r\n" +
                "Long: -1\r\n" +
                "Distance: -1\r\n";
        shared = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = shared.getString("dataBT", defValue);
        data = gson.fromJson(json, CarDetail.class);
        CarDetailManager.getInstance().setDao(data);
        listView = (ListView)rootview.findViewById(R.id.listView);
        listAdapter = new DetailCardAdapter(Labelheader());
        listView.setAdapter(listAdapter);
        instantiate();
        return  rootview;
    }
    private ArrayList<String> Labelheader(){
        header.add("Engine Load");
        header.add("Engine Coolant Temp.");
        header.add("Short term fuel trim—Bank 1");
        header.add("Short term fuel trim—Bank 2");
        header.add("Intake manifold absolute pressure");
        header.add("Engine RPM");
        header.add("Speed");
        header.add("Timing advance");
        header.add("Intake air temperature");
        header.add("MAF air flow rate");
        header.add("Throttle position");
        header.add("Oxygen Sensor 2");
        header.add("Run time since engine start");
        header.add("Distance traveled with malfunction ");
        header.add("Commanded EGR");
        header.add("EGR Error");
        header.add("Commanded evaporative purge");
        header.add("Fuel Tank Level");
        header.add("Warm-ups since codes cleared");
        header.add("Distance traveled since codes cleared");
        header.add("Absolute Barometric Pressure");
        header.add("Oxygen Sensor 1");
        header.add("Catalyst Temperature: Bank 1, Sensor 1");
        header.add("Voltage");
        header.add("Absolute load value");
        header.add("Fuel–Air commanded equivalence ratio");
        header.add("Relative throttle position");
        header.add("Absolute throttle position B");
        header.add("Accelerator pedal position D");
        header.add("Accelerator pedal position E");
        header.add("Commanded throttle actuator");
        header.add("Fuel Type");
        header.add("Ethanol fuel %");
        header.add("DTC");
        header.add("Distance");
        header.add("Latitude");
        header.add("Longitude");
        return header;
    }
    private void instantiate() {
        //shared = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        mHandler = new Handler();
        Runnable runnable = new Runnable() {
            int i = 0;
            @Override
            public void run() {
                String num = String.valueOf(i);
                /*dcMafData = shared.getString("dcMaf","No Data");
                dcEGRData = shared.getString("dcEGR","No Data");
                dcEngineLoadData = shared.getString("dcEngineLoad","No Data");
                dcFuelLevelData = shared.getString("dcFuelLevel","No Data");
                dcTempData = shared.getString("dcTemp","No Data");
                dcThrottleData = shared.getString("dcThrottle","No Data");
                setupDetailCard(dcMaf,"Mass Air Flow",dcMafData,R.drawable.ic_label);
                setupDetailCard(dcEGR,"EGR Error",dcEGRData,R.drawable.ic_label);
                setupDetailCard(dcEngineLoad,"Engine Load",dcEngineLoadData,R.drawable.ic_label);
                setupDetailCard(dcFuelLevel,"Fuel Level",dcFuelLevelData,R.drawable.ic_label);
                setupDetailCard(dcTemp,"Temperature",dcTempData,R.drawable.ic_label);
                setupDetailCard(dcThrottle,"Throttle position",dcThrottleData,R.drawable.ic_label);*/
                defValue = "1 01-07-E1-06\r\n"+
                        "4 "+num+"\r\n" +
                        "5 -1\r\n" +
                        "6 -1\r\n" +
                        "7 -1\r\n" +
                        "11 -1\r\n" +
                        "12 -1\r\n" +
                        "13 -1\r\n" +
                        "14 -1\r\n" +
                        "15 -1\r\n" +
                        "16 -1\r\n" +
                        "17 -1\r\n" +
                        "21 -1 -1\r\n" +
                        "31 -1\r\n" +
                        "33 -1\r\n" +
                        "44 -1\r\n" +
                        "45 -1\r\n" +
                        "46 -1\r\n" +
                        "47 -1\r\n" +
                        "48 -1\r\n" +
                        "49 -1\r\n" +
                        "51 -1\r\n" +
                        "52 -1 -1\r\n" +
                        "60 -1\r\n" +
                        "66 -1\r\n" +
                        "67 -1\r\n" +
                        "68 -1\r\n" +
                        "69 -1\r\n" +
                        "71 -1\r\n" +
                        "73 -1\r\n" +
                        "74 -1\r\n" +
                        "76 -1\r\n" +
                        "81 -1\r\n" +
                        "82 -1\r\n" +
                        "DTC: P-1\r\n" +
                        "Lat: -1\r\n" +
                        "Long: -1\r\n" +
                        "Distance: -1\r\n";
                Gson gson = new Gson();
                String json = shared.getString("dataBT", "");
                if(json.equals(""))
                {
                    data = new CarDetail(defValue);
                }
                else{
                    data = gson.fromJson(json, CarDetail.class);
                }
                CarDetailManager.getInstance().setDao(data);
                listAdapter.notifyDataSetChanged();
                i++;
                Log.d("pepo", CarDetailManager.getInstance().getDao(0 )+"real: "+data.getPid_04());
                if (!mStopHandler) {
                    mHandler.postDelayed(this, 1000);
                }
            }
        };
        mHandler.post(runnable);
    }

    private void setupDetailCard(DetailCard card,String header,String detail,int imgSrc)
    {
        card.setDetail(detail);
        card.setHeader(header);
        card.setImage(imgSrc);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}

