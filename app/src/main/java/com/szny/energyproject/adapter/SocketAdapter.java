package com.szny.energyproject.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.szny.energyproject.R;
import com.szny.energyproject.base.BaseRecyclerViewAdapter;
import com.szny.energyproject.base.BaseViewHolder;
import com.szny.energyproject.entity.ControlEntity;

import java.util.List;

/**
 * 首页插座展示适配器
 * */
public class SocketAdapter extends BaseRecyclerViewAdapter<ControlEntity.DeviceListBean> {

    private Context context;

    //插座总闸门的状态
    public boolean isGate;

    //定义item中控件的点击回调事件
    public interface OnItemsClickListener  {
        void onItemClick(View v, int position);
    }

    private OnItemsClickListener mOnItemsClickListener;

    public void setOnItemsClickListener(OnItemsClickListener listener) {
        this.mOnItemsClickListener  = listener;
    }

    public SocketAdapter(Context context, List<ControlEntity.DeviceListBean> data, boolean isGate) {
        super(data, R.layout.item_socket);
        this.context = context;
        this.isGate = isGate;
    }

    @Override
    protected void convert(BaseViewHolder holder, ControlEntity.DeviceListBean item, int position) {
        //插座名字
        TextView tvName = holder.getView(R.id.tv_name);
        //插座合分闸
        TextView tvSocket = holder.getView(R.id.tv_socket);
        //插座开关状态
        ImageView ivSocket = holder.getView(R.id.iv_socket);
        //插座工作显示状态
        TextView tvStatus = holder.getView(R.id.tv_status);

        tvName.setText(item.getName());

//        if(!isGate){
//            tvSocket.setText("分闸");
//            ivSocket.setBackgroundResource(R.mipmap.ic_switch_close);
//        }else{
//            if(item.isOpen()){
//                tvSocket.setText("合闸");
//                ivSocket.setBackgroundResource(R.mipmap.ic_switch_open);
//            }else{
//                tvSocket.setText("分闸");
//                ivSocket.setBackgroundResource(R.mipmap.ic_switch_close);
//            }
//        }
//
//        tvStatus.setText(item.getStatue());
//
//        ivSocket.setOnClickListener(view -> {
//            if(!isGate){
//                ToastUtils.showShort(context,"请先将插座总闸合闸");
//            }else{
//                if(mOnItemsClickListener != null){
//                    mOnItemsClickListener.onItemClick(ivSocket,position);
//                }
//            }
//        });

    }
}
