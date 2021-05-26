package com.szny.energyproject.mvp.iviews;

import com.szny.energyproject.entity.ControlEntity;
import com.szny.energyproject.entity.DataEntity;
import com.szny.energyproject.entity.RoomEntity;
import com.szny.energyproject.mvp.IBaseView;
import java.util.List;

public interface IDataView extends IBaseView<List<DataEntity>> {
    //获取房间列表的返回
    void getRoomList(List<RoomEntity> data);
    //获取首页信息的返回
    void getInfo(ControlEntity data);
}
