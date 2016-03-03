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
import br.com.jvoliveira.sourceminer.domain.RepositoryLocation;
import br.com.jvoliveira.sourceminer.domain.enums.RepositoryLocationType;
import br.com.jvoliveira.sourceminer.domain.enums.RepositoryVersionManager;
import br.com.jvoliveira.sourceminer.service.RepositoryLocationService;

/**
 * @author Joao Victor
 *
 */
@Controller
@RequestMapping("/repository_location")
public class RepositoryLocationController extends AbstractArqController<RepositoryLocation>{

	@Autowired
	public RepositoryLocationController(RepositoryLocationService service){
		this.service = service;
		this.path = "repository_location";
		this.title = "Repositório";
	}
	
	@ModelAttribute("repositoryLocations")
	public List<RepositoryLocationType> getRepositoryLocationList(){
		return RepositoryLocationType.getValues();
	}
	
	@ModelAttribute("versionManagers")
	public List<RepositoryVersionManager> getVersionManagerList(){
		return RepositoryVersionManager.getValues();
	}
	
}
