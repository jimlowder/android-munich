package com.googlecode.androidmuc.chart.axis;

import android.view.View;

import java.text.SimpleDateFormat;

public class ScaleAxis extends Axis {
	double maxValue;
	double minValue;
	double tickInterval;
	double tickStart;
	float scaleFactor;
	SimpleDateFormat labelFormat;
	
	public float getScaleFactor() {
		return scaleFactor;
	}
	public void setScaleFactor(float scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	public double getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}
	public double getMinValue() {
		return minValue;
	}
	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}
	public double getTickInterval() {
		return tickInterval;
	}
	public void setTickInterval(double tickInterval) {
		this.tickInterval = tickInterval;
	}
	public double getTickStart() {
		return tickStart;
	}
	public void setTickStart(double tickStart) {
		this.tickStart = tickStart;
	}
	public void onLayout(boolean changed, int left, int top, int right, int bottom) {
		if (position == Position.LEFT || position == Position.RIGHT)
			scaleFactor = (float) ((top-bottom)/(maxValue-minValue));
		else
			scaleFactor = (float) ((right-left)/(maxValue-minValue));
	}
	public void setLabelFormat(String pattern) {
		labelFormat = new SimpleDateFormat(pattern);
	}
	public String getLabelForValue(double value) {
		return labelFormat.format(value);
	}
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = View.MeasureSpec.getSize(widthMeasureSpec);
		int height = View.MeasureSpec.getSize(heightMeasureSpec);
		
		if (position == Position.LEFT || position == Position.RIGHT)
			setMeasuredDimension((int) (Math.max(paint.measureText(getLabelForValue(maxValue)), paint.measureText(getLabelForValue(minValue))) + tickSizeInner+tickSizeOuter), height);
		else
			setMeasuredDimension(width, (int) (tickSizeInner+tickSizeOuter + paint.getTextSize()));
	}

}
