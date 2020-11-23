package com.szny.energyproject.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collection;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected final String TAG = getClass().getSimpleName();
    protected List<T> data;
    protected int[] resIds;
    protected OnItemClickListener itemClickListener;
    protected OnItemLongClickListener itemLongClickListener;

    public BaseRecyclerViewAdapter(List<T> data, @LayoutRes int... resIds) {
        this.data = data;
        if (resIds == null || resIds.length <= 0) {
            throw new RuntimeException("至少需要一个布局资源");
        }
        this.resIds = resIds;
    }


    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(viewType, parent, false);
        BaseViewHolder viewHolder = new BaseViewHolder(parent.getContext(), view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (!(holder instanceof BaseViewHolder)) {
            throw new RuntimeException("没有使用自定义的基础BaseViewHolder");
        } else {
            final BaseViewHolder viewHolder = (BaseViewHolder) holder;
            T t = getItem(position);
            convert(viewHolder, t, position);

            viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(viewHolder.getConvertView(), position);
                    }
                }
            });

            viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (itemLongClickListener != null) {
                        itemLongClickListener.onItemLongClick(viewHolder.getConvertView(), position);
                    }
                    return true;
                }
            });
        }
    }

    protected T getItem(int position){
        T t = data.get(position);
        return t;
    }

    protected abstract void convert(BaseViewHolder holder, T item, int position);

    @Override
    public int getItemCount() {
        return (data == null || data.size() == 0) ? 0 : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (resIds.length > 1) {
            return getItemViewType(position, resIds);
        } else {
            return resIds[0];
        }
    }

    public int getItemViewType(int position, int[] resIds) {
        return -1;
    }

    public void clearAllData() {
        if (data != null) {
            data.clear();
            notifyDataSetChanged();
        }
    }

    public void addListData(Collection<T> adds) {
        if (adds != null) {
            data.addAll(adds);
            notifyDataSetChanged();
        }
    }

    public void addListData(int position, Collection<T> adds) {
        if (adds != null) {
            data.addAll(position, adds);
            notifyItemChanged(position);
        }
    }

    public void addData(T t) {
        if (t != null) {
            data.add(t);
            notifyDataSetChanged();
        }
    }

    public void removeData(T t) {
        if (t != null) {
            data.remove(t);
            notifyDataSetChanged();
        }
    }

    public void addData(int position, T t) {
        if (t != null) {
            data.add(position, t);
            notifyItemChanged(position);
        }
    }

    public List<T> getData(){
        return data;
    }
}
