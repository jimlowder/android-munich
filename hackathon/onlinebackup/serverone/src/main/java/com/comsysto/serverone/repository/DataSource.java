package com.comsysto.serverone.repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.comsysto.vooone.Contact;

/**
 * @author danielbartl
 * 
 */
@Service
public class DataSource {

	private Map<Long, Contact> db = new ConcurrentHashMap<Long, Contact>();

	public void put(Contact contact) {
		if (contact != null) {
			db.put(contact.getId(), contact);
		}
	}

	public Contact get(Long id) {
		return db.get(id);
	}

	public Collection<Contact> all() {
		return db.values();
	}
}
