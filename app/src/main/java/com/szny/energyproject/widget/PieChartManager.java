package com.szny.energyproject.widget;

import android.graphics.Color;
import android.graphics.Typeface;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.szny.energyproject.R;
import com.szny.energyproject.application.MyApplication;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 饼状图管理类
 */
public class PieChartManager {
    public PieChart pieChart;

    public PieChartManager(PieChart pieChart) {
        this.pieChart = pieChart;
        initPieChart();
    }

    //初始化
    private void initPieChart() {
        //是否显示中间的洞
        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleRadius(40f);//设置中间洞的大小
        //半透明圈
        pieChart.setTransparentCircleRadius(30f);
        pieChart.setTransparentCircleColor(Color.WHITE); //设置半透明圆圈的颜色
        pieChart.setTransparentCircleAlpha(125); //设置半透明圆圈的透明度

        //如果没有数据的时候，会显示这个
        pieChart.setNoDataText(MyApplication.mContext.getResources().getString(R.string.no_data));
        pieChart.setRotationAngle(0);// 初始旋转角度
        pieChart.setRotationEnabled(false);// 可以手动旋转
        pieChart.setUsePercentValues(true);//显示成百分比
        pieChart.getDescription().setEnabled(false); //取消右下角描述

        //是否显示每个部分的文字描述
        pieChart.setDrawEntryLabels(false);
        pieChart.setEntryLabelColor(Color.BLACK); //描述文字的颜色
        pieChart.setEntryLabelTextSize(10);//描述文字的大小
        pieChart.setEntryLabelTypeface(Typeface.DEFAULT); //描述文字的样式

        //图相对于上下左右的偏移
        pieChart.setExtraOffsets(0, 5, 0, 15);
        //图标的背景色
        //pieChart.setBackgroundColor(Color.TRANSPARENT);
        //设置pieChart图表转动阻力摩擦系数[0,1]
        pieChart.setDragDecelerationFrictionCoef(0.75f);

        //获取图例
        Legend legend = pieChart.getLegend();
        //不显示图例
        //legend.setEnabled(false);
        //legend.setForm(Legend.LegendForm.NONE);
        //显示图例
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);  //设置图例水平显示
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP); //顶部
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT); //右对其
        legend.setXEntrySpace(7f);//x轴的间距
        legend.setYEntrySpace(10f); //y轴的间距
        legend.setYOffset(5f);  //图例的y偏移量
        legend.setXOffset(0f);  //图例x的偏移量
        legend.setTextColor(Color.parseColor("#a1a1a1")); //图例文字的颜色*/
        legend.setTextSize(12);  //图例文字的大小
    }

    /**
     * 显示圆环
     * @param yvals
     * @param colors
     */
    public void  showRingPieChart(List<PieEntry> yvals, List<Integer> colors, String payAmount,boolean isHole){
        //显示为圆环
        pieChart.setDrawHoleEnabled(isHole);
        pieChart.setHoleRadius(30f);//设置中间洞的大小

        //中间添加文字
        pieChart.setDrawCenterText(true);
        pieChart.setCenterText(payAmount); //设置中间文字
        pieChart.setCenterTextColor(Color.parseColor("#333333")); //中间文字的颜色
        pieChart.setCenterTextSizePixels(40);  //中间文字的大小px
        //pieChart.setCenterTextRadiusPercent(1f);
        pieChart.setCenterTextTypeface(Typeface.DEFAULT); //中间文字的样式
        pieChart.setCenterTextOffset(0, 0); //中间文字的偏移量

        //数据集合
        PieDataSet dataset = new PieDataSet(yvals, "");
        //填充每个区域的颜色
        dataset.setColors(colors);
        //是否在图上显示数值
        dataset.setDrawValues(true);
        //文字的大小
        dataset.setValueTextSize(14);
        //文字的颜色
        dataset.setValueTextColor(Color.RED);
        //文字的样式
        dataset.setValueTypeface(Typeface.DEFAULT);

        //当值位置为外边线时，表示线的前半段长度。
        dataset.setValueLinePart1Length(0.45f);
        //当值位置为外边线时，表示线的后半段长度。
        dataset.setValueLinePart2Length(0.45f);
        //当ValuePosits为OutsiDice时，指示偏移为切片大小的百分比
        dataset.setValueLinePart1OffsetPercentage(80f);
        // 当值位置为外边线时，表示线的颜色。
        dataset.setValueLineColor(Color.parseColor("#00000000"));
        //设置Y值的位置是在圆内还是圆外
        dataset.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        //设置Y轴描述线和填充区域的颜色一致
        //dataset.setUsingSliceColorAsValueLineColor(false);

        //设置每条之前的间隙
        dataset.setSliceSpace(4);

        //设置饼状Item被选中时变化的距离
        dataset.setSelectionShift(0f);
        //填充数据
        PieData pieData = new PieData(dataset);
        //格式化显示的数据为%百分比
        pieData.setValueFormatter(new PercentFormatter(pieChart));

        //先清除数据,再放入数据,再刷新显示视图,解决没有及时更新图的问题
        pieChart.clear();
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    /**
     * 显示实心圆
     * @param yvals
     * @param colors
     */
    public void showSolidPieChart(List<PieEntry> yvals, List<Integer> colors) {
        //数据集合
        PieDataSet dataset = new PieDataSet(yvals, "");
        //填充每个区域的颜色
        dataset.setColors(colors);
        //是否在图上显示数值
        dataset.setDrawValues(true);
        //文字的大小
        dataset.setValueTextSize(14);
        //字的颜色
        dataset.setValueTextColor(Color.RED);
        //文字的样式
        dataset.setValueTypeface(Typeface.DEFAULT_BOLD);

        //当值位置为外边线时，表示线的前半段长度。
        dataset.setValueLinePart1Length(0.4f);
        //当值位置为外边线时，表示线的后半段长度。
        dataset.setValueLinePart2Length(0.4f);
        //当ValuePosits为OutsiDice时，指示偏移为切片大小的百分比
        dataset.setValueLinePart1OffsetPercentage(80f);
        // 当值位置为外边线时，表示线的颜色。
        dataset.setValueLineColor(Color.parseColor("#a1a1a1"));
        //设置Y值的位置是在圆内还是圆外
        dataset.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
        //设置Y轴描述线和填充区域的颜色一致
        //dataset.setUsingSliceColorAsValueLineColor(false);
        //设置每条之前的间隙
        dataset.setSliceSpace(0);

        //设置饼状Item被选中时变化的距离
        dataset.setSelectionShift(5f);
        //填充数据
        PieData pieData = new PieData(dataset);
        //格式化显示的数据为%百分比
        pieData.setValueFormatter(new PercentFormatter());
        //显示试图
        pieChart.setData(pieData);
    }

    //自定义百分比格式显示
    public class PercentFormatter extends ValueFormatter {

        public DecimalFormat mFormat;
        private PieChart pieChart;

        public PercentFormatter() {
            mFormat = new DecimalFormat("###,###,##0.00");
        }

        // Can be used to remove percent signs if the chart isn't in percent mode
        public PercentFormatter(PieChart pieChart) {
            this();
            this.pieChart = pieChart;
        }

        @Override
        public String getFormattedValue(float value) {
            String str = "";
            if(value != 0){
                str = mFormat.format(value) + " %";
            }
            return str;
        }

        @Override
        public String getPieLabel(float value, PieEntry pieEntry) {
            if (pieChart != null && pieChart.isUsePercentValuesEnabled()) {
                // Converted to percent
                return getFormattedValue(value);
            } else {
                // raw value, skip percent sign
                return mFormat.format(value);
            }
        }

    }

}
