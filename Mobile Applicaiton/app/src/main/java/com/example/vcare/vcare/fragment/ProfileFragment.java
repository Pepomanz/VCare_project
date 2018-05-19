package com.example.vcare.vcare.fragment;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.vcare.vcare.R;
import com.example.vcare.vcare.dao.Alert;
import com.example.vcare.vcare.dao.UserDetail;
import com.example.vcare.vcare.database.AppDatabase;
import com.example.vcare.vcare.database.InsertData;
import com.example.vcare.vcare.http.NetworkConnectManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by VCare_kmutt on 1/25/2018.
 */

public class ProfileFragment extends Fragment {
    Button btnAPI,btnTest;
    ImageView profleImg;
    TextView tvprofileName,tvProfiletTel,tvProfileEmail,tvProfileCar,tvProfileModel,tvProfileYear;
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_profile,container, false);
        initInstance(rootview);
        return  rootview;
    }

    private void initInstance(View rootview) {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String name = shared.getString("name","");
        String tel = shared.getString("tel","");
        String email = shared.getString("email","");
        String car = shared.getString("brand","");
        String model = shared.getString("model","");
        String year = shared.getString("year","");
        String picture = shared.getString("pic","");

    }

}