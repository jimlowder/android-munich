package com.comsysto.android.appone;

import com.comsysto.android.appone.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends Activity {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button findViewById = (Button)findViewById(R.id.ButtonGoToCreate);
		findViewById.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				Intent var = new Intent(StartActivity.this, ContactCreation.class);
				startActivity(var);
			}
		});
		
//		Button buttonListContacts = (Button) findViewById(R.id.ButtonListContacts);
//		buttonListContacts.setOnClickListener(new View.OnClickListener() {
//			
//			public void onClick(View arg0) {
//				Intent var = new Intent(StartActivity.this, OBListActivity.class);
//				startActivity(var);
//			}
//		});
		
	}
}
