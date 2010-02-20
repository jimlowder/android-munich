package com.googlecode.androidmuc.chart.axis;

import android.view.View;

public abstract class Axis {
	
	public enum Position {
		LEFT, RIGHT, TOP, BOTTOM
	}
	
	int tickColor;
	int tickSizeInner;
	int tickSizeOuter;
	String title;
	
	Position position;
	int measuredWidth;
	int measuredHeight;
	
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = View.MeasureSpec.getSize(widthMeasureSpec);
		int height = View.MeasureSpec.getSize(heightMeasureSpec);
		if (position == Position.LEFT || position == Position.RIGHT)
			setMeasuredDimension(tickSizeInner+tickSizeOuter, height);
		else
			setMeasuredDimension(width, tickSizeInner+tickSizeOuter);
	}
	public int getMeasuredWidth() {
		return measuredWidth;
	}
	public int getMeasuredHeight() {
		return measuredHeight;
	}
	protected void setMeasuredDimension(int width, int height) {
		measuredWidth = width;
		measuredHeight = height;
	}
	
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
}
