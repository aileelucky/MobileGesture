package com.shinemo.shinemogesture;

import android.content.Context;

import com.ailee.retrofit.AdvancedRetrofitHelper;
import com.ailee.retrofit.RetrofitManager;
import com.ailee.retrofit.SimpleRetrofitCallback;
import com.ailee.retrofit.bean.BaseResp;
import com.ailee.retrofit.bean.SimpleResp;
import com.shinemo.shinemogesture.listener.QueryGestureInterface;
import com.shinemo.shinemogesture.listener.SettingGestureInterface;
import com.shinemo.shinemogesture.listener.ShinemoCommonInterface;
import com.shinemo.shinemogesture.model.LoginRequest;
import com.shinemo.shinemogesture.model.LoginResp;
import com.shinemo.shinemogesture.model.QueryGestureRequest;
import com.shinemo.shinemogesture.model.SettingGestureRequest;
import com.shinemo.shinemogesture.model.SettingGestureResponse;
import com.shinemo.shinemogesture.util.AESEncrypt;
import com.shinemo.shinemogesture.util.ToastUtil;

import retrofit2.Call;


public class ShinemoGesture {

    //登录
    public static void loginPWD(Context context, LoginRequest request, ShinemoCommonInterface<LoginResp> commonInterface){
        GestureService gestureService = RetrofitManager.createService(GestureService.class);
        AdvancedRetrofitHelper.enqueue(context,gestureService.pwdLogin(request),new SimpleRetrofitCallback<SimpleResp<LoginResp>>(){
            @Override
            public void onSuccess(Call<SimpleResp<LoginResp>> call, SimpleResp<LoginResp> resp) {
                super.onSuccess(call, resp);
                if(commonInterface != null){
                    commonInterface.success(resp.getData());
                }
            }

            @Override
            public void onError(Call<SimpleResp<LoginResp>> call, String errorCode, String msg) {
                if(commonInterface != null){
                    commonInterface.error(errorCode,msg);
                }
                ToastUtil.show(context,"errotCode:"+errorCode+",msg:"+msg);
            }

        });
    }

    //查询手势
    public static void queryGesture(Context context, String account, QueryGestureInterface listener){
        GestureService gestureService = RetrofitManager.createService(GestureService.class);
        QueryGestureRequest queryGestureRequest = new QueryGestureRequest();
        queryGestureRequest.setAccount(account);
        AdvancedRetrofitHelper.enqueue(context,gestureService.queryGesture(queryGestureRequest),new SimpleRetrofitCallback<SimpleResp<Boolean>>(){
            @Override
            public void onSuccess(Call<SimpleResp<Boolean>> call, SimpleResp<Boolean> resp) {
                super.onSuccess(call, resp);
                if(listener != null){
                    listener.success(resp.getData());
                }
            }

            @Override
            public void onError(Call<SimpleResp<Boolean>> call, String errorCode, String msg) {
                super.onError(call, errorCode, msg);
                if(listener != null){
                    listener.error(errorCode,msg);
                }
                ToastUtil.show(context,"errorCode:"+errorCode+",msg:"+msg);
            }

            @Override
            public void onEnd(Call<SimpleResp<Boolean>> call) {
            }
        });
    }

    //设置手势
    public static void setGesture(Context context,String selectDots, String token, String ticket, SettingGestureInterface gestureInterface){
        GestureService gestureService = RetrofitManager.createService(GestureService.class);
        SettingGestureRequest request = new SettingGestureRequest();
        request.setToken(token);
        request.setTicket(ticket);
        try {
            request.setBiologyInfo(AESEncrypt.encodeAES(selectDots,ShinemoGestureConfig.sysKey));
        }catch (Exception e){

        }
        AdvancedRetrofitHelper.enqueue(context,gestureService.setGesture(request),new SimpleRetrofitCallback<SimpleResp<SettingGestureResponse>>(){
            @Override
            public void onSuccess(Call<SimpleResp<SettingGestureResponse>> call, SimpleResp<SettingGestureResponse> resp) {
                super.onSuccess(call, resp);
                if(gestureInterface != null){
                    gestureInterface.success(resp.getData());
                }
            }

            @Override
            public void onError(Call<SimpleResp<SettingGestureResponse>> call, BaseResp baseResp) {
                if(gestureInterface != null){
                    gestureInterface.error(((SimpleResp<SettingGestureResponse>)baseResp).getData(),baseResp.getCode(),baseResp.getMsg());
                }
            }
        });
    }

    //手势认证
    public static void loginGesture(Context context, String account, String selectDots, ShinemoCommonInterface shinemoCommonInterface){
        GestureService gestureService = RetrofitManager.createService(GestureService.class);
        QueryGestureRequest queryGestureRequest = new QueryGestureRequest();
        queryGestureRequest.setAccount(account);
        try {
            queryGestureRequest.setBiologyInfo(AESEncrypt.encodeAES(selectDots,ShinemoGestureConfig.sysKey));
        }catch (Exception e){ }

        AdvancedRetrofitHelper.enqueue(context,gestureService.loginGesture(queryGestureRequest),new SimpleRetrofitCallback<SimpleResp<LoginResp>>(){
            @Override
            public void onSuccess(Call<SimpleResp<LoginResp>> call, SimpleResp<LoginResp> resp) {
                super.onSuccess(call, resp);
                if(shinemoCommonInterface != null){
                    shinemoCommonInterface.success(resp);
                }
                ToastUtil.show(context,"登录成功");
            }

            @Override
            public void onError(Call<SimpleResp<LoginResp>> call, BaseResp baseResp) {
                super.onError(call, baseResp);
                ToastUtil.show(context,baseResp.getMsg());
            }

            @Override
            public void onEnd(Call<SimpleResp<LoginResp>> call) {
                if(shinemoCommonInterface != null){
                    shinemoCommonInterface.end();
                }
            }
        });
    }
}
