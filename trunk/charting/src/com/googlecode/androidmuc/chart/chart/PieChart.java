package com.googlecode.androidmuc.chart.chart;

import android.content.Context;
import android.util.AttributeSet;

import com.googlecode.androidmuc.chart.data.PieDataset;

public class PieChart extends Chart {
	int lineColor;
	int lineWidth;
	
	int tickColor;
	int tickSizeInner;
	int tickSizeOuter;
	
	PieDataset data;

	public PieChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public PieChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public PieChart(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public int getLineColor() {
		return lineColor;
	}

	public void setLineColor(int lineColor) {
		this.lineColor = lineColor;
	}

	public int getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}

	public int getTickColor() {
		return tickColor;
	}

	public void setTickColor(int tickColor) {
		this.tickColor = tickColor;
	}

	public int getTickSizeInner() {
		return tickSizeInner;
	}

	public void setTickSizeInner(int tickSizeInner) {
		this.tickSizeInner = tickSizeInner;
	}

	public int getTickSizeOuter() {
		return tickSizeOuter;
	}

	public void setTickSizeOuter(int tickSizeOuter) {
		this.tickSizeOuter = tickSizeOuter;
	}

	public PieDataset getData() {
		return data;
	}

	public void setData(PieDataset data) {
		this.data = data;
	}
	
	
}
