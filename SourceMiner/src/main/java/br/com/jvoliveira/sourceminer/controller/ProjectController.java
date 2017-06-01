/**
 * 
 */
package br.com.jvoliveira.sourceminer.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	public String getSyncStatusModel(){
		return getSyncStatus();
	}
	
	@ResponseBody
	@RequestMapping(value="/sync_status", method=RequestMethod.GET, produces = "application/json")
	public String getSyncStatusAjax() throws JsonProcessingException{
		Map<String,String> messages = getCompleteSyncStatus();
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(messages);
	}
	
	private String getSyncStatus(){
		SyncRepositoryObserver syncObserver = projectConfigService.getObserver();
		
		if(syncObserver.isInSync())
			return syncObserver.getLabel();
		else
			return "Nenhuma sincronização ativa";
	}
	
	private Map<String,String> getCompleteSyncStatus(){
		Map<String,String> messages = new HashMap<>();
		SyncRepositoryObserver syncObserver = projectConfigService.getObserver();
		
		if(syncObserver.isInSync()){
			messages.put("label", syncObserver.getLabel());
			messages.put("subLabel", syncObserver.getSubLabel());
		}else{
			messages.put("label", "Nenhuma sincronização ativa");
			messages.put("subLabel", "");
		}
		return messages;
	}
	
	@Autowired
	public void setProjectConfigurationService(ProjectConfigurationService service){
		this.projectConfigService = service;
	}
}
