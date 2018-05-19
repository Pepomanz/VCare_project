package com.example.vcare.vcare.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vcare.vcare.CustomView.DetailCard;
import com.example.vcare.vcare.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class SummaryFragment extends Fragment {
    LineChart lineChart;
    DetailCard dc_Distance,dc_Temperature,dc_Speed,dc_Fuelrate,dc_TroubleCode;
    public static SummaryFragment newInstance() {
        SummaryFragment fragment = new SummaryFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_summary,container, false);
        lineChart = (LineChart) rootview.findViewById(R.id.lineChart);
        dc_Distance = (DetailCard) rootview.findViewById(R.id.dc_Distance);
        dc_Temperature = (DetailCard) rootview.findViewById(R.id.dc_Temperature);
        dc_Speed = (DetailCard) rootview.findViewById(R.id.dc_Speed);
        dc_Fuelrate = (DetailCard) rootview.findViewById(R.id.dc_Fuelrate);
        dc_TroubleCode = (DetailCard) rootview.findViewById(R.id.dc_TroubleCode);
        setupDetailCard(dc_Distance,"Distance","300 Km",R.drawable.ic_label);
        setupDetailCard(dc_Temperature,"Coolant Temperature","300 Km",R.drawable.ic_label);
        setupDetailCard(dc_Speed,"Avg. Speed","30 km/hr",R.drawable.ic_label);
        setupDetailCard(dc_Fuelrate,"Fuel Consumption Rate","12 Km/L",R.drawable.ic_label);
        setupDetailCard(dc_TroubleCode,"DTC Code","P0501",R.drawable.ic_label);
        instantiate();
        return  rootview;
    }

    private void instantiate() {
        List<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(0f,0f));
        entries.add(new Entry(4f,25f));
        entries.add(new Entry(4f,0f));
        ArrayList<String> label = new ArrayList<String>();
        label.add("Jan");
        label.add("Feb");
        label.add("March");
        LineDataSet dataSet = new LineDataSet(entries, "Label");
        LineData lineData = new LineData(dataSet);
        setupChart(lineChart,lineData,label);
    }
    private void setupChart(LineChart chart, LineData data,ArrayList<String> label) {
        chart.setData(data);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setYOffset(-20);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisRight().setDrawAxisLine(false);
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter());
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
    }
    private void setupDetailCard(DetailCard card,String header,String detail,int imgSrc)
    {
        card.setDetail(detail);
        card.setHeader(header);
        card.setImage(imgSrc);
    }

}
