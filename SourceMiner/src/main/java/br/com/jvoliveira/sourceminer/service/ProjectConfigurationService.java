/**
 * 
 */
package br.com.jvoliveira.sourceminer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jvoliveira.arq.service.AbstractArqService;
import br.com.jvoliveira.sourceminer.component.repositoryconnection.RepositoryConnectionSession;
import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.ProjectConfiguration;
import br.com.jvoliveira.sourceminer.domain.RepositoryLocation;
import br.com.jvoliveira.sourceminer.repository.ProjectConfigurationRepository;

/**
 * @author Joao Victor
 *
 */
@Service
public class ProjectConfigurationService extends AbstractArqService<ProjectConfiguration>{

	private SyncRepositoryService syncService;
	
	@Autowired
	public ProjectConfigurationService(ProjectConfigurationRepository repository){
		this.repository = repository;
	}
	
	public ProjectConfiguration getProjectConfiguration(Long idProject){
		Project project = getDAO().findByPrimaryKey(idProject, Project.class);
		return ((ProjectConfigurationRepository)repository).findOneByProject(project);
	}
	
	public void syncProjectUsingConfiguration(RepositoryConnectionSession connSession, Long idProject) {
		Project project = getDAO().findByPrimaryKey(idProject, Project.class);
		
		if(validateSyncUsingConfiguration(connSession,project))
			syncService.synchronizeRepositoryUsingConfiguration(project);
	}
	
	private boolean validateSyncUsingConfiguration(RepositoryConnectionSession connSession, Project project){
		
		if(!connSession.isConnectionOpened())
			return false;
		
		RepositoryLocation location = connSession.getConnection().getConnector().getRepositoryLocation();
		if(location.getId() != project.getRepositoryLocation().getId())
			return false;
		
		return true;
	}
	
	@Autowired
	public void setSyncService(SyncRepositoryService syncService){
		this.syncService = syncService;
	}
	
}
