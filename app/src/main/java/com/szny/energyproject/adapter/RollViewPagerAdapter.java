package com.szny.energyproject.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import java.util.List;

/**
 * 首页轮播图的适配器
 * */
public class RollViewPagerAdapter extends LoopPagerAdapter {
    private List<Integer> mlist;
    private Context context;
    public RollViewPagerAdapter(RollPagerView viewPager , Context context, List<Integer> mlist) {
        super(viewPager);
        this.mlist = mlist;
        this.context = context;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());

        RequestOptions requestOptions = new RequestOptions()
                //.placeholder(R.mipmap.default_screen)
                .centerCrop()
                .encodeQuality(90);
        Glide.with(context).asDrawable().load(mlist.get(position)).apply(requestOptions).into(imageView);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return imageView;
    }

    @Override
    public int getRealCount() {
        return mlist.size();
    }
}
