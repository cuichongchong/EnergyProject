package com.szny.energyproject.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.SwitchCompat;

import com.szny.energyproject.R;
import com.szny.energyproject.base.BaseRecyclerViewAdapter;
import com.szny.energyproject.base.BaseViewHolder;
import com.szny.energyproject.entity.ControlEntity;
import java.util.List;

/**
 * 首页空调展示适配器
 * */
public class AirCondAdapter extends BaseRecyclerViewAdapter<ControlEntity.DeviceListBean> {

    private Context context;

    //空调总闸门的状态
    public boolean isGate;

    //定义item中控件的点击回调事件
    public interface OnItemsClickListener  {
        void onItemClick(View v, int position,boolean checked);
    }

    private OnItemsClickListener mOnItemsClickListener;

    public void setOnItemsClickListener(OnItemsClickListener  listener) {
        this.mOnItemsClickListener  = listener;
    }

    public AirCondAdapter(Context context, List<ControlEntity.DeviceListBean> data, boolean isGate) {
        super(data, R.layout.item_air_conditioner);
        this.context = context;
        this.isGate = isGate;
    }

    @Override
    protected void convert(BaseViewHolder holder, ControlEntity.DeviceListBean item, final int position) {
        //空调名字
        TextView tvName = holder.getView(R.id.tv_name);
        //空调开关
        SwitchCompat setSwitch = holder.getView(R.id.set_switch);
        //室内温度
        TextView tvIndoorTemp = holder.getView(R.id.tv_indoor_temp);
        //当前模式(制冷/制热)
        TextView tvModel = holder.getView(R.id.tv_model);
        //当前风速(自动/一档/二档/三档)
        TextView tvGear = holder.getView(R.id.tv_gear);
        //设置的温度
        TextView tvSetTemp = holder.getView(R.id.tv_set_temp);
        //设置模式
        TextView tvSetModel = holder.getView(R.id.tv_set_model);
        //设置风速
        TextView tvSetGear = holder.getView(R.id.tv_set_gear);
        //温度减
        ImageView ivDesc = holder.getView(R.id.iv_desc);
        //温度加
        ImageView ivAdd = holder.getView(R.id.iv_add);


        tvName.setText(item.getName());

//        setSwitch.setChecked(item.isOpen());
//
//        tvIndoorTemp.setText(String.valueOf(item.getIndoorTemp()));
//
//        tvSetTemp.setText(String.valueOf(item.getSetTemp()));
//
//        if(item.isHot()){
//            tvModel.setText("制热");
//        }else{
//            tvModel.setText("制冷");
//        }
//
//        if(item.isAuto()){
//            tvGear.setText("自动");
//        }else{
//            switch (item.getGear()){
//                case 1:
//                    tvGear.setText("一档");
//                    break;
//                case 2:
//                    tvGear.setText("二档");
//                    break;
//                case 3:
//                    tvGear.setText("三档");
//                    break;
//            }
//        }
//
//        //设置item中控件的点击回调事件
//        List<View> list = new ArrayList<>();
//        list.add(tvSetModel);list.add(tvSetGear);
//        list.add(ivDesc);list.add(ivAdd);
//        setOnClick(list,position);
//
//        //空调开关监听
//        setSwitch.setOnClickListener(view -> {
//            if(!isGate){
//                ToastUtils.showShort(context,"请先将空调总闸合闸");
//            }
//        });
//        setSwitch.setOnCheckedChangeListener((compoundButton, checked) -> {
//            if(isGate){
//                if(mOnItemsClickListener != null){
//                    mOnItemsClickListener.onItemClick(setSwitch,position,checked);
//                }
//            }else{
//                setSwitch.setChecked(false);
//            }
//        });
    }

    public void setOnClick(List<View> list,int position){
        for(View v : list){
            v.setOnClickListener(view ->{
                if(mOnItemsClickListener != null){
                    mOnItemsClickListener.onItemClick(v,position,false);
                }
            });
        }
    }

    public void setTextColor(View v,int resId){
        if(v instanceof TextView){
            ((TextView) v).setTextColor(context.getResources().getColor(resId));
        }
    }
}
