package com.shinemo.shinemogesture;

public interface LoginPWDInterface {
    void success(boolean hasSetGesture);
    void error(String code,String msg);
}
