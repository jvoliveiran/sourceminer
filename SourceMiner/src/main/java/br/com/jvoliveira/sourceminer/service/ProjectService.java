/**
 * 
 */
package br.com.jvoliveira.sourceminer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jvoliveira.arq.service.AbstractArqService;
import br.com.jvoliveira.arq.utils.DateUtils;
import br.com.jvoliveira.sourceminer.component.RepositoryConnectionSession;
import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryConnector;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.domain.RepositorySyncLog;
import br.com.jvoliveira.sourceminer.repository.ProjectRepository;
import br.com.jvoliveira.sourceminer.repository.RepositoryItemRepository;
import br.com.jvoliveira.sourceminer.repository.RepositoryRevisionRepository;
import br.com.jvoliveira.sourceminer.repository.RepositorySyncLogRepository;

/**
 * @author Joao Victor
 *
 */
@Service
public class ProjectService extends AbstractArqService<Project>{

	private RepositoryConnectionSession connection;
	
	private RepositorySyncLogRepository syncLogRepository;
	private RepositoryRevisionRepository revisionRepository;
	private RepositoryItemRepository itemRepository;
	
	@Autowired
	public ProjectService(ProjectRepository repository, RepositoryConnectionSession connection){
		this.repository = repository;
		this.connection = connection;
	}
	
	public List<Project> getAllByConnector(){
		if(connection.getConnection() != null){
			RepositoryConnector connector = connection.getConnection().getConnector();
			return ((ProjectRepository) repository).findByRepositoryConnector(connector);			
		}
		return null;
	}
	
	public List<RepositoryConnector> getAllConnectors(){
		return getDAO().findAll(RepositoryConnector.class);
	}
	
	public List<RepositoryItem> getAllItensInProject(Project project){
		if(this.connection.getConnection() != null)
			sincronyzeRepositoryDatabase(project);
		
		return itemRepository.findTop10ByProjectOrderByIdDesc(project);
	}
	
	public List<RepositoryRevision> getAllRevisionsInProject(Project project){
		if(this.connection.getConnection() != null)
			sincronyzeRepositoryDatabase(project);
		
		return revisionRepository.findTop10ByProjectOrderByIdDesc(project);
	}
	
	private void sincronyzeRepositoryDatabase(Project project){
		Integer revisionStartSync = 1;
		Integer revisionEndSync = -1;
		
		Long revisionLastCommit = connection.getConnection().getLastRevisionNumber(project);
		
		RepositorySyncLog lastSyncLog = syncLogRepository.findByProjectOrderByIdDesc(project);
		
		if(lastSyncLog != null){
			Long revisionLastSync = lastSyncLog.getHeadRevision();
			
			if(revisionLastSync < revisionLastCommit)
				revisionStartSync = revisionLastSync.intValue() + 1;
			else
				return;
		}
		
		sincronyzeRevisionsDatabase(project,revisionStartSync,revisionEndSync);
		sincronyzeFilesDatabase(project,revisionStartSync,revisionLastCommit.intValue());
		refreshSyncLog(lastSyncLog, project);
	}

	private void sincronyzeRevisionsDatabase(Project project, Integer revisionStartSync, Integer revisionEndSync){
		List<RepositoryRevision> repositoryRevisions = connection.getConnection().
												getRevisionsInRange(project, revisionStartSync, revisionEndSync);
		
		for(RepositoryRevision revision : repositoryRevisions){
			revision.setCreateAt(DateUtils.now());
			revision.setProject(project);
			this.revisionRepository.save(revision);
		}
	}
	
	private void sincronyzeFilesDatabase(Project project, Integer revisionStartSync, Integer revisionEndSync) {
		List<RepositoryItem> repositoryItens = connection.getConnection().
				getItensInRevision(project, revisionStartSync, revisionEndSync);
		
		for(RepositoryItem item : repositoryItens){
			item.setCreateAt(DateUtils.now());
			item.setProject(project);
			itemRepository.save(item);
		}
	}
	
	private void refreshSyncLog(RepositorySyncLog lastSyncLog, Project project) {
		lastSyncLog = new RepositorySyncLog();
		lastSyncLog.setCreateAt(DateUtils.now());
		lastSyncLog.setProject(project);
		lastSyncLog.setHeadRevision(connection.getConnection().getLastRevisionNumber(project));
		
		syncLogRepository.save(lastSyncLog);
	}
	
	@Autowired
	public void setSyncLogRepository(RepositorySyncLogRepository syncLogRepository){
		this.syncLogRepository = syncLogRepository;
	}
	
	@Autowired
	public void setRevisionRepository(RepositoryRevisionRepository revisionRepository){
		this.revisionRepository = revisionRepository;
	}
	
	@Autowired
	public void setItemRepository(RepositoryItemRepository itemRepository){
		this.itemRepository = itemRepository;
	}
}
