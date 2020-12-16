package com.szny.energyproject.mvp.models;

import com.szny.energyproject.entity.LoginEntity;
import com.szny.energyproject.entity.LogoutEntity;
import com.szny.energyproject.internet.RetrofitManager;
import com.szny.energyproject.mvp.BaseModel;
import java.util.HashMap;
import java.util.Map;
import io.reactivex.Observable;

public class LoginModel extends BaseModel {

    //登录
    public Observable<LoginEntity> login(String username, String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("grant_type", "password");
        params.put("client_id", "android");
        params.put("client_secret", "65900786");
        params.put("username", username);
        params.put("password", password);
        //RequestBody body = RetrofitManager.getInstance().createBodyByMap(params);
        return RetrofitManager.getInstance().getInternetService().login(params);
    }

    //刷新access_token
    public Observable<LoginEntity> refreshToken(String refresh_token) {
        Map<String, Object> params = new HashMap<>();
        params.put("grant_type", "password");
        params.put("client_id", "android");
        params.put("client_secret", "65900786");
        params.put("refresh_token", refresh_token);
        //RequestBody body = RetrofitManager.getInstance().createBodyByMap(params);
        return RetrofitManager.getInstance().getInternetService().login(params);
    }

    //退出登录
    public Observable<LogoutEntity> logout(String access_token) {
        return RetrofitManager.getInstance().getInternetService().logout(access_token);
    }
}
