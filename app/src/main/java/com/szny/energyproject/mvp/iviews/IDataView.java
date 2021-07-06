package com.szny.energyproject.mvp.iviews;

import com.szny.energyproject.entity.CarbonEntity;
import com.szny.energyproject.entity.ControlEntity;
import com.szny.energyproject.entity.DataEntity;
import com.szny.energyproject.entity.RecordEntity;
import com.szny.energyproject.entity.RoomEntity;
import com.szny.energyproject.mvp.IBaseView;
import java.util.List;

public interface IDataView extends IBaseView<List<CarbonEntity>> {
    //获取房间列表的返回
    void getMember(List<RoomEntity> data);
    //获取权限列表
    void getGroup(List<RoomEntity> data);
    //获取能耗分析
    void getRecord(List<RecordEntity> data);
}
