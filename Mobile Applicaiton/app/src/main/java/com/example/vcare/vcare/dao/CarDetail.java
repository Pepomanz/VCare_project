package com.example.vcare.vcare.dao;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by VCare_kmutt on 1/25/2018.
 */

public class CarDetail {
    private String pid_01;
    private float pid_04;
    private float pid_05;
    private float pid_06;
    private float pid_07;
    private int   pid_11;
    private float pid_12;
    private float pid_13;
    private float pid_14;
    private int   pid_15;
    private float pid_16;
    private float pid_17;
    private float pid_21_A;
    private float pid_21_B;
    private int   pid_31;
    private int   pid_33;
    private float pid_44;
    private int   pid_45;
    private float pid_46;
    private float pid_47;
    public int   pid_48;
    private int   pid_49;
    private int   pid_51;
    private float pid_52_A;
    private float pid_52_B;
    private float pid_60;
    private float pid_66;
    private float pid_67;
    private float pid_68;
    private float pid_69;
    private float pid_71;
    private float pid_73;
    private float pid_74;
    private float pid_76;
    private String pid_81;
    private float pid_82;
    public String DTC ;
    //private int userId;
    private float latitude;
    private float longitude;
    private float distance;

    public CarDetail (String data){
        String[] splited = data.split("\r\n");
        for(int i = 0;i<splited.length;i++)
        {
            String[] word = splited[i].split("\\s+");
            if (word[0].equals("1"))
            {
                pid_01 = word[1];
            }
            else if (word[0].equals("4"))
            {
                pid_04 = Float.parseFloat(word[1]);
            }
            else if (word[0].equals("5"))
            {
                pid_05 = Float.parseFloat(word[1]);
            }
            else if (word[0].equals("6"))
            {
                pid_06 = Float.parseFloat(word[1]);
            }
            else if (word[0].equals("7"))
            {
                pid_07 = Float.parseFloat(word[1]);
            }
            else if (word[0].equals("11"))
            {
                pid_11 = Integer.parseInt(word[1]);
            }
            else if (word[0].equals("12"))
            {
                pid_12 = Float.parseFloat(word[1]);
            }
            else if (word[0].equals("13"))
            {
                pid_13 = Float.parseFloat(word[1]);
            }
            else if (word[0].equals("14"))
            {
                pid_14 = Float.parseFloat(word[1]);
            }
            else if (word[0].equals("15"))
            {
                pid_15 = Integer.parseInt(word[1]);
            }
            else if (word[0].equals("16"))
            {
                pid_16 = Float.parseFloat(word[1]);
            }
            else if (word[0].equals("17"))
            {
                pid_17 = Float.parseFloat(word[1]);
            }
            else if (word[0].equals("21"))
            {
                pid_21_A = Float.parseFloat(word[1]);
                pid_21_B = Float.parseFloat(word[2]);
            }
            else if (word[0].equals("31"))
            {
                pid_31 = Integer.parseInt(word[1]);
            }
            else if (word[0].equals("33"))
            {
                pid_33 = Integer.parseInt(word[1]);
            }
            else if (word[0].equals("44"))
            {
                pid_44 = Float.valueOf(word[1]);
            }
            else if (word[0].equals("45"))
            {
                pid_45 = Integer.parseInt(word[1]);
            }
            else if (word[0].equals("46"))
            {
                pid_46 = Float.parseFloat(word[1]);
            }
            else if (word[0].equals("47"))
            {
                pid_47 = Float.parseFloat(word[1]);
            }
            else if (word[0].equals("48"))
            {
                this.pid_48 = Integer.parseInt(word[1]);
            }
            else if (word[0].equals("49"))
            {
                pid_49 = Integer.parseInt(word[1]);
            }
            else if (word[0].equals("51"))
            {
                pid_51 = Integer.parseInt(word[1]);
            }
            else if (word[0].equals("52"))
            {
                pid_52_A = Float.parseFloat(word[1]);
                pid_52_B = Float.parseFloat(word[2]);
            }
            else if (word[0].equals("60"))
            {
                pid_60 = Float.parseFloat(word[1]);
            }
            else if (word[0].equals("66"))
            {
                pid_66 = Float.parseFloat(word[1]);
            }
            else if (word[0].equals("67"))
            {
                pid_67 = Float.parseFloat(word[1]);
            }
            else if (word[0].equals("68"))
            {
                pid_68 = Float.parseFloat(word[1]);
            }
            else if (word[0].equals("69"))
            {
                pid_69 = Float.parseFloat(word[1]);
            }
            else if (word[0].equals("71"))
            {
                pid_71 = Float.parseFloat(word[1]);
            }
            else if (word[0].equals("73"))
            {
               pid_73 = Float.parseFloat(word[1]);
            }
            else if (word[0].equals("74"))
            {
               pid_74 = Float.parseFloat(word[1]);
            }
            else if (word[0].equals("76"))
            {
               pid_76 = Float.parseFloat(word[1]);
            }
            else if (word[0].equals("81"))
            {
                pid_81 = word[1];
            }
            else if (word[0].equals("82"))
            {
                pid_82 = Float.parseFloat(word[1]);
            }
            else if (word[0].equals("DTC:"))
            {
                DTC = word[1];
            }
            else if (word[0].equals("Lat:"))
            {
                latitude = Float.parseFloat(word[1]);
            }
            else if (word[0].equals("Long:"))
            {
                longitude = Float.parseFloat(word[1]);
            }
            else if (word[0].equals("Dist:"))
            {
                distance = Float.valueOf(word[1]);
            }
        }
    }

    public String getPid_01() {
        return String.valueOf(pid_01);
    }

    public String getPid_04() {
        return String.valueOf(pid_04);
    }

    public String getPid_05() {
        return String.valueOf(pid_05);
    }

    public String getPid_06() {
        return String.valueOf(pid_06);
    }

    public String getPid_07() {
        return String.valueOf(pid_07);
    }

    public String getPid_11() {
        return String.valueOf(pid_11);
    }

    public String getPid_12() {
        return String.valueOf(pid_12);
    }

    public String getPid_13() {
        return String.valueOf(pid_13);
    }

    public String getPid_14() {
        return String.valueOf(pid_14);
    }

    public String getPid_15() {
        return String.valueOf(pid_15);
    }

    public String getPid_16() {
        return String.valueOf(pid_16);
    }

    public String getPid_17() {
        return String.valueOf(pid_17);
    }

    public String getPid_21() {
        return String.valueOf(pid_21_A)+String.valueOf(pid_21_B);
    }

    public String getPid_31() {
        return String.valueOf(pid_31);
    }

    public String getPid_33() {
        return String.valueOf(pid_33);
    }

    public String getPid_44() {
        return String.valueOf(pid_44);
    }

    public String getPid_45() {
        return String.valueOf(pid_45);
    }

    public String getPid_46() {
        return String.valueOf(pid_46);
    }

    public String getPid_47() {
        return String.valueOf(pid_47);
    }

    public String getPid_48() {
        return String.valueOf(pid_48);
    }

    public String getPid_49() {
        return String.valueOf(pid_49);
    }

    public String getPid_51() {
        return String.valueOf(pid_51);
    }

    public String getPid_52() {
        return String.valueOf(pid_52_A)+ String.valueOf(pid_52_B);
    }

    public String getPid_60() {
        return String.valueOf(pid_60);
    }

    public String getPid_66() {
        return String.valueOf(pid_66);
    }

    public String getPid_67() {
        return String.valueOf(pid_67);
    }

    public String getPid_68() {
        return String.valueOf(pid_68);
    }

    public String getPid_69() {
        return String.valueOf(pid_69);
    }

    public String getPid_71() {
        return String.valueOf(pid_71);
    }

    public String getPid_73() {
        return String.valueOf(pid_73);
    }

    public String getPid_74() {
        return String.valueOf(pid_74);
    }

    public String getPid_76() {
        return String.valueOf(pid_76);
    }

    public String getPid_81() {
        return pid_81;
    }

    public String getPid_82() {
        return String.valueOf(pid_82);
    }

    public String getDTC() {
        return DTC;
    }

    public String getLatitude() {
        return String.valueOf(latitude);
    }

    public String getLongitude() {
        return String.valueOf(longitude);
    }

    public String getDistance() {
        return String.valueOf(distance);
    }

}
