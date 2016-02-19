/**
 * 
 */
package br.com.jvoliveira.sourceminer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jvoliveira.arq.service.AbstractArqService;
import br.com.jvoliveira.sourceminer.domain.RepositoryConnector;
import br.com.jvoliveira.sourceminer.repository.RepositoryConnectorRepository;

/**
 * @author Joao Victor
 *
 */
@Service
public class RepositoryConnectorService 
	extends AbstractArqService<RepositoryConnector>{

	@Autowired
	public RepositoryConnectorService(RepositoryConnectorRepository repository){
		this.repository = repository;
	}
	
}
