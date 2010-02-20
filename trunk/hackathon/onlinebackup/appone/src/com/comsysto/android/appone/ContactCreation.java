package com.comsysto.android.appone;

import com.comsysto.android.appone.*;
import com.comsysto.vooone.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class ContactCreation extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createcontactview);
        Button cancelButton = (Button)findViewById(R.id.ButtonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				Intent var = new Intent(ContactCreation.this, StartActivity.class);
				finish();
			}
		});

        Button createButton = (Button)findViewById(R.id.ButtonCreate);        
        createButton.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View arg1) {
				Contact contact = new Contact();
				EditText firstName = (EditText)findViewById(R.id.firstName);
				contact.setFirstName(firstName.getText().toString());
				
				EditText lastName = (EditText)findViewById(R.id.lastName);
				contact.setLastName(lastName.getText().toString());
				
				EditText eMail = (EditText)findViewById(R.id.eMail);
				contact.seteMail(eMail.getText().toString());
				
				EditText phoneNumber = (EditText)findViewById(R.id.phoneNumber);
				contact.setPhoneNumber(phoneNumber.getText().toString());
				
				finish();
			}
		});
        
    }
}