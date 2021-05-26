package com.szny.energyproject.mvp.models;

import com.szny.energyproject.base.BaseEntity;
import com.szny.energyproject.entity.LogoutEntity;
import com.szny.energyproject.entity.UserEntity;
import com.szny.energyproject.internet.RetrofitManager;
import io.reactivex.Observable;

/**
 * 首页请求model
 * */
public class HomeModel {

    //获取用户信息
    public Observable<BaseEntity<UserEntity>> userInfo() {
        return RetrofitManager.getInstance().getInternetService().userInfo();
    }

    //退出登录
    public Observable<LogoutEntity> logout(String access_token) {
        return RetrofitManager.getInstance().getInternetService().logout(access_token);
    }

}
