package com.szny.energyproject.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.szny.energyproject.R;
import com.szny.energyproject.adapter.DataAdapter;
import com.szny.energyproject.base.BaseActivity;
import com.szny.energyproject.entity.DataEntity;
import com.szny.energyproject.utils.TimeUtils;
import com.szny.energyproject.widget.BarChartManager;
import com.szny.energyproject.widget.PieChartManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 数据展示页面
 * */
public class DataActivity extends BaseActivity implements View.OnClickListener {

    private PieChart mPieChart;
    private BarChart mBarChart;

    private RecyclerView rvData;
    private DataAdapter dataAdapter;

    private RelativeLayout rlRoot;
    private TextView tvCurrentTime;
    private ImageView ivTime;
    private TimePickerView timePickerView;
    private String mMonth = "";//当前展示月份

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(view -> finish());
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("数据展示");

        //堆叠条形图
        mBarChart = (BarChart) findViewById(R.id.mBarChart);
        //饼图
        mPieChart = findViewById(R.id.mPieChart);
        //数据展示列表
        rvData = findViewById(R.id.rv_data);
        //根布局
        rlRoot = findViewById(R.id.rl_root);
        //选择日期
        ivTime = findViewById(R.id.iv_choose_time);
        //当前日期
        tvCurrentTime = findViewById(R.id.tv_current_time);

        //初始化日期选择器
        initTimePicker();
    }

    private void initData() {
        //模拟条形图数据
        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        for (int i = 0; i < 15; i++) {
            float mult = (10 + 1);
            float val1 = (float) (Math.random() * mult) + mult / 3;
            float val2 = (float) (Math.random() * mult) + mult / 3;
            float val3 = (float) (Math.random() * mult) + mult / 3;
            /*float val1 = 0f;
            float val2 = 0f;
            float val3 = 0f;*/
            float val4 = (float) (Math.random() * mult) + mult / 3;
            yVals.add(new BarEntry(i+1, new float[]{val1, val2, val3, val4}));
        }
        //展示条形图
        BarChartManager barChartManager = new BarChartManager(mBarChart);
        String label = "能耗数据";
        String[] stackLabels = new String[]{"照明","插座","空调","总量"};
        barChartManager.showBarChart(yVals,label,stackLabels);

        //模拟饼图数据
        List<PieEntry> yvals = new ArrayList<>();
        yvals.add(new PieEntry(10f,"照明"));
        yvals.add(new PieEntry(20f,"插座"));
        yvals.add(new PieEntry(15f,"空调"));
        yvals.add(new PieEntry(50f,"总量"));
        //设置每份的颜色
        List<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.color1));
        colors.add(getResources().getColor(R.color.color2));
        colors.add(getResources().getColor(R.color.color3));
        colors.add(getResources().getColor(R.color.color4));
        //展示饼图
        PieChartManager pieChartManager = new PieChartManager(mPieChart);
        pieChartManager.showRingPieChart(yvals,colors,"能源消耗");

        //模拟列表展示数据
        List<DataEntity> list= new ArrayList<>();
        list.add(new DataEntity("2020-11-01",10,20,30,60));
        list.add(new DataEntity("2020-11-01",10,20,30,60));
        list.add(new DataEntity("2020-11-01",10,20,30,60));
        list.add(new DataEntity("2020-11-01",10,20,30,60));
        list.add(new DataEntity("2020-11-01",10,20,30,60));
        list.add(new DataEntity("2020-11-01",10,20,30,60));
        list.add(new DataEntity("2020-11-01",10,20,30,60));
        list.add(new DataEntity("2020-11-01",10,20,30,60));
        list.add(new DataEntity("2020-11-01",10,20,30,60));
        list.add(new DataEntity("2020-11-01",10,20,30,60));

        dataAdapter = new DataAdapter(this,list);
        rvData.setLayoutManager(new LinearLayoutManager(mContext));
        rvData.setAdapter(dataAdapter);
    }

    private void initEvent() {
        ivTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_choose_time:
                timePickerView.show();
                break;
        }
    }

    //选择年月日
    private void initTimePicker() {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        SimpleDateFormat formatterYear = new SimpleDateFormat("yyyy ");
        String yearStr = formatterYear.format(curDate);
        int yearInt = (int) Double.parseDouble(yearStr);

        SimpleDateFormat formatterMouth = new SimpleDateFormat("MM ");
        String mouthStr = formatterMouth.format(curDate);
        int mouthInt = (int) Double.parseDouble(mouthStr);

        SimpleDateFormat formatterDay = new SimpleDateFormat("dd ");
        String dayStr = formatterDay.format(curDate);
        int dayInt = (int) Double.parseDouble(dayStr);

        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(yearInt, mouthInt - 1, dayInt);

        timePickerView = new TimePickerBuilder(mContext, (OnTimeSelectListener) (date, v) -> {
            tvCurrentTime.setText(TimeUtils.getTime(date, "yyyy年MM月"));
            mMonth = TimeUtils.getTime(date, "yyyy-MM");
        })
                .setType(new boolean[]{true, true, false, false, false, false}) //年月日时分秒 的显示与否，不设置则默认全部显示
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
}