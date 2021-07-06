package com.szny.energyproject.adapter;

import android.content.Context;
import android.widget.TextView;
import com.szny.energyproject.R;
import com.szny.energyproject.base.BaseRecyclerViewAdapter;
import com.szny.energyproject.base.BaseViewHolder;
import com.szny.energyproject.entity.RecordEntity;
import com.szny.energyproject.utils.StringUtils;
import java.util.List;

/**
 * 重点用能列表展示适配器
 * */
public class EnergyAdapter extends BaseRecyclerViewAdapter<RecordEntity> {
    private Context context;
    private int timeType;

    public EnergyAdapter(Context context, List<RecordEntity> data,int timeType) {
        super(data, R.layout.item_energy);
        this.context = context;
        this.timeType = timeType;
    }

    @Override
    protected void convert(BaseViewHolder holder, RecordEntity item, int position) {
        TextView tvDate = holder.getView(R.id.tv_date);
        TextView tvData1 = holder.getView(R.id.tv_data1);
        TextView tvData2 = holder.getView(R.id.tv_data2);
        TextView tvData3 = holder.getView(R.id.tv_data3);

        //按日查询时显示时间点
        if(timeType == 3){
            tvDate.setText(item.getHour()+":00");
        }else{
            tvDate.setText(item.getData_time());
        }

        tvData1.setText(StringUtils.doulbeToStr(item.getElec()));
        tvData2.setText(StringUtils.doulbeToStr(item.getWater()));
        tvData3.setText(StringUtils.doulbeToStr(item.getHeat()));
    }
}
