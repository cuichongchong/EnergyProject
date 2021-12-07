package com.szny.energyproject.constant;

/**
 * 地址配置
 * */
public class UrlHelper {
    //内网服务器环境
    //public static String BASE_IP = "172.10.11.66:48080";
    //外网服务器环境
    public static String BASE_IP = "218.29.131.170:48080";

    //内网signal地址
    //public static String SIGNALR_URL = "http://172.10.11.66:30100/collector";
    //外网signal地址
    public static String SIGNALR_URL = "http://218.29.131.170:30100/collector";

    public static String BASE_URL = "http://" + BASE_IP;
}
