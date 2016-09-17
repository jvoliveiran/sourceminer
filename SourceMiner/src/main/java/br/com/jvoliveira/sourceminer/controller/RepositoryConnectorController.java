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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.jvoliveira.arq.controller.AbstractArqController;
import br.com.jvoliveira.sourceminer.domain.RepositoryConnector;
import br.com.jvoliveira.sourceminer.domain.RepositoryLocation;
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
	public String startConnection(@PathVariable Long idConnector, Model model, RedirectAttributes redirectAttributes){
		((RepositoryConnectorService) service).startConnectionWithRepository(idConnector);
		
		redirectAttributes.addFlashAttribute("arqError", getErrorMessages(model));
		return redirectController("/home/index_redirect");
	}
	
	@Override
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String save(@Validated RepositoryConnector obj, Errors errors, Model model){
		
		if(errors.hasErrors()){
			model.addAttribute("obj", obj);
			return forward("form");
		}
		
		service.persist(obj);
		
		return redirectController("/home/index");
	}
	
	@RequestMapping("/close_connection")
	public String closeConnection(){
		((RepositoryConnectorService) service).closeConnection(this.repositoryConnetionSession);
		return redirectController("/home/index");
	}
}
