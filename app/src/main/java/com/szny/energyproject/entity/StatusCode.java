package com.szny.energyproject.entity;

public enum  StatusCode {

    SUCCESS(0,"成功"),
    FAILURE(1,"失败"),
    ERROR(2,"错误");

    public int status;
    public String value;

    StatusCode(int status, String value) {
        this.status = status;
        this.value = value;
    }
}
