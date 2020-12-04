package com.szny.energyproject.entity;


/**
 * 数据页面展示实体，测试使用
 * */
public class DataEntity {
    private String data;
    private int light;
    private int socket;
    private int air;
    private int sum;

    public DataEntity(String data, int light, int socket, int air, int sum) {
        this.data = data;
        this.light = light;
        this.socket = socket;
        this.air = air;
        this.sum = sum;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public int getSocket() {
        return socket;
    }

    public void setSocket(int socket) {
        this.socket = socket;
    }

    public int getAir() {
        return air;
    }

    public void setAir(int air) {
        this.air = air;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
