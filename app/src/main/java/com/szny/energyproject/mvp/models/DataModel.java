package com.szny.energyproject.mvp.models;

import com.szny.energyproject.base.BaseEntity;
import com.szny.energyproject.entity.CarbonEntity;
import com.szny.energyproject.entity.ControlEntity;
import com.szny.energyproject.entity.DataEntity;
import com.szny.energyproject.entity.RecordEntity;
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

    //获取不同权限下的成员列表
    public Observable<BaseEntity<List<RoomEntity>>> getMember(int userId,int groupId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("groupId", groupId);
        RequestBody body = RetrofitManager.getInstance().createBodyByMap(params);
        return RetrofitManager.getInstance().getInternetService().getMember(body);
    }

    //获取数据信息 之前接口
    public Observable<BaseEntity<List<DataEntity>>> getReport(int roomId,String year,String month) {
        Map<String, Object> params = new HashMap<>();
        params.put("roomId", roomId);
        params.put("year", year);
        params.put("month", month);
        RequestBody body = RetrofitManager.getInstance().createBodyByMap(params);
        return RetrofitManager.getInstance().getInternetService().getReport(body);
    }

    //获取权限列表
    public Observable<BaseEntity<List<RoomEntity>>> getGroup() {
        /*Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("groupId", groupId);
        RequestBody body = RetrofitManager.getInstance().createBodyByMap(params);*/
        return RetrofitManager.getInstance().getInternetService().getGroup();
    }

    //获取能耗分析数据 新接口
    public Observable<BaseEntity<List<RecordEntity>>> getRecord(int memberId,int type,String time) {
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("type", type);
        params.put("time", time);
        RequestBody body = RetrofitManager.getInstance().createBodyByMap(params);
        return RetrofitManager.getInstance().getInternetService().getRecord(body);
    }

    //获取碳排放量
    public Observable<BaseEntity<List<CarbonEntity>>> getCarbon(int groupId,int type,String time) {
        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("type", type);
        params.put("time", time);
        RequestBody body = RetrofitManager.getInstance().createBodyByMap(params);
        return RetrofitManager.getInstance().getInternetService().getCarbon(body);
    }
}
