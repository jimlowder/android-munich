package com.googlecode.androidmuc.chart.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import com.googlecode.androidmuc.chart.axis.Axis;
import com.googlecode.androidmuc.chart.axis.ScaleAxis;
import com.googlecode.androidmuc.chart.data.XYDataset;

import java.util.List;

public class XYChart extends YChart {

	static final String TAG = "XYChart";
	Matrix scaleMatrix;
	Matrix translateMatrix;
	
	double yAxisAtX;
	ScaleAxis xAxis;
	List<XYDataset> data;
	
	public XYChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public XYChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public XYChart(Context context) {
		super(context);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		Log.v(TAG, "onLayout("+changed+","+left+","+top+","+right+","+bottom+")");
		super.onLayout(changed, left, top, right, bottom);
		xAxis.onLayout(changed, yAxis.getMeasuredWidth(), top, right, bottom);
		yAxis.onLayout(changed, left, top, right, bottom);
		if (y2AxisList != null)
			for (ScaleAxis y2axis : y2AxisList) {
				y2axis.onLayout(changed, left, top, right, bottom);
			}
		
		float[] mins = new float[] { (float) xAxis.getMinValue(), (float) yAxis.getMinValue()};

		scaleMatrix = new Matrix();
		scaleMatrix.setScale(xAxis.getScaleFactor(), yAxis.getScaleFactor());
		scaleMatrix.mapPoints(mins);
		
		translateMatrix = new Matrix();
		translateMatrix.setTranslate(-mins[0]+yAxis.getMeasuredWidth(), getMeasuredHeight()-1-mins[1]);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		xAxis.onMeasure(widthMeasureSpec, heightMeasureSpec);
		yAxis.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (y2AxisList != null)
			for (ScaleAxis y2axis : y2AxisList) {
				y2axis.onMeasure(widthMeasureSpec, heightMeasureSpec);
			}
	}
	
	private void drawAxis(Canvas c) {
		Paint axisPaint = xAxis.getPaint();
		float[] axis = new float[]{ (float) xAxis.getMinValue(), (float) xAxisAtY, (float) xAxis.getMaxValue(), (float) xAxisAtY };
		scaleMatrix.mapPoints(axis);
		translateMatrix.mapPoints(axis);
		c.drawLines(axis, axisPaint);
		
		axisPaint = yAxis.getPaint();
		axis = new float[]{ (float) yAxisAtX, (float) yAxis.getMaxValue(), (float) yAxisAtX, (float) yAxis.getMinValue() };
		scaleMatrix.mapPoints(axis);
		translateMatrix.mapPoints(axis);
		c.drawLines(axis, axisPaint);
		
		for (double i = yAxis.getMinValue()+yAxis.getTickStart(); i <= yAxis.getMaxValue(); i+=yAxis.getTickInterval()) {
			float[] tick = new float[]{(float)yAxisAtX-yAxis.getTickSizeOuter(), (float) i, (float)yAxisAtX+yAxis.getTickSizeInner(), (float) i};
			scaleMatrix.mapPoints(tick);
			translateMatrix.mapPoints(tick);
			c.drawLines(tick, axisPaint);
			tick = new float[]{(float)yAxisAtX-yAxis.getTickSizeOuter(), (float) i};
			scaleMatrix.mapPoints(tick);
			translateMatrix.mapPoints(tick);
			c.drawText(yAxis.getLabelForValue(i), tick[0], tick[1],axisPaint);
		}
		
		drawPfeil(c, axisPaint, 0.2f);
	}

	private void drawData(Canvas c) {
		Paint dataPaint = new Paint();
		dataPaint.setStrokeWidth(2);
		dataPaint.setAntiAlias(true);
		
		for (XYDataset dat : data) {
			dataPaint.setColor(dat.getColor());
			float[] pts = makePointArray(dat.getxValues(), dat.getyValues());
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
	}
	
	@Override
	protected void onDraw(Canvas c) {
		drawData(c);
		drawAxis(c);
//		super.paintText(c, "Y Achse", 10, 10);
	}
	
	private void drawPfeil(Canvas c, Paint axisPaint, float distance) {
		axisPaint.setAntiAlias(true);
		float[] axis2 = new float[]{ (float) yAxisAtX - distance, (float) yAxis.getMaxValue() - distance,(float) yAxisAtX, (float) yAxis.getMaxValue(),
				                     (float) yAxisAtX, (float) yAxis.getMaxValue(), (float) yAxisAtX + distance, (float) yAxis.getMaxValue() - distance,
				                     (float) xAxis.getMaxValue(), (float) xAxisAtY , (float) xAxis.getMaxValue() - distance, (float) xAxisAtY + distance,
				                     (float) xAxis.getMaxValue(), (float) xAxisAtY , (float) xAxis.getMaxValue() - distance, (float) xAxisAtY - distance};
		scaleMatrix.mapPoints(axis2);
		translateMatrix.mapPoints(axis2);
		// Draw Axis
		c.drawLines(axis2, axisPaint);
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
