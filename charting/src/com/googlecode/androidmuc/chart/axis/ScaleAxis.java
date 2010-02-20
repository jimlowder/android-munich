package com.googlecode.androidmuc.chart.axis;

public class ScaleAxis extends Axis {
	double maxValue;
	double minValue;
	double tickInterval;
	double tickStart;
	float scaleFactor;
	
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

	
}
