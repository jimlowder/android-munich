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
		
		Paint axisPaint = new Paint();
		axisPaint.setColor(Color.WHITE);
		axisPaint.setStrokeWidth(0);
		
		float[] pts = new float[]{ (float) yAxisAtX, (float) yAxis.getMaxValue(), (float) yAxisAtX, (float) yAxis.getMinValue(),
				(float) xAxis.getMinValue(), (float) xAxisAtY, (float) xAxis.getMaxValue(), (float) xAxisAtY };
		float[] mins = new float[] { (float) xAxis.getMinValue(), (float) yAxis.getMinValue()};

		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(xAxis.getScaleFactor(), yAxis.getScaleFactor());
		scaleMatrix.mapPoints(pts);
		scaleMatrix.mapPoints(mins);
		
		Matrix translateMatrix = new Matrix();
		translateMatrix.setTranslate(-mins[0], getMeasuredHeight()-1-mins[1]);
		translateMatrix.mapPoints(pts);
		
//		Log.v(TAG, "canvas width   : " + c.getWidth());
//		Log.v(TAG, "canvas height  : " + c.getHeight());
//		Log.v(TAG, "measured width : " + getMeasuredWidth());
//		Log.v(TAG, "measured height: " + getMeasuredHeight());
//		Log.v(TAG, "clip rectangle : " + c.getClipBounds().toString());
	
		
		Paint dataPaint = new Paint();
		dataPaint.setStrokeWidth(2);
		for (XYDataset dat : data) {
			dataPaint.setColor(dat.getColor());
			pts = makePointArray(dat.getxValues(), dat.getyValues());
			scaleMatrix.mapPoints(pts);
			translateMatrix.mapPoints(pts);
			StringBuffer pointlist = new StringBuffer();
			for (float f : pts) {
				pointlist.append(f);
				pointlist.append(' ');
			}
			Log.v(TAG,pointlist.toString());
			c.drawLines(pts, dataPaint);
		}
		// Draw Axis
		c.drawLines(pts, axisPaint);
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
	
	private float[] makePointArray(double[] xValues, double[] yValues) {
		if (xValues.length != yValues.length)
			throw new IllegalArgumentException();
		
		float[] ret = new float[4*xValues.length];
		int j = 0;
		for (int i = 0; i < xValues.length; i++) {
			ret[j++] = (float) xValues[i];
			ret[j++] = (float) yValues[i];
			if (i > 0 && i < xValues.length - 1) {
				ret[j++] = (float) xValues[i];
				ret[j++] = (float) yValues[i];
			}
		}
		
		return ret;
	}
	
}
