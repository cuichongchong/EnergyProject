package com.szny.energyproject.entity;

/**
 * signal接收消息实体
 * */
public class SupconMessage {
    public String collector;
    public int index;
    public String command;
    public int Code;
    public Object message;

    public String getCollector() {
        return collector;
    }

    public void setCollector(String collector) {
        this.collector = collector;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    @Override
    public String toString() {
        return "SupconMessage{" +
                "collector='" + collector + '\'' +
                ", index=" + index +
                ", command='" + command + '\'' +
                ", Code=" + Code +
                ", message=" + message +
                '}';
    }
}
