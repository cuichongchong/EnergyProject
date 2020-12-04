package com.szny.energyproject.widget;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * 条形堆叠图管理类
 * */
public class BarChartManager {
    private BarChart mBarChart;
    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;

    public BarChartManager(BarChart barChart) {
        this.mBarChart = barChart;
        leftAxis = mBarChart.getAxisLeft();
        rightAxis = mBarChart.getAxisRight();
        xAxis = mBarChart.getXAxis();
    }

    /**
     * 初始化Chart
     */
    private void initChart() {
        //不显示描述信息
        mBarChart.getDescription().setEnabled(false);
        mBarChart.setMaxVisibleValueCount(40);
        // X,Y轴同时缩放，false则X,Y轴单独缩放,默认false
        mBarChart.setPinchZoom(false);
        //是否显示网格背景
        mBarChart.setDrawGridBackground(false);
        //显示每条背景阴影
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawValueAboveBar(false);
        mBarChart.setHighlightFullBarEnabled(false);
        //图相对于上下左右的偏移
        mBarChart.setExtraOffsets(0, 5, 0, 5);

        //保证Y轴从0开始，不然会上移一点
        leftAxis.setAxisMinimum(0f);
        //设置右侧y轴线跟数据都不显示
        rightAxis.setEnabled(false);
        //X轴设置显示位置在顶部
        xAxis.setPosition(XAxis.XAxisPosition.TOP);

        Legend legend = mBarChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setFormSize(8f);
        legend.setFormToTextSpace(4f);
        legend.setXEntrySpace(6f);
    }

    /**
     * 展示条形堆叠图
     * */
    public void showBarChart(List<BarEntry> yVals, String label,String[] stackLabels){
        initChart();

        BarDataSet set;
        if (mBarChart.getData() != null && mBarChart.getData().getDataSetCount() > 0) {
            set = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
            set.setValues(yVals);
            mBarChart.getData().notifyDataChanged();
            mBarChart.notifyDataSetChanged();
        }else{
            set = new BarDataSet(yVals,label);
            set.setColors(getColors());
            set.setStackLabels(stackLabels);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set);

            BarData data = new BarData(dataSets);
            data.setValueTextColor(Color.WHITE);

            mBarChart.setData(data);
        }

        mBarChart.setFitBars(true);
        mBarChart.invalidate();
    }

    private int[] getColors() {
        int stacksize = 4;
        //有尽可能多的颜色每项堆栈值
        int[] colors = new int[stacksize];
        int[] MATERIAL_COLORS = {rgb("#FFD866"),rgb("#93C37E"),rgb("#74A6AD"),rgb("#6D9EEB")};
        for (int i = 0; i < colors.length; i++) {
            colors[i] = MATERIAL_COLORS[i];
        }
        return colors;
    }

    public int rgb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return Color.rgb(r, g, b);
    }

}
