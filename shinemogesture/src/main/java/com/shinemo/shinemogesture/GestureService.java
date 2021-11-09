package com.shinemo.shinemogesture;

import com.ailee.retrofit.bean.SimpleResp;
import com.shinemo.shinemogesture.model.LoginRequest;
import com.shinemo.shinemogesture.model.LoginResp;
import com.shinemo.shinemogesture.model.QueryGestureRequest;
import com.shinemo.shinemogesture.model.SettingGestureRequest;
import com.shinemo.shinemogesture.model.SettingGestureResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GestureService {
    /**
     * 账户密码登录
     *  account 账号
     *  pwdType 校验的密码类型 1 注册密码 2 会控密码 默认填1
     *  pwd 密码 AES128(md5(pwd),syskey)
     *  authType 认证类型 1 登录 2 鉴权选择，1 则会返回生成的token；2 则只做校验
     */
    @POST(UrlConstants.PWD_LOGIN_URL)
    Call<SimpleResp<LoginResp>> pwdLogin(@Body LoginRequest body);


    /**
     *
     * account account
     * uid uid
     */
    @POST(UrlConstants.QUERY_GESTURE)
    Call<SimpleResp<Boolean>> queryGesture(@Body QueryGestureRequest request);

    @POST(UrlConstants.SET_GESTURE)
    Call<SimpleResp<SettingGestureResponse>> setGesture(@Body SettingGestureRequest request);

    @POST(UrlConstants.LOGIN_GESTURE)
    Call<SimpleResp<LoginResp>> loginGesture(@Body QueryGestureRequest request);
}
