package com.szny.energyproject.mvp;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class BasePresenter<T extends IBaseView> {
    protected final String TAG = getClass().getSimpleName();

    protected List<Disposable> disposables = new ArrayList<>();
    protected T baseView;

    public void attachView(T baseView){
        this.baseView = baseView;
    }

    public void detachView(){
        detachDisposables();
        baseView = null;
    }

    public void detachDisposables(){
        for (Disposable disposable:disposables){
            if (disposable!=null && !disposable.isDisposed()){
                disposable.dispose();
            }
        }
    }
}