package com.googlecode.androidmuc.chart.chart;

import android.content.Context;
import android.util.AttributeSet;

import com.googlecode.androidmuc.chart.axis.CategoryAxis;
import com.googlecode.androidmuc.chart.data.YDataset;

import java.util.List;

public class BarChart extends Chart {

	public BarChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public BarChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public BarChart(Context context) {
		super(context);
	}
	CategoryAxis xAxis;
	List<YDataset> data;
	int barDistance;
	
	public CategoryAxis getxAxis() {
		return xAxis;
	}
	public void setxAxis(CategoryAxis xAxis) {
		this.xAxis = xAxis;
	}
	public List<YDataset> getData() {
		return data;
	}
	public void setData(List<YDataset> data) {
		this.data = data;
	}
	public int getBarDistance() {
		return barDistance;
	}
	public void setBarDistance(int barDistance) {
		this.barDistance = barDistance;
	}
	
}
