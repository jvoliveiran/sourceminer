/**
 * 
 */
package br.com.jvoliveira.sourceminer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.jvoliveira.arq.controller.AbstractArqController;
import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.search.filter.RepositoryItemFilter;
import br.com.jvoliveira.sourceminer.search.filter.RepositoryRevisionFilter;
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
	public String projectDashBoard(@RequestParam Long idProject, Model model, RedirectAttributes redirectAttributes){
		obj = service.getOneById(idProject);
		if(obj.getLastSync() == null){
			addErrorMessage(model, "A primeira sincronização deve ser realizada na área administrativa.");
			redirectAttributes.addFlashAttribute("arqError", getErrorMessages(model));
			return redirectController("/home/index_redirect");
		}
		
		getService().setItemFilter(new RepositoryItemFilter());
		getService().setRevisionFilter(new RepositoryRevisionFilter());
		
		model.addAttribute("itemFilter", getService().getItemFilter());
		model.addAttribute("revisionFilter", getService().getRevisionFilter());
		
		loadDefaultModel(model);
		
		return forward("project_dashboard");
	}
	
	@RequestMapping(value="project_dashboard_details", method=RequestMethod.POST)
	public String projectDashboardDetails(@RequestParam Long idProject, Model model){
		obj = service.getOneById(idProject);
		model.addAttribute("project", obj);
		
		return forward("project_details");
	}
	
	@RequestMapping(value = "/search_item", method = RequestMethod.POST)
	public String searchItem(@Validated RepositoryItemFilter filter, @RequestParam Long idProject, Model model){
		this.obj = service.getOneById(idProject);
		
		getService().searchRepositoryItem(obj, filter);
		model.addAttribute("itemFilter", filter);
		model.addAttribute("revisionFilter", getService().getRevisionFilter());
		loadDefaultModel(model);
		
		return forward("project_dashboard");
	}
	
	@RequestMapping(value = "/clear_search_item", method = RequestMethod.POST)
	public String clearSearchItem(@Validated RepositoryItemFilter filter, @RequestParam Long idProject, Model model){
		this.obj = service.getOneById(idProject);
		
		getService().clearSearchRepositoryItem();
		model.addAttribute("itemFilter", filter);
		model.addAttribute("revisionFilter", getService().getRevisionFilter());
		loadDefaultModel(model);
		
		return forward("project_dashboard");
	}
	
	@RequestMapping(value = "/search_revision", method = RequestMethod.POST)
	public String searchRevision(@Validated RepositoryRevisionFilter filter, @RequestParam Long idProject, Model model){
		this.obj = service.getOneById(idProject);
		
		getService().searchRepositoryRevision(obj, filter);
		model.addAttribute("revisionFilter", filter);
		model.addAttribute("itemFilter", getService().getItemFilter());
		loadDefaultModel(model);
		
		return forward("project_dashboard");
	}
	
	@RequestMapping(value = "/clear_search_revision", method = RequestMethod.POST)
	public String clearSearchRevision(@Validated RepositoryRevisionFilter filter, @RequestParam Long idProject, Model model){
		this.obj = service.getOneById(idProject);
		
		getService().clearSearchRepositoryRevision();
		model.addAttribute("revisionFilter", filter);
		model.addAttribute("itemFilter", getService().getItemFilter());
		loadDefaultModel(model);
		
		return forward("project_dashboard");
	}
	
	private void loadDefaultModel(Model model){
		model.addAttribute("revisions", getService().getAllRevisionsInProject(obj));
		model.addAttribute("itens", getService().getAllItensInProject(obj));
		model.addAttribute("totalItens", getService().getTotalItensInProject(obj));
		model.addAttribute("totalRevisions", getService().getTotalRevisionsInProject(obj));
		model.addAttribute("lastSync", getService().getLastSync(obj));
		model.addAttribute("project", obj);
		model.addAttribute("synchronizations", getService().getAllSyncByProject(obj));
	}
	
	private DashboardService getService(){
		return (DashboardService) this.service;
	}
}
