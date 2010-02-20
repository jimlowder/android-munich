package activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.view.View;

import com.googlecode.androidmuc.chart.R;
import com.googlecode.androidmuc.chart.axis.ScaleAxis;
import com.googlecode.androidmuc.chart.chart.XYChart;
import com.googlecode.androidmuc.chart.data.XYDataset;

import java.util.Arrays;

public class DemoMain extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		final float scale = getResources().getDisplayMetrics().density;

        findViewById(R.id.TestButton1).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
				
				XYChart c = new XYChart(DemoMain.this);
				c.setxAxis(xAxis);
				c.setyAxis(yAxis);
				c.setyAxisAtX(0);
				c.setxAxisAtY(0);
				c.setData(Arrays.asList(new XYDataset[]{data}));
				
				
				((DiagramApp) getApplication()).c = c;
				startActivity(new Intent(DemoMain.this, DiagramActivity.class));
				
			}
		});
        findViewById(R.id.TestButton2).setOnClickListener(new View.OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
        		Paint axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        		axisPaint.setColor(Color.YELLOW);
        		axisPaint.setStyle(Style.FILL);
        		axisPaint.setTextSize(10 * scale);
        		axisPaint.setTextAlign(Paint.Align.RIGHT);
        		axisPaint.setStrokeWidth(0);
        		
        		
        		ScaleAxis xAxis = new ScaleAxis();
        		xAxis.setMinValue(-5d);
        		xAxis.setMaxValue(5d);
        		xAxis.setTickInterval(1d);
        		xAxis.setLabelFormat("0");
        		xAxis.setPaint(axisPaint);
        		
        		ScaleAxis yAxis = new ScaleAxis();
        		yAxis.setMinValue(-5);
        		yAxis.setMaxValue(5d);
        		yAxis.setTickInterval(1);
        		yAxis.setLabelFormat("0");
        		yAxis.setPaint(axisPaint);
        		yAxis.setTickSizeInner(.2f);
        		yAxis.setTickSizeOuter(.2f);
        		yAxis.setTickStart(0);
        		
        		XYDataset data = new XYDataset();
        		data.setColor(Color.RED);
        		data.setxValues(new double[]{ -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5 });
        		data.setyValues(new double[]{ 2.5,1.6,0.9,0.4,0.1,0,0.1,0.4,0.9,1.6,2.5});
        		data.setAxis(yAxis);
        		
        		XYChart c = new XYChart(DemoMain.this);
        		c.setxAxis(xAxis);
        		c.setyAxis(yAxis);
        		c.setyAxisAtX(0);
        		c.setxAxisAtY(0);
        		c.setData(Arrays.asList(new XYDataset[]{data}));
        		
        		
        		((DiagramApp) getApplication()).c = c;
        		startActivity(new Intent(DemoMain.this, DiagramActivity.class));
        		
        	}
        });
        findViewById(R.id.TestButton3).setOnClickListener(new View.OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
        		Paint axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        		axisPaint.setColor(Color.GRAY);
        		axisPaint.setStyle(Style.FILL);
        		axisPaint.setTextSize(10 * scale);
        		axisPaint.setTextAlign(Paint.Align.RIGHT);
        		axisPaint.setStrokeWidth(0);
        		
        		
        		ScaleAxis xAxis = new ScaleAxis();
        		xAxis.setMinValue(-5d);
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
        		XYDataset data2 = new XYDataset();
        		data2.setColor(Color.RED);
        		data2.setxValues(new double[]{ -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5 });
        		data2.setyValues(new double[]{ 2.5,1.6,0.9,0.4,0.1,0,0.1,0.4,0.9,1.6,2.5});
        		data2.setAxis(yAxis);
        		
        		XYChart c = new XYChart(DemoMain.this);
        		c.setxAxis(xAxis);
        		c.setyAxis(yAxis);
        		c.setyAxisAtX(0);
        		c.setxAxisAtY(0);
        		c.setData(Arrays.asList(new XYDataset[]{data,data2}));
        		
        		
        		((DiagramApp) getApplication()).c = c;
        		startActivity(new Intent(DemoMain.this, DiagramActivity.class));
        		
        	}
        });
    }
}