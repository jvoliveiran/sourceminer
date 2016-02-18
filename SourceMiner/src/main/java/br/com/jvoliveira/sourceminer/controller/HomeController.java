/**
 * 
 */
package br.com.jvoliveira.sourceminer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.jvoliveira.arq.controller.AbstractArqController;

/**
 * @author Joao Victor
 *
 */
@Controller
@RequestMapping("/home")
public class HomeController extends AbstractArqController {

	@RequestMapping("/index")
	public String indexPage(){
		return "index";
	}
	
}
