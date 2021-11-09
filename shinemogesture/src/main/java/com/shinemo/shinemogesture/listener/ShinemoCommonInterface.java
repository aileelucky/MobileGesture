package com.shinemo.shinemogesture.listener;

public interface ShinemoCommonInterface<T> {
    void success(T o);
    void error(String code,String msg);
    void end();
}
