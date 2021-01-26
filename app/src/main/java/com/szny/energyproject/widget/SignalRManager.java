package com.szny.energyproject.widget;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.szny.energyproject.constant.ConstantValues;
import com.szny.energyproject.entity.SupconMessage;
import com.szny.energyproject.utils.SPUtils;
import io.reactivex.Single;

/**
 * signal通讯类
 * */
public class SignalRManager {
    private HubConnection hubConnection;

    private static class SignalRManagerHolder {
        private static final SignalRManager instance = new SignalRManager();
    }

    public static SignalRManager getInstance(){
        return SignalRManagerHolder.instance;
    }

    public HubConnection getHubConnection(){
        return hubConnection;
    }

    //初始化
    public void init(){
        new Thread(() -> {
            try {
                String SIGNSLR_URL = "http://172.10.11.66:49315/collector";
                String token = SPUtils.getInstance().getString(ConstantValues.TOKEN,"");
                hubConnection = HubConnectionBuilder.create(SIGNSLR_URL)
                        .withAccessTokenProvider(Single.defer(() -> {
                            // Your logic here.
                            return Single.just(token);
                        }))
                        .build();

                //关闭
                hubConnection.onClosed(exception -> {
                    close();
                });

                //注册接收方法
                hubConnection.on("TransferMessage", (message) -> {
                    //将结果监听回调出去
                    icallBack.receiver(message);
                }, SupconMessage.class);

                //开启连接
                hubConnection.start().blockingAwait();

                connectCallBack.isConnect(true);

            } catch (Exception e) {
                connectCallBack.isConnect(false);
                e.printStackTrace();
            }
        }).start();
    }

    public void close(){
        hubConnection.stop();
    }

    //收到返回消息的监听
    public IcallBack icallBack;
    public void setIcallBack(IcallBack icallBack){
        this.icallBack = icallBack;
    }
    public interface IcallBack{
        void receiver(SupconMessage message);
    }

    //连接成功的监听
    public connectCallBack connectCallBack;
    public void setConnectCallBack(connectCallBack connectCallBack){
        this.connectCallBack = connectCallBack;
    }
    public interface connectCallBack{
        void isConnect(Boolean isConnect);
    }
}
