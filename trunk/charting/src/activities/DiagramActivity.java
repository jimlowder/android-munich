package activities;

import android.app.Activity;
import android.os.Bundle;

public class DiagramActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(((DiagramApp) getApplication()).c);
	}
	
}
