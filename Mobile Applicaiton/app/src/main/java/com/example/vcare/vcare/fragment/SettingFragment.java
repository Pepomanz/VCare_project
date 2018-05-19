package com.example.vcare.vcare.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.preference.PreferenceManager;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vcare.vcare.R;
import com.example.vcare.vcare.Service.MyBluetoothService;

import java.util.Set;

/**
 * Created by VCare_kmutt on 1/25/2018.
 */

public class SettingFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    private SwitchPreferenceCompat enableBT,startService;
    private SharedPreferences shared;
    private int state;
    private String macAddress;
    private String deviceName [],deviceMacAddress [],entriesValue [];
    private ListPreference deviceList;
    private BluetoothAdapter mBluetoothAdapter;
    private final static int REQUEST_ENABLE_BT = 1;
    private Set<BluetoothDevice> pairedDevices;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.setting_preferences);
        instantiate();
    }

    private void instantiate() {
        enableBT = (SwitchPreferenceCompat) findPreference("swicthBT");
        deviceList = (ListPreference) findPreference("deviceList");
        startService = (SwitchPreferenceCompat) findPreference("Service");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {
            enableBT.setChecked(true);
        }
        enableBT.setOnPreferenceClickListener(this);
        startService.setOnPreferenceClickListener(this);
        shared = PreferenceManager.getDefaultSharedPreferences(getActivity());
        macAddress = shared.getString("mac","0000-0000-0000");
        getDevice();
    }

    private void getDevice() {
        pairedDevices = mBluetoothAdapter.getBondedDevices();
        Integer index=0;
        entriesValue = new String[pairedDevices.size()];
        deviceName = new String[pairedDevices.size()];
        deviceMacAddress = new String[pairedDevices.size()];
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                deviceName[index] = device.getName();
                entriesValue[index] = index.toString();
                deviceMacAddress[index] = device.getAddress(); // MAC address
                index++;
            }
        }
        deviceList.setEntries(deviceName);
        deviceList.setEntryValues(entriesValue);
        deviceList.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference == enableBT)
        {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                enableBT.setChecked(true);
                getDevice();
            }
            else
            {
                mBluetoothAdapter.disable();
                enableBT.setChecked(false);
            }
            return true;
        }
        else if(preference == startService)
        {
            state = shared.getInt("state",0);
            if(state == 0)
            {
                Log.d("check","start bluetooth");
                Log.d("check",macAddress);
                if (macAddress.equals("0000-0000-0000"))
                {
                    Toast.makeText(getActivity().getApplicationContext(),"Please select device first",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent intent = new Intent(getActivity().getApplicationContext(), MyBluetoothService.class);
                    intent.putExtra("mac",macAddress);
                    getActivity().startService(intent);
                }
                SharedPreferences.Editor editor = shared.edit();
                editor.putInt("state",1);
                editor.commit();
                startService.setChecked(true);
            }
            else
            {
                Log.d("check","stop bluetooth");
                Intent intent = new Intent(getActivity().getApplicationContext(), MyBluetoothService.class);
                getActivity().stopService(intent);
                SharedPreferences.Editor editor = shared.edit();
                editor.putInt("state",0);
                editor.commit();
                startService.setChecked(false);
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        int index;
        if(preference == deviceList){
            deviceList.setValue(newValue.toString());
            index =  Integer.parseInt(newValue.toString());
            SharedPreferences.Editor editor = shared.edit();
            editor.putString("mac",deviceMacAddress[index]);
            editor.commit();
            return true;
        }
        return false;
    }
}
