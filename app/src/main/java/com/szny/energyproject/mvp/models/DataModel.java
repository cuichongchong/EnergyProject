package com.szny.energyproject.mvp.models;

import com.szny.energyproject.base.BaseEntity;
import com.szny.energyproject.entity.ControlEntity;
import com.szny.energyproject.entity.DataEntity;
import com.szny.energyproject.entity.RoomEntity;
import com.szny.energyproject.internet.RetrofitManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * 数据页面请求model
 * */
public class DataModel {

    //获取房间列表
    public Observable<BaseEntity<List<RoomEntity>>> getRoomList(int id) {
        return RetrofitManager.getInstance().getInternetService().getRoomList(id);
    }

    //获取数据信息
    public Observable<BaseEntity<List<DataEntity>>> getReport(int roomId,String year,String month) {
        Map<String, Object> params = new HashMap<>();
        params.put("roomId", roomId);
        params.put("year", year);
        params.put("month", month);
        RequestBody body = RetrofitManager.getInstance().createBodyByMap(params);
        return RetrofitManager.getInstance().getInternetService().getReport(body);
    }

    //获取首页信息
    public Observable<BaseEntity<ControlEntity>> getInfo(int userId, int roomId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("roomId", roomId);
        RequestBody body = RetrofitManager.getInstance().createBodyByMap(params);
        return RetrofitManager.getInstance().getInternetService().getInfo(body);
    }
}
