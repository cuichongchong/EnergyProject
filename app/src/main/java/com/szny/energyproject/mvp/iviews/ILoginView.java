package com.szny.energyproject.mvp.iviews;

import com.szny.energyproject.entity.LoginEntity;
import com.szny.energyproject.entity.LogoutEntity;
import com.szny.energyproject.mvp.IBaseView;

public interface ILoginView extends IBaseView<LoginEntity> {
    //刷新access_token的返回
    void refreshToken(LoginEntity data);
    //退出登录的返回
    void logout(LogoutEntity data);
}
