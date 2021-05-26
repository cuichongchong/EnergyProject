package com.szny.energyproject.adapter;

import android.content.Context;
import android.widget.TextView;
import com.szny.energyproject.R;
import com.szny.energyproject.base.BaseRecyclerViewAdapter;
import com.szny.energyproject.base.BaseViewHolder;
import com.szny.energyproject.entity.CarbonEntity;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 碳排放量列表展示适配器
 * */
public class CarbonAdapter extends BaseRecyclerViewAdapter<CarbonEntity> {

    private Context context;

    public CarbonAdapter(Context context,List<CarbonEntity> data) {
        super(data, R.layout.item_carbon_emission);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, CarbonEntity item, int position) {
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvEle = holder.getView(R.id.tv_ele);
        TextView tvEleCo2 = holder.getView(R.id.tv_ele_co2);

        tvName.setText(item.getMemberName());
        tvEle.setText(new DecimalFormat("0.00").format(item.getElec()));
        tvEleCo2.setText(new DecimalFormat("0.00").format(item.getElec()*1.2));
    }
}
