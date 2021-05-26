package com.szny.energyproject.mvp.iviews;

import com.szny.energyproject.entity.LogoutEntity;
import com.szny.energyproject.entity.UserEntity;
import com.szny.energyproject.mvp.IBaseView;

public interface IHomeView extends IBaseView<UserEntity> {
    //退出登录的返回
    void logout(LogoutEntity data);
}
