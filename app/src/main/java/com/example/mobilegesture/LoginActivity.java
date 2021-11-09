package com.example.mobilegesture;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.shinemo.shinemogesture.ShinemoGesture;
import com.shinemo.shinemogesture.listener.QueryGestureImp;
import com.shinemo.shinemogesture.listener.ShinemoCommonImp;
import com.shinemo.shinemogesture.model.LoginRequest;
import com.shinemo.shinemogesture.model.LoginResp;
import com.shinemo.shinemogesture.util.AESEncrypt;

public class LoginActivity extends GestureBaseActivity {

    private EditText etPhone,etPasswd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.tv_login).setOnClickListener(view ->{
           login();
        });
        etPhone = findViewById(R.id.tvPhone);
        etPasswd = findViewById(R.id.tvPasswd);
        try {
            String aes = AESEncrypt.encodeAES("1478",GestureApplication.sysKey);
            Log.i("ailee",aes);
            Log.i("ailee","base64: "+ AESEncrypt.deCodeAES(aes,GestureApplication.sysKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //登录
    private void login(){
        try {
            String aes = AESEncrypt.encodeAES("1478",GestureApplication.sysKey);
            Log.i("ailee",aes);
            Log.i("ailee","base64: "+ AESEncrypt.deCodeAES(aes,GestureApplication.sysKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String name = etPhone.getText().toString();
        String pwd = etPasswd.getText().toString();

        showProgressDialog(this,"正在登录");
        LoginRequest request = new LoginRequest();
        request.setAccount(name);
        request.setPwd(pwd);
        request.setAuthType(1);
        request.setPwdType(1);

        ShinemoGesture.loginPWD(this,request,new ShinemoCommonImp<LoginResp>(){
            @Override
            public void success(LoginResp resp) {
                ShinemoGesture.queryGesture(LoginActivity.this, request.getAccount(), new QueryGestureImp() {
                    @Override
                    public void success(boolean hasSetGesture) {
                        if(hasSetGesture){
                            LockSetupActivity.startAuthActivity(LoginActivity.this, request.getAccount());
                        }else {
                            LockSetupActivity.startSetActivity(LoginActivity.this,resp.getToken());
                        }
                    }

                    @Override
                    public void error(String code, String msg) {

                    }
                });
            }

            @Override
            public void error(String code, String msg) {
                dismissProgressDialog();
            }
        });
//        AdvancedRetrofitHelper.enqueue(this,gestureService.pwdLogin(request),new SimpleRetrofitCallback<SimpleResp<LoginResp>>(){
//            @Override
//            public void onSuccess(Call<SimpleResp<LoginResp>> call, SimpleResp<LoginResp> resp) {
//                super.onSuccess(call, resp);
////                queryGesture(request.getAccount(), resp.getData());
//                ShinemoGesture.queryGesture(LoginActivity.this, request.getAccount(), new QueryGestureImp() {
//                    @Override
//                    public void success(boolean hasSetGesture) {
//                        if(hasSetGesture){
//                            LockSetupActivity.startAuthActivity(LoginActivity.this, request.getAccount());
//                        }else {
//                            LockSetupActivity.startSetActivity(LoginActivity.this,resp.getData().getToken());
//                        }
//                    }
//
//                    @Override
//                    public void error(String code, String msg) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onError(Call<SimpleResp<LoginResp>> call, String errorCode, String msg) {
//                dismissProgressDialog();
//                ToastUtil.show(LoginActivity.this,"errotCode:"+errorCode+",msg:"+msg);
//            }
//
//        });
    }

    //查询手势
    private void queryGesture(String account,LoginResp loginResp){
        ShinemoGesture.queryGesture(this,account,new QueryGestureImp(){
            @Override
            public void success(boolean hasSetGesture) {
                super.success(hasSetGesture);
                dismissProgressDialog();
                if(hasSetGesture){
                    LockSetupActivity.startAuthActivity(LoginActivity.this, account);
                }else {
                    LockSetupActivity.startSetActivity(LoginActivity.this,loginResp.getToken());
                }
            }

            @Override
            public void error(String code, String msg) {
                super.error(code, msg);
                dismissProgressDialog();
            }
        });
//        QueryGestureRequest queryGestureRequest = new QueryGestureRequest();
//        queryGestureRequest.setAccount(account);
//        AdvancedRetrofitHelper.enqueue(this,gestureService.queryGesture(queryGestureRequest),new SimpleRetrofitCallback<SimpleResp<Boolean>>(){
//            @Override
//            public void onSuccess(Call<SimpleResp<Boolean>> call, SimpleResp<Boolean> resp) {
//                super.onSuccess(call, resp);
//                if(resp.getData()){
//                    LockSetupActivity.startAuthActivity(LoginActivity.this, account);
//                }else {
//                    LockSetupActivity.startSetActivity(LoginActivity.this,loginResp.getToken());
//                }
//            }
//
//            @Override
//            public void onError(Call<SimpleResp<Boolean>> call, String errorCode, String msg) {
//                super.onError(call, errorCode, msg);
//                ToastUtil.show(LoginActivity.this,"errotCode:"+errorCode+",msg:"+msg);
//            }
//
//            @Override
//            public void onEnd(Call<SimpleResp<Boolean>> call) {
//                dismissProgressDialog();
//            }
//        });
    }
}