package com.googlecode.androidmuc.chart.axis;

public class CategoryAxis extends Axis {
	boolean tickOnCategories;
	boolean tickBetweenCategories;
	
	String[] categories;

	public boolean isTickOnCategories() {
		return tickOnCategories;
	}

	public void setTickOnCategories(boolean tickOnCategories) {
		this.tickOnCategories = tickOnCategories;
	}

	public boolean isTickBetweenCategories() {
		return tickBetweenCategories;
	}

	public void setTickBetweenCategories(boolean tickBetweenCategories) {
		this.tickBetweenCategories = tickBetweenCategories;
	}

	public String[] getCategories() {
		return categories;
	}

	public void setCategories(String[] categories) {
		this.categories = categories;
	}

	
}
