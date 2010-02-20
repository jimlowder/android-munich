package com.comsysto.serverone.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comsysto.vooone.Contact;

/**
 * @author danielbartl
 * 
 */
@Controller
@RequestMapping("/rest")
public class JSONController {

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

	@RequestMapping(value = "contacts", method = RequestMethod.GET)
	public @ResponseBody
	List<Contact> getContacts() {
		List<Contact> contacts = new ArrayList<Contact>();
		return contacts;

	}

}
