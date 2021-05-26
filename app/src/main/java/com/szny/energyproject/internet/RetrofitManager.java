package com.szny.energyproject.internet;

import com.google.gson.Gson;
import com.szny.energyproject.base.BaseEntity;
import com.szny.energyproject.constant.ConstantValues;
import com.szny.energyproject.constant.UrlHelper;
import com.szny.energyproject.entity.CarbonEntity;
import com.szny.energyproject.entity.ControlEntity;
import com.szny.energyproject.entity.DataEntity;
import com.szny.energyproject.entity.LogoutEntity;
import com.szny.energyproject.entity.LoginEntity;
import com.szny.energyproject.entity.RoomEntity;
import com.szny.energyproject.entity.UserEntity;
import com.szny.energyproject.utils.SPUtils;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 *
 * 网络请求的单列存在
 */
public class RetrofitManager {
    private static String BASE_URL_RETROFIT = UrlHelper.BASE_URL + "/";
    private OkHttpClient mClient;
    private Retrofit mRetrofit;
    private InternetService mInternetService;
    private Gson gson;

    private static class RetrofitManagerHolder {
        private static final RetrofitManager instance = new RetrofitManager();
    }

    private RetrofitManager(){
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_RETROFIT)
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //.addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(new CustomerGsonConverterFactory())
                .build();
    }

    public InternetService getInternetService(){
        if (mInternetService==null){
            mInternetService =  mRetrofit.create(InternetService.class);
        }
        return mInternetService;
    }

    private OkHttpClient getOkHttpClient(){
        if (mClient==null){
            mClient = new OkHttpClient.Builder()
                    //添加通用的Header
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request.Builder builder = chain.request().newBuilder();
                            builder.addHeader("Authorization", "Bearer "+ SPUtils.getInstance().getString(ConstantValues.TOKEN,""));
                            return chain.proceed(builder.build());
                        }
                    })
                    .addInterceptor(new HandleErrorInterceptor())
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    //.addInterceptor(new LoggerInterceptor("TAG", true))
                    .build();
        }
        return mClient;
    }

    public static RetrofitManager getInstance(){
        return RetrofitManagerHolder.instance;
    }

    public RequestBody createBodyByMap(Map<String, Object> params){
        if (gson == null){
            gson = new Gson();
        }
        String json = gson.toJson(params);
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
    }

    public interface InternetService{
        //登录
        @FormUrlEncoded
        @POST("auth/oauth/token")
        Observable<LoginEntity> login(@FieldMap Map<String, Object> map);

        //刷新access_token
        @FormUrlEncoded
        @POST("auth/oauth/token")
        Observable<LoginEntity> refreshToken(@FieldMap Map<String, Object> map);

        //退出登录
        @GET("auth/revokeAndroid")
        Observable<LogoutEntity> logout(@Query("access_token")String access_token);

        //获取用户信息
        @GET("api/systematic/user/info")
        Observable<BaseEntity<UserEntity>> userInfo();

        //获取房间列表
        @GET("api/client/desktop/getRoom/{id}")
        Observable<BaseEntity<List<RoomEntity>>> getRoomList(@Path("id")int id);

        //获取首页信息
        @POST("api/client/desktop/getInfo")
        Observable<BaseEntity<ControlEntity>> getInfo(@Body RequestBody requestBody);

        //获取数据信息
        @POST("api/client/analysis/getReport")
        Observable<BaseEntity<List<DataEntity>>> getReport(@Body RequestBody requestBody);

        //获取房间碳排放量
        @POST("api/display/data/carbon")
        Observable<BaseEntity<List<CarbonEntity>>> getCarbon(@Body RequestBody requestBody);
    }

}
