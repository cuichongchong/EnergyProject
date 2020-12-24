package com.szny.energyproject.entity;


/**
 * 数据页面展示实体
 * */
public class DataEntity {

    /**
     * dataTime : 2020-12-01
     * light : 1.0
     * socket : 2.0
     * air : 3.0
     */

    private String dataTime;
    private double light;
    private double socket;
    private double air;

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public double getLight() {
        return light;
    }

    public void setLight(double light) {
        this.light = light;
    }

    public double getSocket() {
        return socket;
    }

    public void setSocket(double socket) {
        this.socket = socket;
    }

    public double getAir() {
        return air;
    }

    public void setAir(double air) {
        this.air = air;
    }
}
