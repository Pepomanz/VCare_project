package com.example.vcare.vcare.dao;

import android.content.Context;

import java.util.ArrayList;

public class CarDetailManager {
    private static CarDetailManager instance;
    private CarDetail dao;
    private ArrayList<String> carDao = new ArrayList<>();

    public static CarDetailManager getInstance(){
        if(instance == null)
            instance = new CarDetailManager();
        return instance;
    }

    public CarDetail getDao() {
        return dao;
    }

    public void setDao(CarDetail dao) {
        this.dao = dao;
        this.carDao.clear();
        this.carDao.add(dao.getPid_04());
        this.carDao.add(dao.getPid_05());
        this.carDao.add(dao.getPid_06());
        this.carDao.add(dao.getPid_07());
        this.carDao.add(dao.getPid_11());
        this.carDao.add(dao.getPid_12());
        this.carDao.add(dao.getPid_13());
        this.carDao.add(dao.getPid_14());
        this.carDao.add(dao.getPid_15());
        this.carDao.add(dao.getPid_16());
        this.carDao.add(dao.getPid_17());
        this.carDao.add(dao.getPid_21());
        this.carDao.add(dao.getPid_31());
        this.carDao.add(dao.getPid_33());
        this.carDao.add(dao.getPid_44());
        this.carDao.add(dao.getPid_45());
        this.carDao.add(dao.getPid_46());
        this.carDao.add(dao.getPid_47());
        this.carDao.add(dao.getPid_48());
        this.carDao.add(dao.getPid_49());
        this.carDao.add(dao.getPid_51());
        this.carDao.add(dao.getPid_52());
        this.carDao.add(dao.getPid_60());
        this.carDao.add(dao.getPid_66());
        this.carDao.add(dao.getPid_67());
        this.carDao.add(dao.getPid_68());
        this.carDao.add(dao.getPid_69());
        this.carDao.add(dao.getPid_71());
        this.carDao.add(dao.getPid_73());
        this.carDao.add(dao.getPid_74());
        this.carDao.add(dao.getPid_76());
        this.carDao.add(dao.getPid_81());
        this.carDao.add(dao.getPid_82());
        this.carDao.add(dao.getDTC());
        this.carDao.add(dao.getDistance());
        this.carDao.add(dao.getLatitude());
        this.carDao.add(dao.getLongitude());
    }

    public String getDao(int position){
        if(this.carDao.isEmpty())
        {
            return "No Data";
        }
        return this.carDao.get(position);
    }
}
