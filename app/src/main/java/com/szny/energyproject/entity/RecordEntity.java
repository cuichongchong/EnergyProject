package com.szny.energyproject.entity;

import java.io.Serializable;

//能耗分析返回实体
public class RecordEntity implements Serializable {

    /**
     * heat : 0.0
     * year : 2021
     * elec : 319.97
     * air : 0.0
     * water : 0.0
     * special : 0.0
     * month : 4
     * light : 0.0
     * data_time : 2021-04-15
     * gas : 0.0
     * socket : 319.97
     * power : 0.0
     * day : 15
     */

    private double heat;
    private int year;
    private double elec;
    private double air;
    private double water;
    private double special;
    private int month;
    private double light;
    private String data_time;
    private double gas;
    private double socket;
    private double power;
    private String day;
    private String hour;

    public double getHeat() {
        return heat;
    }

    public void setHeat(double heat) {
        this.heat = heat;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getElec() {
        return elec;
    }

    public void setElec(double elec) {
        this.elec = elec;
    }

    public double getAir() {
        return air;
    }

    public void setAir(double air) {
        this.air = air;
    }

    public double getWater() {
        return water;
    }

    public void setWater(double water) {
        this.water = water;
    }

    public double getSpecial() {
        return special;
    }

    public void setSpecial(double special) {
        this.special = special;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getLight() {
        return light;
    }

    public void setLight(double light) {
        this.light = light;
    }

    public String getData_time() {
        return data_time;
    }

    public void setData_time(String data_time) {
        this.data_time = data_time;
    }

    public double getGas() {
        return gas;
    }

    public void setGas(double gas) {
        this.gas = gas;
    }

    public double getSocket() {
        return socket;
    }

    public void setSocket(double socket) {
        this.socket = socket;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
