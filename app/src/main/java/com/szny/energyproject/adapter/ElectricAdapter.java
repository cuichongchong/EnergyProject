package com.szny.energyproject.adapter;

import android.widget.TextView;

import com.szny.energyproject.R;
import com.szny.energyproject.base.BaseRecyclerViewAdapter;
import com.szny.energyproject.base.BaseViewHolder;
import com.szny.energyproject.entity.ElectricEntity;

import java.util.List;

/**
 * 首页电量展示适配器
 * */
public class ElectricAdapter extends BaseRecyclerViewAdapter<ElectricEntity.ElectricListBean> {

    public ElectricAdapter(List<ElectricEntity.ElectricListBean> data) {
        super(data, R.layout.item_electric);
    }

    @Override
    protected void convert(BaseViewHolder holder, ElectricEntity.ElectricListBean item, int position) {
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvValue = holder.getView(R.id.tv_value);

        tvName.setText(item.getName());
        tvValue.setText(item.getValue());
    }
}
