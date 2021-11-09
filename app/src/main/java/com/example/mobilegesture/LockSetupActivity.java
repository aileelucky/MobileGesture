package com.example.mobilegesture;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.shinemo.patterlockview.PatternLockView;
import com.shinemo.patterlockview.listener.PatternLockViewListener;
import com.shinemo.shinemogesture.ShinemoGesture;
import com.shinemo.shinemogesture.listener.SettingGestureImp;
import com.shinemo.shinemogesture.listener.ShinemoCommonImp;
import com.shinemo.shinemogesture.model.LoginResp;
import com.shinemo.shinemogesture.model.SettingGestureResponse;
import com.shinemo.shinemogesture.util.ToastUtil;

import java.util.List;


/**
 * 设置手势密码
 */
public class LockSetupActivity extends GestureBaseActivity {

    TextView title;
    TextView titleTip;
    PatternLockView patternLockView;

    public static final int AUTH_TYPE = 1;//验证
    public static final int SET_TYPE = 2;//设置手势

    private boolean isFirstSetting = true; //是否第一次设置手势
    private int mType;
    private String token;
    private String selectDots;
    private String ticket;
    private String account;

    public static void startAuthActivity(Context context, String account) {
        Intent intent = new Intent(context, LockSetupActivity.class);
        intent.putExtra("type", AUTH_TYPE);
        intent.putExtra("account", account);
        context.startActivity(intent);
    }

    public static void startSetActivity(Context context, String token) {
        Intent intent = new Intent(context, LockSetupActivity.class);
        intent.putExtra("type", SET_TYPE);
        intent.putExtra("token", token);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getIntent().getIntExtra("type",0);
        token = getIntent().getStringExtra("token");
        account = getIntent().getStringExtra("account");

        setContentView(R.layout.activity_lock_setup);
        titleTip = findViewById(R.id.handlock_title);
        title = findViewById(R.id.title);
        findViewById(R.id.back).setOnClickListener(v->{
            finish();
        });
        if(mType == AUTH_TYPE){
            title.setText(R.string.auth_security_gesture_set);
        }else if(mType == SET_TYPE){
            title.setText(R.string.setting_security_gesture_set);
        }
        patternLockView = findViewById(R.id.lock_pattern);
        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                inputComplete(pattern);
            }

            @Override
            public void onCleared() {

            }
        });
    }

    //手势输入结束
    private void inputComplete(List<PatternLockView.Dot> pattern){
        String dots = getSelect(pattern);
        if(TextUtils.isEmpty(dots) || dots.length() < 4){
            patternLockView.clearPattern();
            ToastUtil.show(this,"不能少于四个点");
            return;
        }
        if(!isFirstSetting && !selectDots.equals(dots)){
            patternLockView.clearPattern();
            ToastUtil.show(LockSetupActivity.this,"两次手势不一致");
            return;
        }
        selectDots = dots;
        if(mType == SET_TYPE){
            setGesture();
        }else {
            loginGesture();
        }
    }

    private String getSelect(List<PatternLockView.Dot> pattern){
        StringBuilder dots = new StringBuilder();
        for(PatternLockView.Dot dot : pattern){
            dots.append(dot.getRow() *3 + dot.getColumn() + 1);
        }
        return dots.toString();
    }

    //设置手势
    private void setGesture(){
        ShinemoGesture.setGesture(this,selectDots,token,ticket,new SettingGestureImp(){
            @Override
            public void success(SettingGestureResponse response) {
                ToastUtil.show(LockSetupActivity.this,"设置手势密码成功");
                finish();
            }

            @Override
            public void error(SettingGestureResponse response, String code, String msg) {
                super.error(response,code,msg);
                //第一次请求会走error,此时当code=1的时候，表示第一次请求成功，并且会返回ticket
            }
        });
    }

    //验证手势
    private void loginGesture(){
        ShinemoGesture.loginGesture(this,account,selectDots,new ShinemoCommonImp<LoginResp>(){
            @Override
            public void end() {
                super.end();
                patternLockView.clearPattern();
            }
        });

    }
}
