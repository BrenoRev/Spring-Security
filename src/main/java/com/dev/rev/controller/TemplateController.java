package com.dev.rev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class TemplateController {

	@GetMapping("login")
	public String getLoginView() {
		return "login";
	}
	
	@GetMapping
	public String getIndexView() {
		return "index";
	}
	
	@GetMapping("/courses")
	public String getStartView() {
		return "courses";
	}
	
}
