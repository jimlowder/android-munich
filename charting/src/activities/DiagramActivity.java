package activities;

import android.app.Activity;
import android.graphics.Color;
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
//		Bundle bundle = getIntent().getExtras();
		
		ScaleAxis xAxis = new ScaleAxis();
		xAxis.setMinValue(0d);
		xAxis.setMaxValue(5d);
		ScaleAxis yAxis = new ScaleAxis();
		yAxis.setMinValue(0d);
		yAxis.setMaxValue(5d);
		
		XYDataset data = new XYDataset();
		data.setColor(Color.BLUE);
		data.setxValues(new double[]{ 0,1,2,3,4,5 });
		data.setyValues(new double[]{ 4,2,3,1,2,1});
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
