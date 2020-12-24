package com.szny.energyproject.mvp.iviews;

import com.szny.energyproject.entity.ControlEntity;
import com.szny.energyproject.entity.LogoutEntity;
import com.szny.energyproject.entity.RoomEntity;
import com.szny.energyproject.entity.UserEntity;
import com.szny.energyproject.mvp.IBaseView;
import java.util.List;

public interface IControlView extends IBaseView<UserEntity> {
    //退出登录的返回
    void logout(LogoutEntity data);
    //获取房间列表的返回
    void getRoomList(List<RoomEntity> data);
    //获取首页信息的返回
    void getInfo(ControlEntity data);
}
