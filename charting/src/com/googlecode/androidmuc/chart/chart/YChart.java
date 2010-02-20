package com.googlecode.androidmuc.chart.chart;

import android.content.Context;
import android.util.AttributeSet;

import com.googlecode.androidmuc.chart.axis.Axis;
import com.googlecode.androidmuc.chart.axis.ScaleAxis;

import java.util.List;

public abstract class YChart extends Chart {
	ScaleAxis yAxis;
	double xAxisAtY;
	
	List<ScaleAxis> y2AxisList;

	public YChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public YChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public YChart(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ScaleAxis getyAxis() {
		return yAxis;
	}

	public void setyAxis(ScaleAxis yAxis) {
		this.yAxis = yAxis;
		this.yAxis.setPosition(Axis.Position.LEFT);
	}

	public double getxAxisAtY() {
		return xAxisAtY;
	}

	public void setxAxisAtY(double xAxisAtY) {
		this.xAxisAtY = xAxisAtY;
	}

	public List<ScaleAxis> getY2AxisList() {
		return y2AxisList;
	}

	public void setY2AxisList(List<ScaleAxis> y2AxisList) {
		this.y2AxisList = y2AxisList;
	}

}
