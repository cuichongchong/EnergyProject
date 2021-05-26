package com.szny.energyproject.adapter;

import android.content.Context;
import android.widget.TextView;
import com.szny.energyproject.R;
import com.szny.energyproject.base.BaseRecyclerViewAdapter;
import com.szny.energyproject.base.BaseViewHolder;
import com.szny.energyproject.entity.DataEntity;
import java.util.List;

/**
 * 数据列表展示适配器
 * */
public class DataAdapter extends BaseRecyclerViewAdapter<DataEntity> {

    private Context context;

    public DataAdapter(Context context,List<DataEntity> data) {
        super(data, R.layout.item_data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, DataEntity item, int position) {
        TextView tvData = holder.getView(R.id.tv_data);
        TextView tvLight = holder.getView(R.id.tv_light);
        TextView tvSocket = holder.getView(R.id.tv_socket);
        TextView tvAir = holder.getView(R.id.tv_air);
        TextView tvSum = holder.getView(R.id.tv_sum);

        tvData.setText(item.getDataTime());
        tvLight.setText(String.valueOf(item.getLight()));
        tvSocket.setText(String.valueOf(item.getSocket()));
        tvAir.setText(String.valueOf(item.getAir()));
        tvSum.setText(String.valueOf(item.getLight()+item.getSocket()+item.getAir()));
    }
}
