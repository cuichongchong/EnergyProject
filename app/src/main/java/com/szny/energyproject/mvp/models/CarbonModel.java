package com.szny.energyproject.mvp.models;

import com.szny.energyproject.base.BaseEntity;
import com.szny.energyproject.entity.CarbonEntity;
import com.szny.energyproject.internet.RetrofitManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * 碳排放量页面请求model
 * */
public class CarbonModel {
    //获取碳排放量
    public Observable<BaseEntity<List<CarbonEntity>>> getCarbon(String year) {
        Map<String, Object> params = new HashMap<>();
        params.put("type",1);
        params.put("groupId", 24);
        params.put("time", year);
        RequestBody body = RetrofitManager.getInstance().createBodyByMap(params);
        return RetrofitManager.getInstance().getInternetService().getCarbon(body);
    }
}
