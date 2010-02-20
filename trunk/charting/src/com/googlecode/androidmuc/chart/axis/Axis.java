package com.googlecode.androidmuc.chart.axis;

import android.graphics.Paint;
import android.view.View;

public abstract class Axis {
	
	public enum Position {
		LEFT, RIGHT, TOP, BOTTOM
	}
	
	Paint paint;
	float tickSizeInner;
	float tickSizeOuter;
	String title;
	
	Position position;
	int measuredWidth;
	int measuredHeight;
	
	public float getTickSizeInner() {
		return tickSizeInner;
	}
	public void setTickSizeInner(float tickSizeInner) {
		this.tickSizeInner = tickSizeInner;
	}
	public float getTickSizeOuter() {
		return tickSizeOuter;
	}
	public void setTickSizeOuter(float tickSizeOuter) {
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
			setMeasuredDimension((int) (tickSizeInner+tickSizeOuter), height);
		else
			setMeasuredDimension(width, (int) (tickSizeInner+tickSizeOuter));
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
	public Paint getPaint() {
		return paint;
	}
	public void setPaint(Paint paint) {
		this.paint = paint;
	}
	
}
