package com.shinemo.shinemogesture.listener;

import com.shinemo.shinemogesture.model.SettingGestureResponse;

public interface SettingGestureInterface {
    void success(SettingGestureResponse response);
    void error(SettingGestureResponse response, String code,String msg);
}
