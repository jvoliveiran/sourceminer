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
import br.com.jvoliveira.sourceminer.service.ProjectConfigurationService;
import br.com.jvoliveira.sourceminer.service.ProjectService;
import br.com.jvoliveira.sourceminer.sync.SyncRepositoryObserver;

/**
 * @author Joao Victor
 *
 */
@Controller
@RequestMapping("/project")
public class ProjectController extends AbstractArqController<Project>{

	private ProjectConfigurationService projectConfigService;
	
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
	
	@ModelAttribute("syncStatus")
	public String getSyncStatus(){
		SyncRepositoryObserver syncObserver = projectConfigService.getObserver();
		
		if(syncObserver.isInSync())
			return syncObserver.getLabel();
		else
			return "Nenhuma sincronização ativa";
	}
	
	@Autowired
	public void setProjectConfigurationService(ProjectConfigurationService service){
		this.projectConfigService = service;
	}
}
