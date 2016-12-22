package com.urent.server;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping({"/", "console"})
public class HelloController {
	@RequestMapping(method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		//model.addAttribute("message", "Hello world!");
		return "index.html";
	}

    @RequestMapping(method = RequestMethod.GET, value = "upload")
    public String testUpload(ModelMap model) {
        //model.addAttribute("message", "Hello world!");
        return "index2.html";
    }
}