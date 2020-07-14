package com.peachkite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	@RequestMapping("/")
	public ModelAndView defaultView(){
		return new ModelAndView("redirect:/imatter");
	}

	@RequestMapping("/imatter")
	public ModelAndView home(){
		return new ModelAndView("index");
	}
}
