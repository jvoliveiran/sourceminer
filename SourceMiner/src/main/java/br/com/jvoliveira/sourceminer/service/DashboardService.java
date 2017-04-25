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
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.domain.RepositorySyncLog;
import br.com.jvoliveira.sourceminer.neo4j.service.SyncGraphService;
import br.com.jvoliveira.sourceminer.repository.ProjectRepository;
import br.com.jvoliveira.sourceminer.repository.RepositoryItemRepository;
import br.com.jvoliveira.sourceminer.repository.RepositoryRevisionRepository;
import br.com.jvoliveira.sourceminer.repository.SyncLogRepository;
import br.com.jvoliveira.sourceminer.search.RepositoryItemSearch;
import br.com.jvoliveira.sourceminer.search.RepositoryRevisionSearch;
import br.com.jvoliveira.sourceminer.search.filter.RepositoryItemFilter;
import br.com.jvoliveira.sourceminer.search.filter.RepositoryRevisionFilter;

/**
 * @author Joao Victor
 *
 */
@Service
public class DashboardService extends AbstractArqService<Project>{

	private RepositoryConnectionSession connection;
	
	private RepositoryRevisionRepository revisionRepository;
	private RepositoryItemRepository itemRepository;
	private SyncLogRepository syncLogRepository;
	
	private RepositoryItemSearch itemSearch;
	private RepositoryRevisionSearch revisionSearch;
	
	private SyncRepositoryService syncService;
	private SyncGraphService graphService;
	
	@Autowired
	public DashboardService(ProjectRepository repository,  RepositoryConnectionSession connection){
		this.repository = repository;
		this.connection = connection;
	}
	
	public Integer getTotalItensInProject(Project project){
		return itemRepository.countTotalItensByProject(project);
	}
	
	public Integer getTotalRevisionsInProject(Project project){
		return revisionRepository.countTotalItensByProject(project);
	}
	
	public List<RepositoryItem> getAllItensInProject(Project project){
		if(this.connection.getConnection() != null)
			sincronyzeRepositoryDatabase(project);
		
		if(itemSearch.hasResult())
			return itemSearch.getResult();
		else
			return itemRepository.findTop10ByProjectOrderByIdDesc(project);
	}
	
	public List<RepositoryRevision> getAllRevisionsInProject(Project project){
		if(this.connection.getConnection() != null)
			sincronyzeRepositoryDatabase(project);
		
		if(revisionSearch.hasResult())
			return revisionSearch.getResult();
		else
			return revisionRepository.findTop10ByProjectOrderByDateRevisionDesc(project);
	}
	
	public void searchRepositoryItem(Project project, RepositoryItemFilter filter){
		filter.setProject(project);
		itemSearch.setFilter(filter);
		itemSearch.searchWithFilter();
	}
	
	public void searchRepositoryRevision(Project project, RepositoryRevisionFilter filter){
		filter.setProject(project);
		revisionSearch.setFilter(filter);
		revisionSearch.searchWithFilter();
	}
	
	public void clearSearchRepositoryItem(){
		itemSearch.init();
	}
	
	public void clearSearchRepositoryRevision(){
		revisionSearch.init();
	}
	
	public RepositorySyncLog getLastSync(Project project){
		return syncLogRepository.findFirstByProjectOrderByIdDesc(project);
	}
	
	public Boolean isFirstSync(Project project){
		return syncLogRepository.findFirstByProjectOrderByIdDesc(project) != null;
	}
	
	public String getFileContentInRevision(String path, String revision){
		return this.connection.getConnection().getFileContent(path, revision);
	}
	
	private void sincronyzeRepositoryDatabase(Project project){
		syncService.synchronizeRepositoryUsingConfiguration(project);
		graphService.synchronizeGraphUsingConfiguration(project);
	}
	
	public RepositoryRevisionFilter getRevisionFilter(){
		return (RepositoryRevisionFilter) this.revisionSearch.getFilter();
	}
	
	public void setRevisionFilter(RepositoryRevisionFilter filter){
		if(this.revisionSearch.getFilter() == null)
			this.revisionSearch.setFilter(filter);
	}
	
	public RepositoryItemFilter getItemFilter(){
		return (RepositoryItemFilter) this.itemSearch.getFilter();
	}
	
	public void setItemFilter(RepositoryItemFilter filter){
		if(this.itemSearch.getFilter() == null)
			this.itemSearch.setFilter(filter);
	}
	
	@Autowired
	public void setSyncLogRepository(SyncLogRepository syncLogRepository){
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
	
	@Autowired
	public void setItemSearch(RepositoryItemSearch search){
		this.itemSearch = search;
	}
	
	@Autowired
	public void setRevisionSearch(RepositoryRevisionSearch search){
		this.revisionSearch = search;
	}
	
	@Autowired
	public void setSyncRepositoryService(SyncRepositoryService service){
		this.syncService = service;
	}
	
	@Autowired
	public void setSyncGraphService(SyncGraphService graphService){
		this.graphService = graphService;
	}
}
