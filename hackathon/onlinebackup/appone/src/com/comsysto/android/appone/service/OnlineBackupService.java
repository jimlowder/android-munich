package com.comsysto.android.appone.service;

import java.util.List;

import com.comsysto.vooone.Contact;

public interface OnlineBackupService {

	public List<Contact> createContact(Contact newContact);
	public List<Contact> getContacts();
}
