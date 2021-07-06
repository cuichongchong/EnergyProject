package com.szny.energyproject.mvp.models;

import com.szny.energyproject.base.BaseEntity;
import com.szny.energyproject.entity.ControlEntity;
import com.szny.energyproject.entity.RoomEntity;
import com.szny.energyproject.internet.RetrofitManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * 控制首页请求model
 * */
public class ControlModel {

    //获取房间列表
    public Observable<BaseEntity<List<RoomEntity>>> getMember(int userId,int groupId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("groupId", groupId);
        RequestBody body = RetrofitManager.getInstance().createBodyByMap(params);
        return RetrofitManager.getInstance().getInternetService().getMember(body);
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
