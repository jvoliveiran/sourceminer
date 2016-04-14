/**
 * 
 */
package br.com.jvoliveira.sourceminer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jvoliveira.arq.service.AbstractArqService;
import br.com.jvoliveira.arq.utils.DateUtils;
import br.com.jvoliveira.sourceminer.component.repositoryconnection.RepositoryConnectionSession;
import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.ProjectConfiguration;
import br.com.jvoliveira.sourceminer.domain.RepositoryLocation;
import br.com.jvoliveira.sourceminer.repository.ProjectConfigurationRepository;
import br.com.jvoliveira.sourceminer.repository.ProjectRepository;

/**
 * @author Joao Victor
 *
 */
@Service
public class ProjectService extends AbstractArqService<Project>{

	private RepositoryConnectionSession connection;
	
	private ProjectConfigurationRepository projectConfigurationRepository;
	
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
	
	@Override
	public Project persist(Project obj) {
		obj = super.persist(obj);
		
		ProjectConfiguration projectConfiguration = new ProjectConfiguration(obj);
		
		projectConfiguration.setCreateAt(DateUtils.now());
		this.projectConfigurationRepository.save(projectConfiguration);
		
		return obj;
	}
	
	@Autowired
	public void setProjectConfigurationRepository(ProjectConfigurationRepository repository){
		this.projectConfigurationRepository = repository;
	}
}
