package com.szny.energyproject.activity;

import androidx.appcompat.widget.Toolbar;
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
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.gyf.barlibrary.ImmersionBar;
import com.szny.energyproject.R;
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
import com.szny.energyproject.widget.PieChartManager;
import com.szny.energyproject.widget.SearchableSpinner;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 能耗分析页面
 * */
public class AnalysisActivity extends BaseActivity implements View.OnClickListener, IDataView,DatePickerDialog.OnDateSetListener {
    private PieChart mPieChart;
    private RelativeLayout rlRoot;
    private TextView tvCurrentTime;
    private TextView tvRangeTime;
    private SearchableSpinner spinner;
    private SearchableSpinner typeSpinner;
    private Spinner timeSpinner;
    private TimePickerView timePickerView;

    private TextView tvData1;
    private TextView tvData1Val;
    private TextView tvData2;
    private TextView tvData2Val;
    private TextView tvData3;
    private TextView tvData3Val;
    private View view;

    private int userId;//用户id
    private int groupId;//类别id
    private int memberId;//成员id'
    private int timeType = 2;//选择的时间类型(1年2月3日4时间段)
    private String classType;//选择的分类类别
    private String selectTime;//存储选择的时间

    //日期选择数据集合
    public ArrayAdapter<String> adapterTime;
    //类别列表选择器
    private List<String> groupList = new ArrayList<>();
    //类别下成员选择器
    private List<String> memberList = new ArrayList<>();
    //数据统计返回的数据
    private List<RecordEntity> data;

    private DataPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

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
        toolbar_title.setText("能耗分析");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewGroup.LayoutParams tbLinearParams = toolbar.getLayoutParams(); //取控件textView当前的布局参数
            toolbar.setPadding(0, DensityUtil.dip2px(this, 20f),0, 0);//4个参数按顺序分别是左上右下
            tbLinearParams.height = DensityUtil.dip2px(this, 69f);// 控件的高
            toolbar.setLayoutParams(tbLinearParams);//使设置好的布局参数应用到控件
        }

        //饼图
        mPieChart = findViewById(R.id.mPieChart);
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
        //类别选择器
        typeSpinner = findViewById(R.id.type_spinner);
        tvData1 = findViewById(R.id.tv_data1);
        tvData1Val = findViewById(R.id.tv_data1_val);
        tvData2 = findViewById(R.id.tv_data2);
        tvData2Val = findViewById(R.id.tv_data2_val);
        tvData3 = findViewById(R.id.tv_data3);
        tvData3Val = findViewById(R.id.tv_data3_val);
        view = findViewById(R.id.view);

        presenter = new DataPresenter();
        presenter.attachView(this);
    }

    private void initData() {
        //接收userId
        userId = getIntent().getIntExtra("userId", 0);

        //获取类别分组列表
        presenter.getGroup();

        //获取当前年份月份
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间

        //填充日期选择类别
        adapterTime = new ArrayAdapter<>(this,R.layout.item_spinner);
        List<String> dataType = new ArrayList<>();
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
                if("月".equals(type)){
                    timeType = 2;
                    tvCurrentTime.setText(TimeUtils.getTime(curDate,"yyyy-MM"));
                    //切换了查询时间类别，默认查当前的
                    if(memberId != 0){
                        presenter.getRecord(memberId,timeType,tvCurrentTime.getText().toString());
                    }
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
        switch (view.getId()){
            case R.id.tv_current_time:
                if(timeType == 2){
                    initTimePicker(true,true,false);
                    timePickerView.show();
                }else if(timeType == 3){
                    initTimePicker(true,true,true);
                    timePickerView.show();
                }else if(timeType == 4){
                    showDatePickerDialog();
                }
                break;
            case R.id.tv_range_time:
                showDatePickerDialog();
                break;
        }
    }

    @Override
    public void getGroup(List<RoomEntity> data) {
        if(data != null && data.size() > 0) {
            //获取到类别列表后，默认请求第一个类别下的成员列表
            groupId = data.get(0).getId();
            presenter.getMember(userId, groupId);

            //设置类别选择数据
            groupList.clear();
            for (RoomEntity datum : data) {
                groupList.add(datum.getName());
            }

            //类型选择器
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_spinner);
            adapter.addAll(groupList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            typeSpinner.setAdapter(adapter);
            typeSpinner.setTitle("选择类型");
            typeSpinner.setPositiveButton("关闭");
            typeSpinner.setOnSearchableItemClickListener(new SearchableSpinner.SearchableItem() {
                @Override
                public void onSearchableItemClicked(Object item) {
                    Log.e("doing","选择了 "+ item.toString());
                    classType = item.toString();
                    for (RoomEntity entity : data) {
                        if(classType.equals(entity.getName())){
                            //获取当前选择的groupId
                            groupId = entity.getId();
                            //重新请求所选类别的成员列表
                            presenter.getMember(userId,groupId);
                        }
                    }
                }
            });
        }
    }

    //类别下成员信息返回
    @Override
    public void getMember(List<RoomEntity> data) {
        if(data != null && data.size() > 0){
            //获取到成员列表，默认请求第一个成员的数据
            memberId = data.get(0).getId();
            presenter.getRecord(memberId,timeType,selectTime);

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

    //能耗分析数据返回
    @Override
    public void getRecord(List<RecordEntity> data) {
        if(data != null){
            this.data = data;
            double workTimeSumEle = 0.00;
            double restTimeSumEle = 0.00;
            double workDaySumEle = 0.00;
            double restDaySumEle = 0.00;
            try {
                //设置工作时间
                Date startTime1 = new SimpleDateFormat("HH").parse("8");
                Date endTime1 = new SimpleDateFormat("HH").parse("12");
                Date startTime2 = new SimpleDateFormat("HH").parse("15");
                Date endTime2 = new SimpleDateFormat("HH").parse("18");
                for (RecordEntity item : data) {
                    //如果是按日查的，就是计算工作时间和休息时间
                    if(timeType == 3){
                        Date nowTime = new SimpleDateFormat("HH").parse(item.getHour());
                        if(new DateUtils().isEffectiveDate(nowTime,startTime1,endTime1)||
                                new DateUtils().isEffectiveDate(nowTime,startTime2,endTime2)){
                            workTimeSumEle = addDouble(workTimeSumEle,item.getElec());
                        }else{
                            restTimeSumEle = addDouble(restTimeSumEle,item.getElec());
                        }
                    }else{
                        //如果是按月/时间段查，就是计算工作日和节假日
                        if(new DateUtils().isWeekend(item.getData_time())){
                            restDaySumEle = addDouble(restDaySumEle,item.getElec());
                        }else{
                            workDaySumEle = addDouble(workDaySumEle,item.getElec());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            showPieChart(workTimeSumEle,restTimeSumEle,workDaySumEle,restDaySumEle);

            //设置能效分析数据显示
            if(timeType == 3){
                tvData1.setText("工作时间能耗");
                tvData1Val.setText(StringUtils.doulbeToStr(workTimeSumEle)+"kWh");
                tvData2.setText("休息时间能耗");
                tvData2Val.setText(StringUtils.doulbeToStr(restTimeSumEle)+"kWh");
                tvData3.setVisibility(View.VISIBLE);
                tvData3Val.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
                //计算效能指数
                BigDecimal b1 = new BigDecimal(Double.toString(workTimeSumEle));
                BigDecimal b2 = new BigDecimal(Double.toString(addDouble(workTimeSumEle,restTimeSumEle)));
                if(b2.doubleValue() != 0){
                    BigDecimal result = b1.divide(b2,2,BigDecimal.ROUND_HALF_UP);
                    tvData3Val.setText(String.valueOf(result.doubleValue()));
                }else{
                    tvData3Val.setText("0.00");
                }
            }else{
                tvData1.setText("工作日能耗");
                tvData1Val.setText(StringUtils.doulbeToStr(workDaySumEle)+"kWh");
                tvData2.setText("休息日能耗");
                tvData2Val.setText(StringUtils.doulbeToStr(restDaySumEle)+"kWh");
                tvData3.setVisibility(View.GONE);
                tvData3Val.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
            }
        }
    }

    public double addDouble(double data1,double data2){
        BigDecimal b1 = new BigDecimal(Double.toString(data1));
        BigDecimal b2 = new BigDecimal(Double.toString(data2));
        return b1.add(b2).doubleValue();
    }

    @Override
    public void success(List<CarbonEntity> dataEntities) {
    }

    //饼状图
    public void showPieChart(double workTimeSumEle,double restTimeSumEle,double workDaySumEle,double restDaySumEle){
        //设置饼图数据
        List<PieEntry> yvals = new ArrayList<>();
        if(timeType == 3){
            yvals.add(new PieEntry((float) workTimeSumEle,"工作时间"));
            yvals.add(new PieEntry((float) restTimeSumEle,"休息时间"));
        }else{
            yvals.add(new PieEntry((float) workDaySumEle,"工作日"));
            yvals.add(new PieEntry((float) restDaySumEle,"休息日"));
        }

        //设置每份的颜色
        List<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.color1));
        colors.add(getResources().getColor(R.color.color2));
        //展示饼图
        PieChartManager pieChartManager = new PieChartManager(mPieChart);
        pieChartManager.showRingPieChart(yvals,colors,"",false);
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
            if(timeType == 2){
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