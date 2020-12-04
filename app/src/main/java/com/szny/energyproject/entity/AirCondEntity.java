package com.szny.energyproject.entity;

/**
* 空调列表实体类，测试使用
* */
public class AirCondEntity {
    private String name;
    private int indoorTemp;//室内温度
    private int setTemp;//设置的温度
    private boolean isHot;//制冷制热
    private boolean isAuto;//是否自动
    private int gear;//档的等级
    private boolean isOpen;//空调是否开关

    public AirCondEntity(String name, int indoorTemp, int setTemp, boolean isHot, boolean isAuto, int gear, boolean isOpen) {
        this.name = name;
        this.indoorTemp = indoorTemp;
        this.setTemp = setTemp;
        this.isHot = isHot;
        this.isAuto = isAuto;
        this.gear = gear;
        this.isOpen = isOpen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndoorTemp() {
        return indoorTemp;
    }

    public void setIndoorTemp(int indoorTemp) {
        this.indoorTemp = indoorTemp;
    }

    public int getSetTemp() {
        return setTemp;
    }

    public void setSetTemp(int setTemp) {
        this.setTemp = setTemp;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }

    public boolean isAuto() {
        return isAuto;
    }

    public void setAuto(boolean auto) {
        isAuto = auto;
    }

    public int getGear() {
        return gear;
    }

    public void setGear(int gear) {
        this.gear = gear;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
