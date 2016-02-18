/**
 * 
 */
package br.com.jvoliveira.sourceminer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.jvoliveira.arq.controller.AbstractArqController;
import br.com.jvoliveira.security.domain.User;

/**
 * @author Joao Victor
 *
 */
@Controller
@RequestMapping("/home")
public class HomeController extends AbstractArqController<User>{

	@Override
	@RequestMapping(value = {"/","/index"})
	public String getIndex(Model model){
		return "index";
	}
	
}
