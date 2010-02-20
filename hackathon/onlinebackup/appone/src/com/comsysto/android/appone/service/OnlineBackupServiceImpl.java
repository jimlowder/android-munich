package com.comsysto.android.appone.service;

import java.util.List;

import com.comsysto.android.appone.rest.RestClient;
import com.comsysto.vooone.Contact;

public class OnlineBackupServiceImpl implements OnlineBackupService {

	public List<Contact> createContact(Contact newContact) {
		RestClient.createContact(newContact);
		return RestClient.getContacts();
	}

	public List<Contact> getContacts() {
		return RestClient.getContacts();
	}

	
}
