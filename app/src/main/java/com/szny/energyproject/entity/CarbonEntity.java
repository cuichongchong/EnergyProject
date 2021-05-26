package com.szny.energyproject.entity;

/**
 * 碳排放量实体
 */
public class CarbonEntity {
    /**
     * heat : 0.0
     * year : 2021
     * heatCarbon : 0.0
     * waterCarbon : 0.0
     * gas : 0.0
     * name : 能源 530办公室
     * elec : 24.38
     * elecCarbon : 29.26
     * water : 0.0
     * gasCarbon : 0.0
     */

    private String heat;
    private String year;
    private String heatCarbon;
    private String waterCarbon;
    private String gas;
    private String memberName;
    private Double elec;
    private String elecCarbon;
    private String water;
    private String gasCarbon;

    public String getHeat() {
        return heat;
    }

    public void setHeat(String heat) {
        this.heat = heat;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getHeatCarbon() {
        return heatCarbon;
    }

    public void setHeatCarbon(String heatCarbon) {
        this.heatCarbon = heatCarbon;
    }

    public String getWaterCarbon() {
        return waterCarbon;
    }

    public void setWaterCarbon(String waterCarbon) {
        this.waterCarbon = waterCarbon;
    }

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Double getElec() {
        return elec;
    }

    public void setElec(Double elec) {
        this.elec = elec;
    }

    public String getElecCarbon() {
        return elecCarbon;
    }

    public void setElecCarbon(String elecCarbon) {
        this.elecCarbon = elecCarbon;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getGasCarbon() {
        return gasCarbon;
    }

    public void setGasCarbon(String gasCarbon) {
        this.gasCarbon = gasCarbon;
    }
}
