package com.szny.energyproject.utils;

import android.app.ActivityManager;
import android.content.Context;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by kylin on 2017/4/6.
 */

public class StringUtils {
    /**
     * 字符串转byte数组
     * */
    public static byte[] strTobytes(String str){
        byte[] b=null,data=null;
        try {
            b = str.getBytes("utf-8");
            data=new String(b,"utf-8").getBytes("gbk");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return data;
    }
    /**
     * byte数组拼接
     * */
    public static   byte[] byteMerger(byte[] byte_1, byte[] byte_2){
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    public static byte[] strTobytes(String str , String charset){
        byte[] b=null,data=null;
        try {
            b = str.getBytes("utf-8");
            data=new String(b,"utf-8").getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 判断对象是否为null，为null返回空字符串
     * */
    public static String nullToStr(String str){
        String s = "";
        if(str == null){
            return s;
        }
        return str;
    }

    /**
     * 将double转为字符串，保留两位小数
     * */
    public static String doulbeToStr(double d){
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }
}
