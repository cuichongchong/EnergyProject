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

    private SignalRManager(){
        try {
            //初始化
            String SIGNSLR_URL = "http://192.168.0.9:49315/collector";
            hubConnection = HubConnectionBuilder.create(SIGNSLR_URL)
                    .withAccessTokenProvider(Single.defer(() -> {
                        // Your logic here.
                        return Single.just(SPUtils.getInstance().getString(ConstantValues.TOKEN,""));
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SignalRManager getInstance(){
        return SignalRManagerHolder.instance;
    }

    public HubConnection getHubConnection(){
        return hubConnection;
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
}
