package com.example.vcare.vcare.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.vcare.vcare.R;

import java.util.ArrayList;

/**
 * Created by PepoManZ on 3/29/2018.
 */

public class InfoFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_info, container, false);
        ArrayList< String > list = new ArrayList<>( );
        list.add ( "Load Engine 80 %" );
        list.add ( "Coolant Temperature 45 celcius" );
        list.add ( "Engine status is OK" );
        list.add ( "Combusttion of engine is quite good" );
        ArrayAdapter < String > dataAdapter = new ArrayAdapter< >( getContext(), android.R.layout.simple_list_item_1, list );
        ListView listview = (ListView) rootview.findViewById (R.id.lsInfo);
        listview.setAdapter (dataAdapter);
        return  rootview;
    }
}
