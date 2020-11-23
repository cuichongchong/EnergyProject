package com.szny.energyproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

public final class SPUtils {
    private final static String name = "config";
    private final static int mode = Context.MODE_PRIVATE;

    private static final SPUtils instance = new SPUtils();

    private SharedPreferences sp;

    public static SPUtils getInstance(){
        return instance;
    }

    public void init(Context context){
        sp = context.getSharedPreferences(name, mode);
    }

    /**
     * 保存首选项
     * @param key
     * @param value
     */
    public void saveBoolean(String key, boolean value){
        if (sp == null){
            throw new RuntimeException("没有初始化方法");
        }
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }
    public  void saveInt(String key, int value){
        if (sp == null){
            throw new RuntimeException("没有初始化方法");
        }
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.apply();
    }
    public  void saveString(String key, String value){
        if (sp == null){
            throw new RuntimeException("没有初始化方法");
        }
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.apply();
    }
    public void saveLong(String key, long value){
        if (sp == null){
            throw new RuntimeException("没有初始化方法");
        }
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong(key, value);
        edit.apply();
    }

    /**
     * 获取首选项
     * @param key
     * @param defValue
     * @return
     */
    public  boolean getBoolean(String key, boolean defValue){
        if (sp == null){
            throw new RuntimeException("没有初始化方法");
        }
        return sp.getBoolean(key, defValue);
    }

    public  int getInt(String key, int defValue){
        if (sp == null){
            throw new RuntimeException("没有初始化方法");
        }
        return sp.getInt(key, defValue);
    }

    public String getString(String key, String defValue){
        if (sp == null){
            throw new RuntimeException("没有初始化方法");
        }
        return sp.getString(key, defValue);
    }

    public long getLong(String key, long defValue){
        if (sp == null){
            throw new RuntimeException("没有初始化方法");
        }
        return sp.getLong(key, defValue);
    }

    public void remove(String key){
        if (sp == null){
            throw new RuntimeException("没有初始化方法");
        }
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(key);
        edit.apply();
    }
}
