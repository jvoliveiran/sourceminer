/**
 * 
 */
package br.com.jvoliveira.sourceminer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.jvoliveira.arq.controller.AbstractArqController;
import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.service.DashboardService;

/**
 * @author Joao Victor
 *
 */

@Controller
@RequestMapping("/dashboard")
public class DashboardController extends AbstractArqController<Project>{

	@Autowired
	public DashboardController(DashboardService service){
		this.service = service;
		this.path = "dashboard";
		this.title = "Dashboard";
	}
	
	@RequestMapping(value="/project_dashboard", method=RequestMethod.POST)
	public String projectDashBoard(@RequestParam Long idProject, Model model){
		obj = service.getOneById(idProject);
		
		model.addAttribute("revisions", ((DashboardService)service).getAllRevisionsInProject(obj));
		model.addAttribute("itens", ((DashboardService)service).getAllItensInProject(obj));
		model.addAttribute("project", obj);
		
		return forward("project_dashboard");
	}
	
	@RequestMapping(value = "/revision_details", method = RequestMethod.POST)
	public String revisionDetails(@RequestParam Long idRevision, Model model){
		
		RepositoryRevision revision = ((DashboardService)service).getRevisionById(idRevision);
		
		model.addAttribute("revision", revision);
		model.addAttribute("project", revision.getProject());
		
		return forward("revision_details");
	}
	
	@RequestMapping(value = "/item_details", method = RequestMethod.POST)
	public String itemDetails(@RequestParam Long idItem, Model model){
		
		RepositoryItem item = ((DashboardService)service).getItemById(idItem);
		String fileContent = ((DashboardService)service).getFileContentInRevision(item.getPath(), new Long(-1));
		
		model.addAttribute("item", item);
		model.addAttribute("project", item.getProject());
		model.addAttribute("fileContent", fileContent);
		
		return forward("item_details");
	}
}
