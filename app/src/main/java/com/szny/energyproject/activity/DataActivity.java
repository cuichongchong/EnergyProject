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
import com.szny.energyproject.mvp.exceptions.BaseException;
import com.szny.energyproject.mvp.iviews.IDataView;
import com.szny.energyproject.mvp.persenters.DataPresenter;
import com.szny.energyproject.utils.TimeUtils;
import com.szny.energyproject.utils.ToastUtils;
import com.szny.energyproject.widget.BarChartManager;
import com.szny.energyproject.widget.PieChartManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 数据展示页面
 * */
public class DataActivity extends BaseActivity implements View.OnClickListener, IDataView {

    private PieChart mPieChart;
    private BarChart mBarChart;

    private RecyclerView rvData;
    private DataAdapter dataAdapter;
    private List<DataEntity> dataList;

    private RelativeLayout rlRoot;
    private TextView tvCurrentTime;
    private ImageView ivTime;
    private TimePickerView timePickerView;
    private String mMonth = "";//选中月份
    private String mYear = "";//选中年份

    private int roomId;

    private DataPresenter presenter;

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

        presenter = new DataPresenter();
        presenter.attachView(this);
    }

    private void initData() {
        //接收roomId
        roomId = getIntent().getIntExtra("roomId",0);
        //获取当前年份月份
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        mYear = TimeUtils.getTime(curDate,"yyyy");
        mMonth = TimeUtils.getTime(curDate,"MM");
        tvCurrentTime.setText(TimeUtils.getTime(curDate,"yyyy年MM月"));

        //获取数据
        presenter.getReport(roomId,mYear,mMonth);

        //模拟列表展示数据
        dataList= new ArrayList<>();
        dataAdapter = new DataAdapter(this,dataList);
        rvData.setLayoutManager(new LinearLayoutManager(mContext));
        rvData.setAdapter(dataAdapter);
    }

    private void initEvent() {
        ivTime.setOnClickListener(this);
    }

    //数据信息成功返回
    @Override
    public void success(List<DataEntity> dataEntities) {
        dataAdapter.clearAllData();
        if(dataEntities != null && dataEntities.size() > 0){

            //对数据按照日期进行排序
            Collections.sort(dataEntities, new Comparator<DataEntity>() {
                @Override
                public int compare(DataEntity o1, DataEntity o2) {
                    int flag = 0;
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date dt1 = format.parse(o1.getDataTime());
                        Date dt2 = format.parse(o2.getDataTime());
                        if (dt1.getTime() > dt2.getTime()) {
                            flag = 1;
                        } else if (dt1.getTime() < dt2.getTime()) {
                            flag = -1;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return flag;
                }
            });

            //设置条形图
            showBarChart(dataEntities);

            //设置饼状图
            showPieChart(dataEntities);

            //设置列表数据
            dataList.addAll(dataEntities);
            dataAdapter.notifyDataSetChanged();
        }else{
            showBarChart(new ArrayList<>());
            showPieChart(new ArrayList<>());
        }
    }

    //条形图
    public void showBarChart(List<DataEntity> dataEntities){
        //设置条形图数据
        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        for(int i = 0; i < dataEntities.size(); i++){
            DataEntity entity = dataEntities.get(i);
            yVals.add(new BarEntry(i+1, new float[]{(float) entity.getLight(), (float) entity.getSocket(), (float) entity.getAir()}));
        }
        BarChartManager barChartManager = new BarChartManager(mBarChart);
        String label = "能耗数据";
        String[] stackLabels = new String[]{"照明","插座","空调"};
        barChartManager.showBarChart(yVals,label,stackLabels);
    }

    //饼状图
    public void showPieChart(List<DataEntity> dataEntities){
        double sumLight = 0;
        double sumAir = 0;
        double sumSocket = 0;
        for(DataEntity item : dataEntities){
            sumLight = sumLight + item.getLight();
            sumAir = sumAir + item.getAir();
            sumSocket = sumSocket + item.getSocket();
        }

        //设置饼图数据
        List<PieEntry> yvals = new ArrayList<>();
        yvals.add(new PieEntry((float) sumLight,"照明"));
        yvals.add(new PieEntry((float) sumSocket,"插座"));
        yvals.add(new PieEntry((float) sumAir,"空调"));
        //设置每份的颜色
        List<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.color1));
        colors.add(getResources().getColor(R.color.color2));
        colors.add(getResources().getColor(R.color.color3));
        //展示饼图
        PieChartManager pieChartManager = new PieChartManager(mPieChart);
        pieChartManager.showRingPieChart(yvals,colors,"能源消耗");
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
            mMonth = TimeUtils.getTime(date, "MM");
            mYear = TimeUtils.getTime(date,"yyyy");
            //重新请求数据
            presenter.getReport(roomId,mYear,mMonth);
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

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}