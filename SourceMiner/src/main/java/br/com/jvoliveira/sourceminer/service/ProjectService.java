/**
 * 
 */
package br.com.jvoliveira.sourceminer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jvoliveira.arq.service.AbstractArqService;
import br.com.jvoliveira.sourceminer.component.repositoryconnection.RepositoryConnectionSession;
import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryLocation;
import br.com.jvoliveira.sourceminer.repository.ProjectRepository;

/**
 * @author Joao Victor
 *
 */
@Service
public class ProjectService extends AbstractArqService<Project>{

	private RepositoryConnectionSession connection;
	
	@Autowired
	public ProjectService(ProjectRepository repository, RepositoryConnectionSession connection){
		this.repository = repository;
		this.connection = connection;
	}
	
	public List<Project> getAllByRepositoryLocation(){
		if(connection.getConnection() != null){
			RepositoryLocation location = connection.getConnection().getConnector().getRepositoryLocation();
			return ((ProjectRepository) repository).findByRepositoryLocation(location);			
		}
		return null;
	}
	
	public List<RepositoryLocation> getAllRepositoryLocation(){
		return getDAO().findAll(RepositoryLocation.class);
	}
	
}
