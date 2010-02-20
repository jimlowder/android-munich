package com.comsysto.android.appone;

import android.app.ListActivity;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class OBListActivity extends ListActivity {
	   @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        // Get a cursor with all people
	        Cursor c = getContentResolver().query(People.CONTENT_URI, null, null, null, null);
	        startManagingCursor(c);

	        ListAdapter adapter = new SimpleCursorAdapter(this, 
	                // Use a template that displays a text view
	                android.R.layout.simple_list_item_1, 
	                // Give the cursor to the list adatper
	                c, 
	                // Map the NAME column in the people database to...
	                new String[] {People.NAME} ,
	                // The "text1" view defined in the XML template
	                new int[] {android.R.id.text1}); 
	        setListAdapter(adapter);
	    }
	}
