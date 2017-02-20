/**
 * 
 */
package br.com.jvoliveira.sourceminer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import br.com.jvoliveira.arq.service.AbstractArqService;
import br.com.jvoliveira.sourceminer.component.repositoryconnection.RepositoryConnection;
import br.com.jvoliveira.sourceminer.component.repositoryconnection.RepositoryConnectionSession;
import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.ProjectConfiguration;
import br.com.jvoliveira.sourceminer.domain.RepositoryLocation;
import br.com.jvoliveira.sourceminer.neo4j.service.SyncGraphService;
import br.com.jvoliveira.sourceminer.repository.ProjectConfigurationRepository;
import br.com.jvoliveira.sourceminer.sync.SyncRepositoryObserver;

/**
 * @author Joao Victor
 *
 */
@Service
public class ProjectConfigurationService extends AbstractArqService<ProjectConfiguration>{

	private SyncRepositoryService syncService;	
	private SyncGraphService graphService;
	private SyncRepositoryObserver observer;
	
	@Autowired
	public ProjectConfigurationService(ProjectConfigurationRepository repository){
		this.repository = repository;
		observer = new SyncRepositoryObserver();
	}
	
	public ProjectConfiguration getProjectConfiguration(Long idProject){
		Project project = getDAO().findByPrimaryKey(idProject, Project.class);
		return ((ProjectConfigurationRepository)repository).findOneByProject(project);
	}
	
	public void syncProjectUsingConfiguration(RepositoryConnectionSession connSession, Long idProject) {
		Project project = getDAO().findByPrimaryKey(idProject, Project.class);
		
		if(validateSyncUsingConfiguration(connSession,project))
			syncService.synchronizeRepositoryUsingConfigurationObserver(project,observer,connSession.getConnection());
		
	}
	
	@Async
	public void asyncProjectUsingConfiguration(Long idProject, RepositoryConnection connection){
		Project project = getDAO().findByPrimaryKey(idProject, Project.class);
		
		//TODO: Refatorar método de validação de conexão aberta. Não usar bean de sessão
		syncService.synchronizeRepositoryUsingConfigurationObserver(project,observer,connection);
		
		graphService.syncGraphUsingConfigurationObserver(project, observer, connection);
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
	
	@Autowired
	public void setSyncGraphService(SyncGraphService graphService){
		this.graphService = graphService;
	}
	
	public SyncRepositoryObserver getObserver() {
		return observer;
	}
}
