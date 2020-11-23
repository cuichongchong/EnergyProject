package com.szny.energyproject.utils;

import android.util.Log;

public class LogUtils {
    public static boolean isDebug = true;
    private LogUtils(){
    }
    public static void d(String tag, String msg){
        if (isDebug){
            Log.d(tag,msg);
        }
    }
    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }


    public static void e(String tag, String msg) {
        if (isDebug)
            Log.e(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            Log.v(tag, msg);
    }
}
