package com.comsysto.android.appone;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.comsysto.android.appone.service.OnlineBackupService;
import com.comsysto.android.appone.service.OnlineBackupServiceFactory;
import com.comsysto.vooone.Contact;

public class OBListActivity extends ListActivity {
	   @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        // Get a factory with all people
	        final OnlineBackupService onBS = new OnlineBackupServiceFactory().getOnlineBackupService();
	        
	        
			List<Contact> coList = onBS.getContacts(); 

	        //ListAdapter adapter = new ArrayAdapter<Contact>(null, 0, 0, coList);
	       
	        // Use an existing ListAdapter that will map an array
	        // of strings to TextViews
	        setListAdapter(new ArrayAdapter<Contact>(this,
	                android.R.layout.simple_list_item_1, coList));
	        getListView().setTextFilterEnabled(true);
	    }
	   
	}
