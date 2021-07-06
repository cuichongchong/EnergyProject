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

    //获取权限下成员列表
    public void getMember(int userId,int groupId) {
        Disposable disposable = dataModel.getMember(userId, groupId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseEntity -> {
                    if(baseView != null){
                        if(200 == baseEntity.getCode()){
                            LogUtils.e("doing","获取成员列表成功 "+baseEntity.getMsg());
                            baseView.getMember(baseEntity.getData());
                        }else{
                            LogUtils.e("doing","获取成员列表失败 "+baseEntity.getCode()+"  "+baseEntity.getMsg());
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

    /*//获取数据信息，之前接口
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
    }*/

    //获取权限分组列表
    public void getGroup() {
        Disposable disposable = dataModel.getGroup()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseEntity -> {
                    if(baseView != null){
                        if(200 == baseEntity.getCode()){
                            LogUtils.e("doing","获取权限分组成功 "+baseEntity.getMsg());
                            baseView.getGroup(baseEntity.getData());
                        }else{
                            LogUtils.e("doing","获取权限分组失败 "+baseEntity.getCode()+"  "+baseEntity.getMsg());
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

    //获取能耗分析数据
    public void getRecord(int memberId,int type,String time) {
        Disposable disposable = dataModel.getRecord(memberId,type,time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseEntity -> {
                    if(baseView != null){
                        if(200 == baseEntity.getCode()){
                            LogUtils.e("doing","获取能耗分析数据成功 "+baseEntity.getMsg());
                            baseView.getRecord(baseEntity.getData());
                        }else{
                            LogUtils.e("doing","获取能耗分析数据失败 "+baseEntity.getCode()+"  "+baseEntity.getMsg());
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

    //获取碳排量数据
    public void getCarbon(int groupId,int type,String time) {
        Disposable disposable = dataModel.getCarbon(groupId,type,time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseEntity -> {
                    if(baseView != null){
                        if(200 == baseEntity.getCode()){
                            LogUtils.e("doing","获取碳排量成功 "+baseEntity.getMsg());
                            baseView.success(baseEntity.getData());
                        }else{
                            LogUtils.e("doing","获取碳排放量失败 "+baseEntity.getCode()+"  "+baseEntity.getMsg());
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
