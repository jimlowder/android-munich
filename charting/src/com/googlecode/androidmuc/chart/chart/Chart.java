package com.googlecode.androidmuc.chart.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public abstract class Chart extends View {
	String title;

	public Chart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public Chart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Chart(Context context) {
		super(context);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void paintText(Canvas c, String text, int beginDrawX, int beginDrawY) {
		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setColor(Color.WHITE);
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(25);
		foreground.setTextAlign(Paint.Align.CENTER);
		FontMetrics fm = foreground.getFontMetrics();
		
		c.drawText(text, beginDrawX, beginDrawY, foreground);
	}
	
}
