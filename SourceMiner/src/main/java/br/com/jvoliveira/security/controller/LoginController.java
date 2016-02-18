package br.com.jvoliveira.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.jvoliveira.arq.controller.AbstractArqController;

/**
 * Controller de acesso para as paginas de login
 * @author Joao Victor
 *
 */

@Controller
public class LoginController extends AbstractArqController {

	@RequestMapping(value = {"/","/login"}, method = RequestMethod.GET)
	public String loginPage(){
		return "login/login";
	}
	
	@RequestMapping(value = "/logout")
	public String logoutPage(){
		return "login/login";
	}
}
