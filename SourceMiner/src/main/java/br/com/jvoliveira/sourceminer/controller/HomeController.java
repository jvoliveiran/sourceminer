/**
 * 
 */
package br.com.jvoliveira.sourceminer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.jvoliveira.arq.controller.AbstractArqController;
import br.com.jvoliveira.sourceminer.domain.User;
import br.com.jvoliveira.sourceminer.service.ProjectService;
import br.com.jvoliveira.sourceminer.service.RepositoryConnectorService;

/**
 * @author Joao Victor
 *
 */
@Controller
@RequestMapping("/home")
public class HomeController extends AbstractArqController<User>{
	
	private RepositoryConnectorService connectorService;
	private ProjectService projectService;

	@Override
	@RequestMapping(value = {"/","/index"})
	public String getIndex(Model model){
		model.addAttribute("listConnectors",connectorService.getAllByUser());
		model.addAttribute("listProject",projectService.getAllByRepositoryLocation());
		return "index";
	}
	
	@RequestMapping(value = {"/index_redirect"})
	public String getIndexRedirection(Model model, 
			@ModelAttribute("arqError") final Object arqError){
		model.addAttribute("arqErros",arqError);
		model.addAttribute("listConnectors",connectorService.getAllByUser());
		model.addAttribute("listProject",projectService.getAllByRepositoryLocation());
		return "index";
	}
	
	@Autowired
	public void setConnectorService(RepositoryConnectorService connectorService){
		this.connectorService = connectorService;
	}
	
	@Autowired
	public void setProjectService(ProjectService projectService){
		this.projectService = projectService;
	}
}
