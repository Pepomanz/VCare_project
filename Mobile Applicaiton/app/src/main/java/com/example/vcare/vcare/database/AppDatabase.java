package com.example.vcare.vcare.database;


import com.example.vcare.vcare.dao.CarDetailDao;
import com.example.vcare.vcare.dao.UserDetailDao;

/**
 * Created by VCare_kmutt on 1/25/2018.
 */

public abstract class AppDatabase  {
    /*private static AppDatabase INSTANCE;
    public abstract UserDetailDao userModel();
    public abstract CarDetailDao carModel();
    public static AppDatabase getINSTANCE(Context context) {
        String DataBaseName = "Vcare.db";
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    DataBaseName)
                    .build();
        }
        return INSTANCE;
    }
    public static void destroyInstance() {
        INSTANCE = null;
    }*/
}
