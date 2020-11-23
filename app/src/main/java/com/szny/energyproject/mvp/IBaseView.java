package com.szny.energyproject.mvp;

public interface IBaseView<T> {
    void success(T t);
    void failed(Throwable e);
}
