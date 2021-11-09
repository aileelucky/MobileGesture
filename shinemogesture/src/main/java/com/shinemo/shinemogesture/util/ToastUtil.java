package com.shinemo.shinemogesture.util;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.StringRes;

public class ToastUtil {

    static Toast toast = null;

    public static void show(Context context, String info) {
        if(info==null || info.trim().length()<1){
            return;
        }
        if (null == toast) {
            toast = Toast.makeText(context, info, Toast.LENGTH_SHORT);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.cancel();
            toast = Toast.makeText(context, info, Toast.LENGTH_SHORT);
        }
        toast.show();
//		Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }

    public static void show(Context context, @StringRes int info) {
        if (null == toast) {
            toast = Toast.makeText(context, info, Toast.LENGTH_SHORT);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.cancel();
            toast = Toast.makeText(context, info, Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public static void showLong(Context context, String info) {
        if(info==null || info.trim().length()<1){
            return;
        }
        if (null == toast) {
            toast = Toast.makeText(context, info, Toast.LENGTH_LONG);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(info);
        }
        toast.show();
    }

    public static void showLong(Context context, @StringRes int info) {
        if (null == toast) {
            toast = Toast.makeText(context, info, Toast.LENGTH_LONG);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(info);
        }
        toast.show();
//		Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }

    public static void showMaxLengthTip(Context context, int size) {
        if (null == toast) {
            toast = Toast.makeText(context, "不能超过" + size + "字", Toast.LENGTH_LONG);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText("不能超过" + size + "字");
        }
        toast.show();
    }

}
