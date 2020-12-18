package com.szny.energyproject.widget;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.szny.energyproject.R;


public class Loading extends Dialog {

    public Loading(@NonNull Context context) {
        super(context);
        init();
    }

    public Loading(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    void init() {
        setContentView(R.layout.view_loading);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        ImageView imgLoading = findViewById(R.id.img_loading);
        RequestOptions requestOptions = new RequestOptions()
               .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(getContext()).load(R.mipmap.gif_page_load).apply(requestOptions).into(imgLoading);
    }
}
