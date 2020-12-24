package com.szny.energyproject.entity;

/**
 * 首页电量展示实体
 * */
public class ElectricEntity {
    private String name;
    private String value;

    public ElectricEntity(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
