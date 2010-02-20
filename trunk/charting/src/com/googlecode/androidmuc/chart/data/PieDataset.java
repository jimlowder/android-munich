package com.googlecode.androidmuc.chart.data;

public class PieDataset extends Dataset {
	int[] colors;
	String[] tickLabels;
	double[] values;
	public int[] getColors() {
		return colors;
	}
	public void setColors(int[] colors) {
		this.colors = colors;
	}
	public String[] getTickLabels() {
		return tickLabels;
	}
	public void setTickLabels(String[] tickLabels) {
		this.tickLabels = tickLabels;
	}
	public double[] getValues() {
		return values;
	}
	public void setValues(double[] values) {
		this.values = values;
	}

}
