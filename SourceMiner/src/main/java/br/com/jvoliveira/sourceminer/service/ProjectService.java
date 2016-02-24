/**
 * 
 */
package br.com.jvoliveira.sourceminer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jvoliveira.arq.service.AbstractArqService;
import br.com.jvoliveira.sourceminer.component.RepositoryConnectionSession;
import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryConnector;
import br.com.jvoliveira.sourceminer.repository.ProjectRepository;

/**
 * @author Joao Victor
 *
 */
@Service
public class ProjectService extends AbstractArqService<Project>{

	@Autowired
	public ProjectService(ProjectRepository repository){
		this.repository = repository;
	}
	
	public List<Project> getAllByConnector(RepositoryConnectionSession connection){
		if(connection.getConnection() != null){
			RepositoryConnector connector = connection.getConnection().getConnector();
			return ((ProjectRepository) repository).findByRepositoryConnector(connector);			
		}
		return null;
	}
	
	public List<RepositoryConnector> getAllConnectors(){
		return getDAO().findAll(RepositoryConnector.class);
	}
	
}
