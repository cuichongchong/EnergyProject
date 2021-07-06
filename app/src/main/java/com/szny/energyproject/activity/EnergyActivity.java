package com.szny.energyproject.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.gyf.barlibrary.ImmersionBar;
import com.szny.energyproject.R;
import com.szny.energyproject.adapter.EnergyAdapter;
import com.szny.energyproject.base.BaseActivity;
import com.szny.energyproject.entity.CarbonEntity;
import com.szny.energyproject.entity.RecordEntity;
import com.szny.energyproject.entity.RoomEntity;
import com.szny.energyproject.mvp.exceptions.BaseException;
import com.szny.energyproject.mvp.iviews.IDataView;
import com.szny.energyproject.mvp.persenters.DataPresenter;
import com.szny.energyproject.utils.DateUtils;
import com.szny.energyproject.utils.DensityUtil;
import com.szny.energyproject.utils.StringUtils;
import com.szny.energyproject.utils.TimeUtils;
import com.szny.energyproject.utils.ToastUtils;
import com.szny.energyproject.widget.BarChartManager;
import com.szny.energyproject.widget.SearchableSpinner;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 重点用能页面
 * */
public class EnergyActivity extends BaseActivity implements View.OnClickListener, IDataView, DatePickerDialog.OnDateSetListener {
    private RelativeLayout rlRoot;
    private TextView tvCurrentTime;
    private TextView tvRangeTime;
    private SearchableSpinner spinner;
    private Spinner timeSpinner;
    private TimePickerView timePickerView;
    private RecyclerView rvEnergy;
    private BarChart mBarChart;

    private EnergyAdapter adapter;
    private List<RecordEntity> dataList;

    //能耗总计字段
    private TextView tvSum1Val;
    private TextView tvSum2Val;
    private TextView tvSum3Val;

    private int userId;//用户id
    private int memberId;//成员id
    private int timeType = 1;//选择的时间类型(1年2月3日4时间段)
    private String selectTime;//存储选择的时间

    //日期选择数据集合
    public ArrayAdapter<String> adapterTime;
    //类别下成员选择器
    private List<String> memberList = new ArrayList<>();

    private DataPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_energy);

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
        toolbar_title.setText("重点用能");
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
        //时间段
        tvRangeTime = findViewById(R.id.tv_range_time);
        //成员选择器
        spinner = findViewById(R.id.search_spinner);
        //时间类型选择器
        timeSpinner = findViewById(R.id.time_spinner);
        //能耗列表
        rvEnergy = findViewById(R.id.rv_energy);
        //能耗统计控件
        tvSum1Val = findViewById(R.id.tv_sum1_val);
        tvSum2Val = findViewById(R.id.tv_sum2_val);
        tvSum3Val = findViewById(R.id.tv_sum3_val);
        //堆叠条形图
        mBarChart = findViewById(R.id.mBarChart);

        presenter = new DataPresenter();
        presenter.attachView(this);
    }

    private void initData() {
        //接收userId
        userId = getIntent().getIntExtra("userId", 0);

        //获取重点用能下成员列表
        presenter.getMember(userId,33);

        //获取当前年份月份
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间

        //填充日期选择类别
        adapterTime = new ArrayAdapter<>(this,R.layout.item_spinner);
        List<String> dataType = new ArrayList<>();
        dataType.add("年");
        dataType.add("月");
        dataType.add("日");
        dataType.add("区间");
        adapterTime.addAll(dataType);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(adapterTime);
        //日期选择监听
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvCurrentTime.setVisibility(View.VISIBLE);
                tvRangeTime.setVisibility(View.GONE);
                String type = dataType.get(position);
                if("年".equals(type)){
                    timeType = 1;
                    tvCurrentTime.setText(TimeUtils.getTime(curDate,"yyyy"));
                    //切换了查询时间类别，默认查当前的
                    if(memberId != 0){
                        presenter.getRecord(memberId,timeType,tvCurrentTime.getText().toString());
                    }
                }else if("月".equals(type)){
                    timeType = 2;
                    tvCurrentTime.setText(TimeUtils.getTime(curDate,"yyyy-MM"));
                    //切换了查询时间类别，默认查当前的
                    presenter.getRecord(memberId,timeType,tvCurrentTime.getText().toString());
                }else if("日".equals(type)){
                    timeType = 3;
                    tvCurrentTime.setText(TimeUtils.getTime(curDate,"yyyy-MM-dd"));
                    //切换了查询时间类别，默认查当前的
                    presenter.getRecord(memberId,timeType,tvCurrentTime.getText().toString());
                }else if("区间".equals(type)){
                    timeType = 4;
                    tvCurrentTime.setText("");
                }
                selectTime = tvCurrentTime.getText().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initEvent() {
        tvCurrentTime.setOnClickListener(this);
        tvRangeTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_current_time:
                if (timeType == 1) {
                    initTimePicker(true, false, false);
                    timePickerView.show();
                } else if (timeType == 2) {
                    initTimePicker(true, true, false);
                    timePickerView.show();
                } else if (timeType == 3) {
                    initTimePicker(true, true, true);
                    timePickerView.show();
                } else if (timeType == 4) {
                    showDatePickerDialog();
                }
                break;
            case R.id.tv_range_time:
                showDatePickerDialog();
                break;
        }
    }

    //重点用能下成员列表返回
    @Override
    public void getMember(List<RoomEntity> data) {
        if(data != null && data.size() > 0) {
            //获取到成员列表，默认请求第一个成员的数据
            memberId = data.get(0).getId();
            presenter.getRecord(memberId, timeType, selectTime);

            //设置成员列表选择数据
            memberList.clear();
            for (RoomEntity item : data) {
                memberList.add(item.getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_spinner);
            adapter.addAll(memberList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setTitle("选择");
            spinner.setPositiveButton("关闭");
            //类型下成员的选择器
            spinner.setOnSearchableItemClickListener(new SearchableSpinner.SearchableItem() {
                @Override
                public void onSearchableItemClicked(Object item) {
                    Log.e("doing","选择了 "+ item.toString());
                    for (RoomEntity entity : data) {
                        if(item.equals(entity.getName())){
                            //获取当前选择的memberId
                            memberId = entity.getId();
                            //重新请求能耗统计数据
                            presenter.getRecord(memberId,timeType,selectTime);
                        }
                    }
                }
            });
        }
    }

    //能耗数据返回
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void getRecord(List<RecordEntity> data) {
        dataList = new ArrayList<>();
        if(data != null){
            //设置列表数据
            dataList.addAll(data);
            adapter = new EnergyAdapter(this,dataList,timeType);
            rvEnergy.setLayoutManager(new LinearLayoutManager(mContext));
            rvEnergy.setAdapter(adapter);

            //计算电、水、热总能耗
            double sumElec = data.stream().mapToDouble(RecordEntity::getElec).sum();
            double sumWater = data.stream().mapToDouble(RecordEntity::getWater).sum();
            double sumHeat = data.stream().mapToDouble(RecordEntity::getHeat).sum();
            tvSum1Val.setText(StringUtils.doulbeToStr(sumElec)+"kWh");
            tvSum2Val.setText(StringUtils.doulbeToStr(sumWater)+"m³");
            tvSum3Val.setText(StringUtils.doulbeToStr(sumHeat)+"MJ");

            //设置条形图
            showBarChart(data);
        }
    }

    //条形图
    public void showBarChart(List<RecordEntity> recordEntities){
        //封装日期
        List<String> dataList = new ArrayList<>();
        //设置坐标轴数据
        List<BarEntry> vals = new ArrayList<BarEntry>();
        for(int i = 0; i < recordEntities.size(); i++){
            //根据类型设置x轴数据(1年2月3日4时间段)
            switch (timeType){
                case 1:
                    dataList.add(recordEntities.get(i).getMonth()+"月");
                    break;
                case 2:
                    dataList.add(recordEntities.get(i).getDay()+"日");
                    break;
                case 3:
                    dataList.add(recordEntities.get(i).getHour()+"点");
                    break;
                case 4:
                    dataList.add(recordEntities.get(i).getMonth()+"-"+recordEntities.get(i).getDay());
                    break;
            }
            RecordEntity entity = recordEntities.get(i);
            //添加x/y轴数据
            vals.add(new BarEntry((float) i, new float[]{(float) entity.getElec(), (float) entity.getWater(), (float) entity.getHeat()}));
        }

        BarChartManager barChartManager = new BarChartManager(mBarChart);
        String label = "";
        //根据类别类型，设置图例显示
        String[] stackLabels = new String[]{"电","水","热"};
        barChartManager.showBarChart(vals,label,stackLabels,dataList);
    }

    @Override
    public void success(List<CarbonEntity> carbonEntities) {
    }

    @Override
    public void getGroup(List<RoomEntity> data) {
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

    //显示时间段选择框
    public void showDatePickerDialog(){
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show(getFragmentManager(),"Datepickerdialog");
    }

    //时间段选择框返回
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        //开始时间的时间戳
        String timeStart = new DateUtils().getTimeStart("" + year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
        //结束时间的时间戳
        String timeEnd = new DateUtils().getTimeEnd("" + yearEnd + "年" + (monthOfYearEnd + 1) + "月" + dayOfMonthEnd + "日");
        //如果需要一些判断操作，可以try-catch 一下 或 if-else 判断
        try {
            if (Long.parseLong(timeStart) > Long.parseLong(timeEnd)) {
                ToastUtils.showShort( this,"结束时间不能小于开始时间");
            }
        } catch (Exception e) {
            ToastUtils.showShort(this, "选择时间的有误");
        }
        tvCurrentTime.setVisibility(View.GONE);
        tvRangeTime.setVisibility(View.VISIBLE);
        tvRangeTime.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth +"至"+ yearEnd + "-" + (monthOfYearEnd + 1) + "-" + dayOfMonthEnd);

        //重新请求统计数据
        selectTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth +"T"+ yearEnd + "-" + (monthOfYearEnd + 1) + "-" + dayOfMonthEnd;
        presenter.getRecord(memberId,timeType,selectTime);
    }

    //选择年月日
    private void initTimePicker(boolean year,boolean month,boolean day) {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        SimpleDateFormat formatterYear = new SimpleDateFormat("yyyy");
        String yearStr = formatterYear.format(curDate);
        int yearInt = Integer.parseInt(yearStr);

        SimpleDateFormat formatterMouth = new SimpleDateFormat("MM");
        String mouthStr = formatterMouth.format(curDate);
        int mouthInt = Integer.parseInt(mouthStr);

        SimpleDateFormat formatterDay = new SimpleDateFormat("dd");
        String dayStr = formatterDay.format(curDate);
        int dayInt = Integer.parseInt(dayStr);

        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(yearInt, mouthInt - 1, dayInt);

        timePickerView = new TimePickerBuilder(mContext, (OnTimeSelectListener) (date, v) -> {
            if(timeType == 1){
                tvCurrentTime.setText(TimeUtils.getTime(date, "yyyy"));
            }else if(timeType == 2){
                tvCurrentTime.setText(TimeUtils.getTime(date, "yyyy-MM"));
            }else if(timeType == 3){
                tvCurrentTime.setText(TimeUtils.getTime(date, "yyyy-MM-dd"));
            }
            //重新请求能耗统计数据
            selectTime = tvCurrentTime.getText().toString();
            presenter.getRecord(memberId,timeType,selectTime);
        })
                .setType(new boolean[]{year, month, day, false, false, false}) //年月日时分秒 的显示与否，不设置则默认全部显示
                .setLabel("", "", "", "", "", "")//默认设置为年月日时分秒
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
        ImmersionBar.with(this).destroy();
    }
}