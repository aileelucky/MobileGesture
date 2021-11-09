package com.shinemo.shinemogesture.listener;

public interface QueryGestureInterface {
    void success(boolean hasSetGesture);
    void error(String code,String msg);
}
