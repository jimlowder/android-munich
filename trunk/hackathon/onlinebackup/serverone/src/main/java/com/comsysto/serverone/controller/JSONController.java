package com.comsysto.serverone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author danielbartl
 * 
 */
@Controller
@RequestMapping("/json")
public class JSONController {

	@RequestMapping("hello")
	public String hello() {
		throw new IllegalStateException("Not implemented yet");
	}

}
