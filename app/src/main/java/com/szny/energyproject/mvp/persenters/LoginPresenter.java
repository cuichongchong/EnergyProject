package com.szny.energyproject.mvp.persenters;

import com.szny.energyproject.mvp.BasePresenter;
import com.szny.energyproject.mvp.iviews.ILoginView;
import com.szny.energyproject.mvp.models.LoginModel;
import com.szny.energyproject.utils.LogUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter extends BasePresenter<ILoginView> {
    private LoginModel loginModel = new LoginModel();

    //登录
    public void login(String username, String password) {
        Disposable disposable = loginModel.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseEntity -> {
                    LogUtils.e("doing","登录成功 "+baseEntity.getAccess_token());
                    if(baseView != null){
                        baseView.success(baseEntity);
                    }
                }, throwable -> {
                    LogUtils.e("doing","登录失败 ");
                    if (baseView != null) {
                        baseView.failed(throwable);
                    }
                });
        disposables.add(disposable);
    }

    //刷新access_token
    public void refreshToken(String refresh_token) {
        Disposable disposable = loginModel.refreshToken(refresh_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseEntity -> {
                    LogUtils.e("doing","token刷新成功 "+baseEntity.getAccess_token());
                    if(baseView != null){
                        baseView.refreshToken(baseEntity);
                    }
                }, throwable -> {
                    LogUtils.e("doing","token刷新失败 ");
                    if (baseView != null) {
                        baseView.failed(throwable);
                    }
                });
        disposables.add(disposable);
    }

}
