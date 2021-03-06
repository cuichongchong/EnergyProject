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
 * 数据列表展示适配器
 * */
public class DataAdapter extends BaseRecyclerViewAdapter<RecordEntity> {
    private Context context;
    private String classType;
    private int timeType;

    public DataAdapter(Context context,List<RecordEntity> data,String classType,int timeType) {
        super(data, R.layout.item_data);
        this.context = context;
        this.classType = classType;
        this.timeType = timeType;
    }

    @Override
    protected void convert(BaseViewHolder holder, RecordEntity item, int position) {
        TextView tvDate = holder.getView(R.id.tv_date);
        TextView tvData1 = holder.getView(R.id.tv_data1);
        TextView tvData2 = holder.getView(R.id.tv_data2);
        TextView tvData3 = holder.getView(R.id.tv_data3);
        //TextView tvSum = holder.getView(R.id.tv_sum);

        //按日查询时显示时间点
        if(timeType == 3){
            tvDate.setText(item.getHour()+":00");
        }else{
            tvDate.setText(item.getData_time());
        }

        if("房间".equals(classType) || "单位".equals(classType)){
            tvData1.setText(StringUtils.doulbeToStr(item.getLight()));
            tvData2.setText(StringUtils.doulbeToStr(item.getSocket()));
            tvData3.setText(StringUtils.doulbeToStr(item.getAir()));
        }else{
            tvData1.setText(StringUtils.doulbeToStr(item.getElec()));
            tvData2.setText(StringUtils.doulbeToStr(item.getWater()));
            tvData3.setText(StringUtils.doulbeToStr(item.getHeat()));
        }
        //tvSum.setText(String.valueOf(item.getLight()+item.getSocket()+item.getAir()));
    }
}
