/**
 * 
 */
package br.com.jvoliveira.sourceminer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.jvoliveira.arq.controller.AbstractArqController;
import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryLocation;
import br.com.jvoliveira.sourceminer.service.ProjectService;

/**
 * @author Joao Victor
 *
 */
@Controller
@RequestMapping("/project")
public class ProjectController extends AbstractArqController<Project>{

	@Autowired
	public ProjectController(ProjectService service){
		this.service = service;
		this.path = "project";
		this.title = "Projeto";
	}
	
	@ModelAttribute("repositories")
	public List<RepositoryLocation> getAllRepositoryLocation(){
		return ((ProjectService)service).getAllRepositoryLocation();
	}
	
}
