/**
 * 
 */
package br.com.jvoliveira.sourceminer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.jvoliveira.arq.controller.AbstractArqController;
import br.com.jvoliveira.sourceminer.domain.RepositoryConnector;
import br.com.jvoliveira.sourceminer.service.RepositoryConnectorService;


/**
 * @author Joao Victor
 *
 */
@Controller
@RequestMapping("/repository_connector")
public class RepositoryConnectorController extends AbstractArqController<RepositoryConnector>{

	@Autowired
	public RepositoryConnectorController(RepositoryConnectorService service){
		path = "repository";
		title = "Repositório";
		this.service = service;
	}
	
}
