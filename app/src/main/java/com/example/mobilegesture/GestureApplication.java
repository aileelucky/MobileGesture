package com.example.mobilegesture;

import android.app.Application;

import com.shinemo.shinemogesture.ShinemoGestureConfig;
import com.shinemo.shinemogesture.UrlConstants;

public class GestureApplication extends Application {

    public static final String sysId = "12";
    public static final String sysKey = "11111111111111111111111111111111";


    @Override
    public void onCreate() {
        super.onCreate();
        ShinemoGestureConfig.init(this,UrlConstants.DEV,sysKey);
    }

}
