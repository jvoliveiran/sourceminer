package br.com.jvoliveira.sourceminer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller de acesso para as paginas de login
 * @author Joao Victor
 *
 */

@Controller
public class LoginController{

	@RequestMapping(value = {"/","/login"}, method = RequestMethod.GET)
	public String loginPage(){
		return "login/login";
	}
	
	@RequestMapping(value = "/logout")
	public String logoutPage(){
		return "login/login";
	}
	
	
}
