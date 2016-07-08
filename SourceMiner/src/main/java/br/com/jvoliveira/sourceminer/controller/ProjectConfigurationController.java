/**
 * 
 */
package br.com.jvoliveira.sourceminer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.jvoliveira.arq.controller.AbstractArqController;
import br.com.jvoliveira.sourceminer.domain.MetricCase;
import br.com.jvoliveira.sourceminer.domain.ProjectConfiguration;
import br.com.jvoliveira.sourceminer.service.MetricCaseService;
import br.com.jvoliveira.sourceminer.service.ProjectConfigurationService;
import br.com.jvoliveira.sourceminer.sync.SyncRepositoryObserver;

/**
 * @author Joao Victor
 *
 */
@Controller
@RequestMapping("/project/configuration")
public class ProjectConfigurationController extends AbstractArqController<ProjectConfiguration>{

	private MetricCaseService metricCaseService;
	
	@Autowired
	public ProjectConfigurationController(ProjectConfigurationService service, MetricCaseService metricCaseService){
		this.path = "project/configuration";
		this.title = "Configurações do Projeto";
		this.service = service;
		this.metricCaseService = metricCaseService;
	}
	
	@Override
	@RequestMapping(value="/edit", method=RequestMethod.POST)
	public String edit(@RequestParam("idObj") Long id, Model model){
		this.obj = ((ProjectConfigurationService)this.service).getProjectConfiguration(id);
		model.addAttribute("obj", this.obj);
		return forward("form");
	}
	
	@Override
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String save(@Validated ProjectConfiguration obj, Errors errors, Model model){
		
		if(errors.hasErrors()){
			model.addAttribute("obj", obj);
			return forward("form");
		}
		
		this.obj = this.service.getOneById(obj.getId());
		
		obj.setCreateAt(this.obj.getCreateAt());
		obj.setProject(this.obj.getProject());
		
		service.persist(obj);
		
		return redirectController("project/index");
	}
	
	@RequestMapping(value="/sync", method=RequestMethod.POST)
	public String synchronize(@RequestParam("idObj") Long idProject, Model model){
		
		//((ProjectConfigurationService)this.service).syncProjectUsingConfiguration(getRepositoryConnectionSession(),idProject);
		((ProjectConfigurationService)this.service).asyncProjectUsingConfiguration(idProject, getRepositoryConnectionSession().getConnection());
		
		return redirectController("project/index");
	}
	
	@ModelAttribute("syncStatus")
	public String getSyncStatus(){
		SyncRepositoryObserver syncObserver = ((ProjectConfigurationService)this.service).getObserver();
		
		if(syncObserver.isInSync())
			return syncObserver.getLabel();
		else
			return "Nenhuma sincronização ativa";
	}
	
	@ModelAttribute("metricCases")
	public List<MetricCase> getAllMetricCase(){
		return metricCaseService.getAll();
	}
}
