package com.szny.energyproject.mvp.exceptions;

public class BaseException extends RuntimeException {
    private String msg;
    private String errCode;

    public BaseException(String msg) {
        this.msg = msg;
    }
    public BaseException(String msg, String errCode) {
        this.msg = msg;
        this.errCode = errCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }
}
