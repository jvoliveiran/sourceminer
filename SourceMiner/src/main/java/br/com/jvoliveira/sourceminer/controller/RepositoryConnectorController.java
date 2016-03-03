/**
 * 
 */
package br.com.jvoliveira.sourceminer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.jvoliveira.arq.controller.AbstractArqController;
import br.com.jvoliveira.sourceminer.domain.RepositoryConnector;
import br.com.jvoliveira.sourceminer.domain.RepositoryLocation;
import br.com.jvoliveira.sourceminer.domain.enums.RepositoryLocationType;
import br.com.jvoliveira.sourceminer.service.RepositoryConnectorService;
import br.com.jvoliveira.sourceminer.service.RepositoryLocationService;


/**
 * @author Joao Victor
 *
 */
@Controller
@RequestMapping("/repository_connector")
public class RepositoryConnectorController extends AbstractArqController<RepositoryConnector>{

	private RepositoryLocationService locationService;
	
	@Autowired
	public RepositoryConnectorController(RepositoryConnectorService service,
			RepositoryLocationService locationService){
		path = "repository_connector";
		title = "Repositório";
		this.service = service;
		this.locationService = locationService;
	}
	
	@ModelAttribute("repositoryLocations")
	public List<RepositoryLocation> getRepositoryLocationList(){
		return locationService.getAllEnable();
	}
	
	@Override
	@RequestMapping(value = {"","/index"})
	public String getIndex(Model model){
		model.addAttribute("list",((RepositoryConnectorService)service).getAllByUser());
		return forward("index");
	}
	
	@RequestMapping("/start_connection/{idConnector}")
	public String startConnection(@PathVariable Long idConnector){
		((RepositoryConnectorService) service).startConnectionWithRepository(idConnector);
		
		return redirect("index");
	}
	
	@RequestMapping("/close_connection")
	public String closeConnection(){
		((RepositoryConnectorService) service).closeConnection(this.repositoryConnetionSession);
		return redirect("index");
	}
}
