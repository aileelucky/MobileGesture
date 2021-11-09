package com.shinemo.shinemogesture;

import android.app.Application;


import com.ailee.retrofit.ApiStatusInterceptor;
import com.ailee.retrofit.LMCache;
import com.ailee.retrofit.OkHttpConfiguration;
import com.ailee.retrofit.RetrofitManager;
import com.ailee.retrofit.RetrofitProvider;
import com.ailee.retrofit.SimpleOkHttpConfiguration;

import java.util.Collections;
import java.util.List;

import okhttp3.Interceptor;

public class ShinemoGestureConfig {
    public static Application application;
    public static String EN_URL;  //环境地址
    public static String sysKey;

    public static void init(Application _application,String en_url,String _sysKey){
        application = _application;
        EN_URL = en_url;
        sysKey = _sysKey;
        RetrofitManager.init(new RetrofitProvider() {
            @Override
            public String provideBaseUrl() {
                return EN_URL;
            }

            @Override
            public OkHttpConfiguration provideOkHttpConfig() {
                return new SimpleOkHttpConfiguration(){
                    @Override
                    public List<Interceptor> interceptors() {
                        return Collections.<Interceptor>singletonList(new RetrofitInterceptor(application));
                    }

                    @Override
                    public int readTimeoutSeconds() {
                        return super.readTimeoutSeconds();
                    }

                    @Override
                    public int writeTimeoutSeconds() {
                        return super.writeTimeoutSeconds();
                    }

                    @Override
                    public int connectTimeoutSeconds() {
                        return super.connectTimeoutSeconds();
                    }
                };
            }

            @Override
            public ApiStatusInterceptor provideApiStatusInterceptor() {
                return null;
            }

            @Override
            public LMCache provideCache() {
                return null;
            }
        });
    }
}
