package com.szny.energyproject.internet;

import com.google.gson.Gson;
import com.szny.energyproject.constant.ConstantValues;
import com.szny.energyproject.constant.UrlHelper;
import com.szny.energyproject.entity.LogoutEntity;
import com.szny.energyproject.entity.LoginEntity;
import com.szny.energyproject.utils.SPUtils;
import java.io.IOException;
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
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
                            builder.addHeader("Authorization", "bearer "+ SPUtils.getInstance().getString(ConstantValues.TOKEN,""));
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
        @POST("oauth/token")
        Observable<LoginEntity> login(@FieldMap Map<String, Object> map);

        //刷新access_token
        @FormUrlEncoded
        @POST("oauth/token")
        Observable<LoginEntity> refreshToken(@FieldMap Map<String, Object> map);

        //退出登录
        @GET("revokeAndroid")
        Observable<LogoutEntity> logout(@Query("access_token")String access_token);
    }

}
