package com.comsysto.android.appone.service;

import java.util.ArrayList;
import java.util.List;

import com.comsysto.vooone.Contact;

public class OnlineBackupServiceMock implements OnlineBackupService {

	private List<Contact> contacts = new ArrayList<Contact>();

	public OnlineBackupServiceMock(){
		contacts = new ArrayList<Contact>();
		Contact firstContact = new Contact();
		firstContact.seteMail("hallo@asdfasdf.de");
		firstContact.setFirstName("tollervorname");
		firstContact.setLastName("nachname");
		firstContact.setPhoneNumber("49179234234");
		contacts.add(firstContact);
	}
	
	public List<Contact> createContact(Contact newContact) {
		contacts.add(newContact);
		return getContacts();
	}

	public List<Contact> getContacts() {
		return contacts;
	}

}
