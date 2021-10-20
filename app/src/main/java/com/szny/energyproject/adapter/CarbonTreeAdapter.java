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
 * 碳排放植树列表展示适配器
 * */
public class CarbonTreeAdapter extends BaseRecyclerViewAdapter<CarbonEntity> {

    private Context context;

    public CarbonTreeAdapter(Context context, List<CarbonEntity> data) {
        super(data, R.layout.item_carbon_tree);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, CarbonEntity item, int position) {
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvCo2 = holder.getView(R.id.tv_co2);
        TextView tvTree = holder.getView(R.id.tv_tree);

        tvName.setText(item.getMemberName());
        tvCo2.setText(new DecimalFormat("0.00").format(item.getElec()*0.997));
        tvTree.setText(new DecimalFormat("0").format(item.getElec()*0.997/220));
    }
}
