package com.szny.energyproject.activity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gyf.barlibrary.ImmersionBar;
import com.szny.energyproject.R;
import com.szny.energyproject.adapter.CarbonAdapter;
import com.szny.energyproject.base.BaseActivity;
import com.szny.energyproject.entity.CarbonEntity;
import com.szny.energyproject.mvp.exceptions.BaseException;
import com.szny.energyproject.mvp.iviews.ICarbonView;
import com.szny.energyproject.mvp.persenters.CarbonPresenter;
import com.szny.energyproject.utils.DensityUtil;
import com.szny.energyproject.utils.TimeUtils;
import com.szny.energyproject.utils.ToastUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 碳排放量页面
 * */
public class CarbonEmissionActivity extends BaseActivity implements View.OnClickListener, ICarbonView {
    private RelativeLayout rlRoot;
    private TextView tvCurrentTime;
    private RecyclerView rvData;
    private TimePickerView timePickerView;

    private String mYear = "";//选中年份
    private List<CarbonEntity> dataList;
    private CarbonAdapter adapter;
    private CarbonPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carbon_emission);

        ImmersionBar.with(this).init();

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        toolbar.setNavigationOnClickListener(view -> finish());
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("碳排放量");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewGroup.LayoutParams tbLinearParams = toolbar.getLayoutParams(); //取控件textView当前的布局参数
            toolbar.setPadding(0, DensityUtil.dip2px(this, 20f),0, 0);//4个参数按顺序分别是左上右下
            tbLinearParams.height = DensityUtil.dip2px(this, 69f);// 控件的高
            toolbar.setLayoutParams(tbLinearParams);//使设置好的布局参数应用到控件
        }

        //根布局
        rlRoot = findViewById(R.id.rl_root);
        //当前日期
        tvCurrentTime = findViewById(R.id.tv_current_time);
        rvData = findViewById(R.id.rv_data);

        //初始化日期选择器
        initTimePicker();

        presenter = new CarbonPresenter();
        presenter.attachView(this);
    }

    private void initData() {
        //获取当前年份月份
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        mYear = TimeUtils.getTime(curDate,"yyyy");
        tvCurrentTime.setText(TimeUtils.getTime(curDate,"yyyy年"));

        //碳排放量展示数据
        dataList = new ArrayList<>();
        adapter= new CarbonAdapter(this,dataList);
        rvData.setLayoutManager(new LinearLayoutManager(mContext));
        rvData.setAdapter(adapter);

        //请求当前年份
        presenter.getCarbon(mYear);
    }

    private void initEvent() {
        tvCurrentTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_current_time:
                timePickerView.show();
                break;
        }
    }

    @Override
    public void success(List<CarbonEntity> carbonEntities) {
        adapter.clearAllData();
        if(carbonEntities != null && carbonEntities.size() > 0){
            //设置展示数据
            dataList.addAll(carbonEntities);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void failed(Throwable e) {
        //token失效，返回登录页面
        if(e instanceof BaseException && ((BaseException) e).getErrCode() == 113){
            ToastUtils.showShort(mContext,"验证失效,请重新登录");
        }else{
            ToastUtils.showShort(mContext,"请求失败");
        }
    }

    //选择年月日
    private void initTimePicker() {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        SimpleDateFormat formatterYear = new SimpleDateFormat("yyyy");
        String yearStr = formatterYear.format(curDate);
        int yearInt = Integer.parseInt(yearStr);
        //int yearInt = (int) Double.parseDouble(yearStr);

        SimpleDateFormat formatterMouth = new SimpleDateFormat("MM");
        String mouthStr = formatterMouth.format(curDate);
        int mouthInt = Integer.parseInt(mouthStr);
        //int mouthInt = (int) Double.parseDouble(mouthStr);

        SimpleDateFormat formatterDay = new SimpleDateFormat("dd");
        String dayStr = formatterDay.format(curDate);
        int dayInt = Integer.parseInt(dayStr);
        //int dayInt = (int) Double.parseDouble(dayStr);

        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(yearInt, mouthInt - 1, dayInt);

        timePickerView = new TimePickerBuilder(mContext, (OnTimeSelectListener) (date, v) -> {
            tvCurrentTime.setText(TimeUtils.getTime(date, "yyyy年"));
            mYear = TimeUtils.getTime(date,"yyyy");
            //重新请求数据
            presenter.getCarbon(mYear);
        })
                .setType(new boolean[]{true, false, false, false, false, false}) //年月日时分秒 的显示与否，不设置则默认全部显示
                .setLabel("年", "月", "", "", "", "")//默认设置为年月日时分秒
                .isCenterLabel(false)
                .setDate(selectedDate)
                .setDecorView(rlRoot)
                .setLineSpacingMultiplier(1.8f)
                .setTextXOffset(0, 0, 0, 0, 0, 0)//设置X轴倾斜角度[ -90 , 90°]
                .setRangDate(startDate, endDate)
                .setSubmitText("完成")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("")//标题
                .setSubCalSize(16)//确定和取消文字大小
                .setTitleSize(16)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(getResources().getColor(R.color.color_1790FF))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.color_111111))//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.colorWhite))//标题背景颜色 Night mode
                .setBgColor(getResources().getColor(R.color.colorWhite))//滚轮背景颜色 Night mode
                .setContentTextSize(16)//滚轮文字大小
                .setLayoutRes(R.layout.item_picker_time, (CustomListener) v -> {
                    TextView tvFinish = v.findViewById(R.id.tv_finish);
                    tvFinish.setOnClickListener(view -> {
                        timePickerView.returnData();
                        timePickerView.dismiss();
                    });
                })
                .build();
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}