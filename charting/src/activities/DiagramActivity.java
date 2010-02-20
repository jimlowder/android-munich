package activities;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;

import com.googlecode.androidmuc.chart.axis.ScaleAxis;
import com.googlecode.androidmuc.chart.chart.Chart;
import com.googlecode.androidmuc.chart.chart.XYChart;
import com.googlecode.androidmuc.chart.data.XYDataset;

import java.util.Arrays;

public class DiagramActivity extends Activity {
	Chart c;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		float scale = getResources().getDisplayMetrics().density;
//		Bundle bundle = getIntent().getExtras();
		
		Paint axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		axisPaint.setColor(Color.WHITE);
		axisPaint.setStyle(Style.FILL);
		axisPaint.setTextSize(10 * scale);
		axisPaint.setTextAlign(Paint.Align.RIGHT);
		axisPaint.setStrokeWidth(0);

		
		ScaleAxis xAxis = new ScaleAxis();
		xAxis.setMinValue(-0.2d);
		xAxis.setMaxValue(5d);
		xAxis.setTickInterval(1d);
		xAxis.setLabelFormat("0");
		xAxis.setPaint(axisPaint);
		
		ScaleAxis yAxis = new ScaleAxis();
		yAxis.setMinValue(-0.2d);
		yAxis.setMaxValue(5d);
		yAxis.setTickInterval(0.5);
		yAxis.setLabelFormat("0.0");
		yAxis.setPaint(axisPaint);
		yAxis.setTickSizeInner(.1f);
		yAxis.setTickSizeOuter(.1f);
		yAxis.setTickStart(0.7);
		
		XYDataset data = new XYDataset();
		data.setColor(Color.BLUE);
		data.setxValues(new double[]{ 0,1,2,3,4,5 });
		data.setyValues(new double[]{ 1,2,1,3,2,4 });
		data.setAxis(yAxis);
		
		XYChart c = new XYChart(this);
		c.setxAxis(xAxis);
		c.setyAxis(yAxis);
		c.setyAxisAtX(0);
		c.setxAxisAtY(0);
		c.setData(Arrays.asList(new XYDataset[]{data}));
		
		setContentView(c);
	}
	
}
