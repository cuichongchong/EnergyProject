package com.szny.energyproject.mvp.persenters;

import com.szny.energyproject.mvp.BasePresenter;
import com.szny.energyproject.mvp.exceptions.BaseException;
import com.szny.energyproject.mvp.iviews.IDataView;
import com.szny.energyproject.mvp.models.DataModel;
import com.szny.energyproject.utils.LogUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DataPresenter extends BasePresenter<IDataView> {
    private DataModel dataModel = new DataModel();

    //获取数据信息
    public void getReport(int roomId,String year,String month) {
        Disposable disposable = dataModel.getReport(roomId,year,month)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseEntity -> {
                    if(baseView != null){
                        if(200 == baseEntity.getCode()){
                            LogUtils.e("doing","获取数据信息成功 "+baseEntity.getMsg());
                            baseView.success(baseEntity.getData());
                        }else{
                            LogUtils.e("doing","获取数据信息失败 "+baseEntity.getCode()+"  "+baseEntity.getMsg());
                            baseView.failed(new BaseException(baseEntity.getMsg(),baseEntity.getCode()));
                        }
                    }
                }, throwable -> {
                    if (baseView != null) {
                        baseView.failed(throwable);
                    }
                });
        disposables.add(disposable);
    }
}
