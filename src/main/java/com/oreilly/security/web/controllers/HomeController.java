package com.oreilly.security.web.controllers;

import com.oreilly.security.domain.entities.AutoUser;
import com.oreilly.security.domain.repositories.AutoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HomeController {

	@Autowired
	private AutoUserRepository autoUserRepository;

	@RequestMapping(method=RequestMethod.GET)
	public String goHome(){
		return "home";
	}
	
	@RequestMapping("/services")
	public String goServices(){
		return "services";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String goLogin(){
		return "login";
	}

	@RequestMapping("/schedule")
	public String goSchedule(){
		return "schedule";
	}

	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String goRegister(){
		return "register";
	}

	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String register(@ModelAttribute AutoUser autoUser){
		autoUser.setRole("ROLE_USER");

		autoUserRepository.save(autoUser);

		Authentication authentication = new UsernamePasswordAuthenticationToken(autoUser, autoUser.getPassword(),
				autoUser.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return "redirect:/";
	}
}
