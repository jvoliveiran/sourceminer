/**
 * 
 */
package br.com.jvoliveira.sourceminer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.jvoliveira.arq.controller.AbstractArqController;
import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryConnector;
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
	
	@Override
	@RequestMapping(value = {"","/index"})
	public String getIndex(Model model){
		model.addAttribute("list",
				((ProjectService)service).getAllByConnector());
		return forward("index");
	}
	
	@RequestMapping(value="/project_dashboard", method=RequestMethod.POST)
	public String projectDashBoard(@RequestParam Long idProject, Model model){
		obj = service.getOneById(idProject);
		
		model.addAttribute("revisions", ((ProjectService)service).getAllRevisionsInProject(obj));
		model.addAttribute("itens", ((ProjectService)service).getAllItensInProject(obj));
		model.addAttribute("project", obj);
		
		return forward("project_dashboard");
	}
	
	@ModelAttribute("connectors")
	public List<RepositoryConnector> getAllConnectors(){
		return ((ProjectService)service).getAllConnectors();
	}
	
}
