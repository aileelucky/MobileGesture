package com.shinemo.shinemogesture;

import android.content.Context;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by gujiajia on 2016/6/2.
 * E-mail 965939858@qq.com
 * Tel: 15050261230
 */
public class RetrofitInterceptor implements Interceptor {

    private Context context;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public RetrofitInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request chainRequest = chain.request();
        Request.Builder builder = chainRequest.newBuilder();
        String time = Long.toString(System.currentTimeMillis() / 1000);
        Map<String,String> map = SignBuilder.getBaseMap(context,time);
        for (Map.Entry entry : map.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            builder.addHeader(key, value);
        }
        Request request = SignBuilder.newBuilder(context, builder, time);
        //非sign加密
//        builder.addHeader("sign", SecureUtils.getSHA(Constants.VERSION + Constants.KEY));
//        Request request = builder.build();
        return chain.proceed(request);
    }
}
