/**
 * 
 */
package br.com.jvoliveira.sourceminer.service;

import java.util.List;

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
	
	public List<RepositoryConnector> getAllByUser(){
		return ((RepositoryConnectorRepository) repository).findByUser(getUsuarioLogado());
	}
	
	@Override
	protected RepositoryConnector beforeCreate(RepositoryConnector obj) {
		obj.setUser(getUsuarioLogado());
		return obj;
	}
	
	@Override
	protected RepositoryConnector beforeUpdate(RepositoryConnector obj) {
		obj.setUser(getUsuarioLogado());
		return obj;
	}
}
