package com.szny.energyproject.application;

import android.app.Application;
import android.content.Context;

import com.szny.energyproject.utils.LogUtils;
import com.szny.energyproject.utils.SPUtils;

public class MyApplication extends Application {
    private final String TAG = getClass().getSimpleName();
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        //初始化sp
        SPUtils.getInstance().init(this);

        //设置是否打印log日志,线上环境关闭
        LogUtils.isDebug = true;
    }
}
