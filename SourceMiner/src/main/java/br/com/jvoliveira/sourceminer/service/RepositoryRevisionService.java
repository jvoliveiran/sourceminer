/**
 * 
 */
package br.com.jvoliveira.sourceminer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jvoliveira.arq.service.AbstractArqService;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.repository.RepositoryRevisionRepository;

/**
 * @author Joao Victor
 *
 */
@Service
public class RepositoryRevisionService extends AbstractArqService<RepositoryRevision>{

	@Autowired
	public RepositoryRevisionService(RepositoryRevisionRepository repository) {
		this.repository = repository;
	}
	
	public RepositoryRevision getRevisionById(Long id){
		return this.repository.findOne(id);
	}
}
