package com.comsysto.serverone.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comsysto.serverone.repository.DataSource;
import com.comsysto.vooone.Contact;

/**
 * @author danielbartl
 * 
 */
@Controller
@RequestMapping("/rest")
public class JSONController {

	@Autowired
	private DataSource dataSource;

	@RequestMapping(value = "contacts/mock", method = RequestMethod.GET)
	public @ResponseBody
	List<Contact> getContactsForTesting() {
		List<Contact> contacts = new ArrayList<Contact>();
		Contact c1 = new Contact();
		c1.setFirstName("Max");
		c1.setLastName("Muster");
		c1.seteMail("max.muster@gmail.com");
		c1.setPhoneNumber("+ 49 170 123456");
		contacts.add(c1);
		return contacts;
	}

	@RequestMapping(value = "contact/mock", method = RequestMethod.GET)
	public @ResponseBody
	Collection<Contact> updateMock(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "firstName", required = false) String firstName,
			@RequestParam(value = "lastName", required = false) String lastName,
			@RequestParam("eMail") String eMail,
			@RequestParam(value = "phoneNumber", required = false) String phoneNumber) {
		return update(id, firstName, lastName, eMail, phoneNumber);
	}

	@RequestMapping(value = "contact", method = RequestMethod.PUT)
	public @ResponseBody
	Collection<Contact> update(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "firstName", required = false) String firstName,
			@RequestParam(value = "lastName", required = false) String lastName,
			@RequestParam("eMail") String eMail,
			@RequestParam(value = "phoneNumber", required = false) String phoneNumber) {
		if (id == null) {
			id = Long.valueOf(RandomStringUtils.randomNumeric(10));
		}
		Contact contact = new Contact(id, firstName, lastName, eMail,
				phoneNumber);
		System.out.println("created new contact : " + contact);
		dataSource.put(contact);
		System.out.println("contact saved : " + contact);
		return dataSource.all();
	}

	@RequestMapping(value = "contacts", method = RequestMethod.GET)
	public @ResponseBody
	Collection<Contact> all() {
		return dataSource.all();
	}

	@RequestMapping(value = "contact/id", method = RequestMethod.GET)
	public @ResponseBody
	Contact get(@RequestParam("id") Long id) {
		return dataSource.get(id);
	}

	@RequestMapping(value = "contact/email", method = RequestMethod.GET)
	public @ResponseBody
	Contact find(@RequestParam("eMail") String eMail) {
		Collection<Contact> all = dataSource.all();
		for (Contact contact : all) {
			if (StringUtils.equals(eMail, contact.geteMail())) {
				return contact;
			}
		}
		return null;
	}

}
