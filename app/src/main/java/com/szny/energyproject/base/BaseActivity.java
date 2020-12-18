package com.szny.energyproject.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.szny.energyproject.R;
import com.szny.energyproject.widget.Loading;

public abstract class BaseActivity extends AppCompatActivity {
    protected final String TAG = getClass().getSimpleName();
    public Context mContext;
    public LayoutInflater layoutInflater;
    private Loading loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorWhite));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void showLoading(){
        if(loading == null) {
            loading = new Loading(mContext, R.style.MyDialog);
        }
        if(!loading.isShowing()){
            loading.show();
        }
    }

    public void hideLoading(){
        if(loading != null && loading.isShowing()){
            loading.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
