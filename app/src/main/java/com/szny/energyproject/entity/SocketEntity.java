package com.szny.energyproject.entity;

/**
 * 插座列表实体类，测试使用
 * */
public class SocketEntity {
    private String name;//插座名称
    private boolean isOpen;//插座是否开关
    private String statue;//插座的工作状态

    public SocketEntity(String name, boolean isOpen, String statue) {
        this.name = name;
        this.isOpen = isOpen;
        this.statue = statue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }
}
