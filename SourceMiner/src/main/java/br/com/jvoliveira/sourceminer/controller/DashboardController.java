/**
 * 
 */
package br.com.jvoliveira.sourceminer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.jvoliveira.arq.controller.AbstractArqController;
import br.com.jvoliveira.sourceminer.component.javaparser.ChangeLogGroupModel;
import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
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
	public String projectDashBoard(@RequestParam Long idProject, Model model){
		obj = service.getOneById(idProject);
		
		((DashboardService)service).setItemFilter(new RepositoryItemFilter());
		((DashboardService)service).setRevisionFilter(new RepositoryRevisionFilter());
		
		model.addAttribute("itemFilter", ((DashboardService)service).getItemFilter());
		model.addAttribute("revisionFilter", ((DashboardService)service).getRevisionFilter());
		
		loadDefaultModel(model);
		
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
		List<ChangeLogGroupModel> historyChangeLog = ((DashboardService)service).getChangeLogInRepositoryItem(item);
		
		model.addAttribute("item", item);
		model.addAttribute("project", item.getProject());
		model.addAttribute("fileContent", fileContent);
		model.addAttribute("revisionsNumber", item.getAllRevisionsNumber());
		model.addAttribute("historyChangeLog", historyChangeLog);
		
		return forward("item_details");
	}
	
	@RequestMapping(value = "/search_item", method = RequestMethod.POST)
	public String searchItem(@Validated RepositoryItemFilter filter, @RequestParam Long idProject, Model model){
		this.obj = service.getOneById(idProject);
		
		((DashboardService)service).searchRepositoryItem(obj, filter);
		
		model.addAttribute("itemFilter", filter);
		model.addAttribute("revisionFilter", ((DashboardService)service).getRevisionFilter());
		
		loadDefaultModel(model);
		
		return forward("project_dashboard");
	}
	
	@RequestMapping(value = "/clear_search_item", method = RequestMethod.POST)
	public String clearSearchItem(@Validated RepositoryItemFilter filter, @RequestParam Long idProject, Model model){
		this.obj = service.getOneById(idProject);
		
		((DashboardService)service).clearSearchRepositoryItem();
		model.addAttribute("itemFilter", filter);
		model.addAttribute("revisionFilter", ((DashboardService)service).getRevisionFilter());
		
		loadDefaultModel(model);
		
		return forward("project_dashboard");
	}
	
	@RequestMapping(value = "/search_revision", method = RequestMethod.POST)
	public String searchRevision(@Validated RepositoryRevisionFilter filter, @RequestParam Long idProject, Model model){
		this.obj = service.getOneById(idProject);
		
		((DashboardService)service).searchRepositoryRevision(obj, filter);
		model.addAttribute("revisionFilter", filter);
		model.addAttribute("itemFilter", ((DashboardService)service).getItemFilter());
		
		loadDefaultModel(model);
		
		return forward("project_dashboard");
	}
	
	@RequestMapping(value = "/clear_search_revision", method = RequestMethod.POST)
	public String clearSearchRevision(@Validated RepositoryRevisionFilter filter, @RequestParam Long idProject, Model model){
		this.obj = service.getOneById(idProject);
		
		((DashboardService)service).clearSearchRepositoryRevision();
		model.addAttribute("revisionFilter", filter);
		model.addAttribute("itemFilter", ((DashboardService)service).getItemFilter());
		
		loadDefaultModel(model);
		
		return forward("project_dashboard");
	}
	
	@ResponseBody
	@RequestMapping(value = "/file_content_revision", method = RequestMethod.POST)
	public String getFileContentInRevision(@RequestParam Long revisionNumber, @RequestParam String path){
		return ((DashboardService)service).getFileContentInRevision(path, revisionNumber);
	}
	
	private void loadDefaultModel(Model model){
		model.addAttribute("revisions", ((DashboardService)service).getAllRevisionsInProject(obj));
		model.addAttribute("itens", ((DashboardService)service).getAllItensInProject(obj));
		model.addAttribute("totalItens", ((DashboardService)service).getTotalItensInProject(obj));
		model.addAttribute("totalRevisions", ((DashboardService)service).getTotalRevisionsInProject(obj));
		model.addAttribute("lastSync", ((DashboardService)service).getLastSync(obj));
		model.addAttribute("project", obj);
	}
	
}
