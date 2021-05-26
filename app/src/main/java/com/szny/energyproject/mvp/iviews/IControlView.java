package com.szny.energyproject.mvp.iviews;

import com.szny.energyproject.entity.ControlEntity;
import com.szny.energyproject.entity.RoomEntity;
import com.szny.energyproject.mvp.IBaseView;
import java.util.List;

public interface IControlView extends IBaseView<List<RoomEntity>> {
    //获取首页信息的返回
    void getInfo(ControlEntity data);
}
