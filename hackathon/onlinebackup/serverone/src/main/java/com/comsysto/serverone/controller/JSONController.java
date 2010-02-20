package com.comsysto.serverone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author danielbartl
 *
 */
@Controller
public class JSONController {

	@RequestMapping("hello")
	public String test(){
		return "Hello!";
	}
	
}
