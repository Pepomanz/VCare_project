package com.example.vcare.vcare.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.vcare.vcare.CustomView.DetailCard;
import com.example.vcare.vcare.dao.CarDetail;
import com.example.vcare.vcare.dao.CarDetailManager;

import java.util.ArrayList;

public class DetailCardAdapter extends BaseAdapter{
    private ArrayList<String> header;
    private CarDetailManager dao;

    public DetailCardAdapter(ArrayList<String> header) {
        this.header = header;
    }

    @Override
    public int getCount() {
        return header.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DetailCard dc;
        if(convertView != null)
            dc = (DetailCard) convertView;
        else
            dc = new DetailCard(parent.getContext());
        dc.setHeader(header.get(position));
        dc.setDetail(CarDetailManager.getInstance().getDao(position));
        return dc;
    }
}
