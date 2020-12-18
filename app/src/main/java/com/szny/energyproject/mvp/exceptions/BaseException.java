package com.szny.energyproject.mvp.exceptions;

public class BaseException extends RuntimeException {
    private String msg;
    private int errCode;

    public BaseException(String msg) {
        this.msg = msg;
    }
    public BaseException(String msg, int errCode) {
        this.msg = msg;
        this.errCode = errCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }
}
