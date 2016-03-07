/**
 * 
 */
package br.com.jvoliveira.sourceminer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.jvoliveira.arq.controller.AbstractArqController;
import br.com.jvoliveira.security.domain.User;
import br.com.jvoliveira.security.service.UserService;

/**
 * @author joao
 *
 */
@RequestMapping("/user")
@Controller
public class UserController extends AbstractArqController<User> {

	@Autowired
	public UserController(UserService service){
		this.service = service;
		this.path = "user";
		this.title = "Usuário";
	}

	@Override
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String save(@Validated User obj, Errors errors, Model model){
		
		if(errors.hasErrors()){
			model.addAttribute("obj", obj);
			return forward("form");
		}
		
		service.persist(obj);
		
		return redirectController("/login");
	}
}
