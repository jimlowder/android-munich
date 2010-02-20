package com.googlecode.androidmuc.chart.data;

import com.googlecode.androidmuc.chart.axis.ScaleAxis;

public abstract class AxisDataset extends Dataset {

	ScaleAxis axis;
	int color;
	public ScaleAxis getAxis() {
		return axis;
	}
	public void setAxis(ScaleAxis axis) {
		this.axis = axis;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	
}
