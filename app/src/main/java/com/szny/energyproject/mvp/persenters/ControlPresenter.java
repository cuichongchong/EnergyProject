package com.szny.energyproject.mvp.persenters;

import com.szny.energyproject.mvp.BasePresenter;
import com.szny.energyproject.mvp.exceptions.BaseException;
import com.szny.energyproject.mvp.iviews.IControlView;
import com.szny.energyproject.mvp.models.ControlModel;
import com.szny.energyproject.utils.LogUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 控制首页请求presenter
 * */
public class ControlPresenter extends BasePresenter<IControlView> {
    private ControlModel controlModel = new ControlModel();

    //获取房间列表
    public void getMember(int userId,int groupId) {
        Disposable disposable = controlModel.getMember(userId,groupId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseEntity -> {
                    if(baseView != null){
                        if(200 == baseEntity.getCode()){
                            LogUtils.e("doing","获取房间列表成功 "+baseEntity.getMsg());
                            baseView.success(baseEntity.getData());
                        }else{
                            LogUtils.e("doing","获取房间列表失败 "+baseEntity.getCode()+"  "+baseEntity.getMsg());
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

    //获取首页信息
    public void getInfo(int userId, int roomId) {
        Disposable disposable = controlModel.getInfo(userId,roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseEntity -> {
                    if(baseView != null){
                        if(200 == baseEntity.getCode()){
                            LogUtils.e("doing","获取首页信息成功 "+baseEntity.getMsg());
                            baseView.getInfo(baseEntity.getData());
                        }else{
                            LogUtils.e("doing","获取首页信息失败 "+baseEntity.getCode()+"  "+baseEntity.getMsg());
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
