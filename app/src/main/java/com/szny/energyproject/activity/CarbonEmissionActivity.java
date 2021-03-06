package com.szny.energyproject.activity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.gyf.barlibrary.ImmersionBar;
import com.szny.energyproject.R;
import com.szny.energyproject.adapter.CarbonAdapter;
import com.szny.energyproject.adapter.CarbonTreeAdapter;
import com.szny.energyproject.base.BaseActivity;
import com.szny.energyproject.entity.CarbonEntity;
import com.szny.energyproject.entity.RecordEntity;
import com.szny.energyproject.entity.RoomEntity;
import com.szny.energyproject.mvp.exceptions.BaseException;
import com.szny.energyproject.mvp.iviews.IDataView;
import com.szny.energyproject.mvp.persenters.DataPresenter;
import com.szny.energyproject.utils.DateUtils;
import com.szny.energyproject.utils.DensityUtil;
import com.szny.energyproject.utils.TimeUtils;
import com.szny.energyproject.utils.ToastUtils;
import com.szny.energyproject.widget.SearchableSpinner;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 碳中和页面
 * */
public class CarbonEmissionActivity extends BaseActivity implements View.OnClickListener, IDataView,DatePickerDialog.OnDateSetListener {
    private RelativeLayout rlRoot;
    private TextView tvCurrentTime;
    private TextView tvRangeTime;
    private RecyclerView rvData;
    private RecyclerView rvCo2;
    private TimePickerView timePickerView;
    private SearchableSpinner typeSpinner;
    private Spinner timeSpinner;

    private List<CarbonEntity> dataList;
    private CarbonAdapter adapter;
    private CarbonTreeAdapter treeAdapter;

    private int groupId;//类别id
    private int timeType = 1;//选择的时间类型(1年2月3日4时间段)
    private String classType;//选择的分类类别
    private String selectTime;//存储选择的时间

    //日期选择数据集合
    public ArrayAdapter<String> adapterTime;
    //类别列表选择器
    private List<String> groupList = new ArrayList<>();

    private DataPresenter presenter;

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
        toolbar_title.setText("碳中和分析");
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
        rvData = findViewById(R.id.rv_data);
        rvCo2 = findViewById(R.id.rv_co2);

        //类别选择器
        typeSpinner = findViewById(R.id.type_spinner);
        //时间类型选择器
        timeSpinner = findViewById(R.id.time_spinner);

        presenter = new DataPresenter();
        presenter.attachView(this);
    }

    private void initData() {
        //获取类别分组列表
        presenter.getGroup();

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
                    if(groupId != 0){
                        presenter.getCarbon(groupId,timeType,tvCurrentTime.getText().toString());
                    }
                }else if("月".equals(type)){
                    timeType = 2;
                    tvCurrentTime.setText(TimeUtils.getTime(curDate,"yyyy-MM"));
                    //切换了查询时间类别，默认查当前的
                    presenter.getCarbon(groupId,timeType,tvCurrentTime.getText().toString());
                }else if("日".equals(type)){
                    timeType = 3;
                    tvCurrentTime.setText(TimeUtils.getTime(curDate,"yyyy-MM-dd"));
                    //切换了查询时间类别，默认查当前的
                    presenter.getCarbon(groupId,timeType,tvCurrentTime.getText().toString());
                }else if("区间".equals(type)){
                    timeType = 4;
                    tvCurrentTime.setText("");
                    //dataAdapter.clearAllData();
                    //dataAdapter.notifyDataSetChanged();
                }
                selectTime = tvCurrentTime.getText().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //碳排放量展示数据
        dataList = new ArrayList<>();
        adapter= new CarbonAdapter(this,dataList);
        rvData.setLayoutManager(new LinearLayoutManager(mContext));
        rvData.setAdapter(adapter);
        //碳中和展示数据
        treeAdapter = new CarbonTreeAdapter(this,dataList);
        rvCo2.setLayoutManager(new LinearLayoutManager(mContext));
        rvCo2.setAdapter(treeAdapter);
    }

    private void initEvent() {
        tvCurrentTime.setOnClickListener(this);
        tvRangeTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_current_time:
                if(timeType == 1){
                    initTimePicker(true,false,false);
                    timePickerView.show();
                }else if(timeType == 2){
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

    //获取类别分组列表
    @Override
    public void getGroup(List<RoomEntity> data) {
        if(data != null && data.size() > 0) {
            //获取到类别列表后，默认请求第一个类别下的所有碳排放数据
            groupId = data.get(0).getId();
            presenter.getCarbon(groupId,timeType,selectTime);

            //获取第一个类别
            classType = data.get(0).getName();

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
                            //重新请求所选类别的碳排量数据
                            presenter.getCarbon(groupId,timeType,selectTime);
                        }
                    }
                }
            });
        }
    }

    //碳排放量数据返回
    @Override
    public void success(List<CarbonEntity> data) {
        adapter.clearAllData();
        treeAdapter.clearAllData();
        if(data != null && data.size() > 0){
            dataList.addAll(data);
            //设置碳排放展示数据
            adapter.notifyDataSetChanged();
            //设置碳中和展示数据
            treeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getMember(List<RoomEntity> data) {
    }

    @Override
    public void getRecord(List<RecordEntity> data) {
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

        //重新请求碳排量数据
        selectTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth +"T"+ yearEnd + "-" + (monthOfYearEnd + 1) + "-" + dayOfMonthEnd;
        presenter.getCarbon(groupId,timeType,selectTime);
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
            //重新请求碳排放量
            selectTime = tvCurrentTime.getText().toString();
            presenter.getCarbon(groupId,timeType,selectTime);
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