package com.googlecode.androidmuc.chart.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.util.AttributeSet;
import android.util.Log;

import com.googlecode.androidmuc.chart.axis.Axis;
import com.googlecode.androidmuc.chart.axis.ScaleAxis;
import com.googlecode.androidmuc.chart.data.XYDataset;

import java.util.List;

public class XYChart extends YChart {

	static final String TAG = "XYChart";
		
	double yAxisAtX;
	ScaleAxis xAxis;
	List<XYDataset> data;
	
	public XYChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	public XYChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public XYChart(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		Log.v(TAG, "onLayout("+changed+","+left+","+top+","+right+","+bottom+")");
		super.onLayout(changed, left, top, right, bottom);
		xAxis.onLayout(changed, left, top, right, bottom);
		yAxis.onLayout(changed, left, top, right, bottom);
		if (y2AxisList != null)
			for (ScaleAxis y2axis : y2AxisList) {
				y2axis.onLayout(changed, left, top, right, bottom);
			}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.v(TAG, "onMeasure("+widthMeasureSpec+","+heightMeasureSpec+")");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		xAxis.onMeasure(widthMeasureSpec, heightMeasureSpec);
		yAxis.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (y2AxisList != null)
			for (ScaleAxis y2axis : y2AxisList) {
				y2axis.onMeasure(widthMeasureSpec, heightMeasureSpec);
			}
	}
	
	@Override
	protected void onDraw(Canvas c) {
		
//		super.onDraw(c);
		
		Matrix matrix1 = new Matrix();
		matrix1.setTranslate(0, 724);
		Matrix matrix2 = new Matrix();
		matrix2.setScale(30, -20);
		
		Paint mPaint = new Paint();
		mPaint.setColor(Color.BLUE);
		mPaint.setStrokeWidth(2);
		mPaint.setAntiAlias(true);
		mPaint.setStrokeCap(Cap.ROUND);
		
		
		Log.v(TAG, "canvas width   : " + c.getWidth());
		Log.v(TAG, "canvas height  : " + c.getHeight());
		Log.v(TAG, "matrix  : " + matrix1.toString());
		Log.v(TAG, "measured width : " + getMeasuredWidth());
		Log.v(TAG, "measured height: " + getMeasuredHeight());
		Log.v(TAG, "clip rectangle : " + c.getClipBounds().toString());
	
		float[] points = new float[] { 0, 0, 10, 10 };
		matrix2.mapPoints(points);
		matrix1.mapPoints(points);
		
		StringBuffer pointlist = new StringBuffer();
		for (float f : points) {
			pointlist.append(f);
			pointlist.append(' ');
		}
		Log.v(TAG,pointlist.toString());
		// Draw Axis
		c.drawLines(points, mPaint);
//		c.drawLine(0, 0, 10, 10, mPaint);
	}
	public double getyAxisAtX() {
		return yAxisAtX;
	}
	public void setyAxisAtX(double yAxisAtX) {
		this.yAxisAtX = yAxisAtX;
	}
	public ScaleAxis getxAxis() {
		return xAxis;
	}
	public void setxAxis(ScaleAxis xAxis) {
		this.xAxis = xAxis;
		this.xAxis.setPosition(Axis.Position.BOTTOM);
	}
	public List<XYDataset> getData() {
		return data;
	}
	public void setData(List<XYDataset> data) {
		this.data = data;
	}
}
