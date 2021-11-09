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
    }

}