/**
 * 
 */
package br.com.jvoliveira.sourceminer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jvoliveira.arq.service.AbstractArqService;
import br.com.jvoliveira.arq.utils.DateUtils;
import br.com.jvoliveira.sourceminer.component.javaparser.ChangeLogGroupModel;
import br.com.jvoliveira.sourceminer.component.javaparser.ClassParserHelper;
import br.com.jvoliveira.sourceminer.component.repositoryconnection.RepositoryConnectionSession;
import br.com.jvoliveira.sourceminer.domain.ItemAsset;
import br.com.jvoliveira.sourceminer.domain.ItemChangeLog;
import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevisionItem;
import br.com.jvoliveira.sourceminer.domain.RepositorySyncLog;
import br.com.jvoliveira.sourceminer.repository.ItemAssetRepository;
import br.com.jvoliveira.sourceminer.repository.ItemChageLogRepositoryImpl;
import br.com.jvoliveira.sourceminer.repository.ProjectRepository;
import br.com.jvoliveira.sourceminer.repository.RepositoryItemRepository;
import br.com.jvoliveira.sourceminer.repository.RepositoryRevisionItemRepository;
import br.com.jvoliveira.sourceminer.repository.RepositoryRevisionRepository;
import br.com.jvoliveira.sourceminer.repository.RepositorySyncLogRepository;
import br.com.jvoliveira.sourceminer.search.ItemChangeLogSearch;
import br.com.jvoliveira.sourceminer.search.RepositoryItemSearch;
import br.com.jvoliveira.sourceminer.search.RepositoryRevisionSearch;
import br.com.jvoliveira.sourceminer.search.filter.ItemChangeLogFilter;
import br.com.jvoliveira.sourceminer.search.filter.RepositoryItemFilter;
import br.com.jvoliveira.sourceminer.search.filter.RepositoryRevisionFilter;

/**
 * @author Joao Victor
 *
 */
@Service
public class DashboardService extends AbstractArqService<Project>{

	private RepositoryConnectionSession connection;
	
	private RepositorySyncLogRepository syncLogRepository;
	private RepositoryRevisionRepository revisionRepository;
	private RepositoryItemRepository itemRepository;
	private RepositoryRevisionItemRepository revisionItemRepository;
	private ItemAssetRepository itemAssetRepository;
	private ItemChageLogRepositoryImpl itemChangeLogRepository;
	
	private RepositoryItemSearch itemSearch;
	private RepositoryRevisionSearch revisionSearch;
	private ItemChangeLogSearch itemChangeLogSearch;
	
	private ClassParserHelper classParserHelper;
	
	@Autowired
	public DashboardService(ProjectRepository repository, 
			RepositoryConnectionSession connection){
		this.repository = repository;
		this.connection = connection;
		this.classParserHelper = new ClassParserHelper();
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
	
	public List<RepositoryRevision> getAllRevisionsInProject(Project project){
		if(this.connection.getConnection() != null)
			sincronyzeRepositoryDatabase(project);
		
		if(revisionSearch.hasResult())
			return revisionSearch.getResult();
		else
			return revisionRepository.findTop10ByProjectOrderByIdDesc(project);
	}
	
	public RepositorySyncLog getLastSync(Project project){
		return syncLogRepository.findFirstByProjectOrderByIdDesc(project);
	}
	
	private void sincronyzeRepositoryDatabase(Project project){
		Integer revisionStartSync = 1;
		Integer revisionEndSync = -1;
		
		Long revisionLastCommit = connection.getConnection().getLastRevisionNumber(project);
		
		RepositorySyncLog lastSyncLog = getLastSync(project);
		
		if(lastSyncLog != null){
			Long revisionLastSync = lastSyncLog.getHeadRevision();
			
			if(revisionLastSync < revisionLastCommit)
				revisionStartSync = revisionLastSync.intValue() + 1;
			else
				return;
		}
		
		sincronyzeRepositoryDatabase(project,revisionStartSync,revisionEndSync);
		refreshSyncLog(lastSyncLog, project);
	}
	
	private void sincronyzeRepositoryDatabase(Project project, Integer revisionStartSync, Integer revisionEndSync) {
		List<RepositoryRevisionItem> repositoryItens = connection.getConnection().
				getRevisionItensInProjectRange(project, revisionStartSync, revisionEndSync);
		
		for(RepositoryRevisionItem item : repositoryItens){
			item.setCreateAt(DateUtils.now());
			item.setProject(project);
			item.setRepositoryRevision(getSyncRevision(project,item.getRepositoryRevision()));
			item.setRepositoryItem(getSyncItem(project, item.getRepositoryItem()));
			revisionItemRepository.save(item);
			
			processRepositoryItemChanges(item);
		}
	}
	
	private void processRepositoryItemChanges(RepositoryRevisionItem revisionItem) {
		Long revisionNumber = revisionItem.getRepositoryRevision().getRevision();
		RepositoryItem item = revisionItem.getRepositoryItem();
		
		List<ItemAsset> actualItemAssets = itemAssetRepository.findByRepositoryItemAndEnable(item,true);
		
		actualItemAssets.stream().forEach(asset -> asset.setNewAsset(true));
		
		String fileContent = getFileContentInRevision(item.getPath(), revisionNumber);
		
		List<ItemAsset> assetsToSync = classParserHelper.generateActualClassAssets(actualItemAssets, fileContent);
		
		syncAssets(assetsToSync, revisionItem);
	}

	private void syncAssets(List<ItemAsset> assetsToSync, RepositoryRevisionItem revisionItem) {
		for(ItemAsset asset : assetsToSync){
			
			if(asset.getId() == null){
				asset.setCreateAt(DateUtils.now());
				asset.setRepositoryItem(revisionItem.getRepositoryItem());
				itemAssetRepository.save(asset);
			}else if(asset.getItemChageLog().getChangeType().isDelete()){
				asset.setUpdateAt(DateUtils.now());
				asset.setEnable(false);
				itemAssetRepository.save(asset);
			}else if(asset.getItemChageLog().getChangeType().isUpdate()){
				asset.setUpdateAt(DateUtils.now());
				itemAssetRepository.save(asset);
			}
			
			ItemChangeLog itemChange = asset.getItemChageLog();
			if(itemChange != null){
				itemChange.setName(asset.getName());
				itemChange.setSignature(asset.getSignature());
				itemChange.setAsset(asset);
				itemChange.setCreateAt(DateUtils.now());
				itemChange.setRevisionItem(revisionItem);
				
				itemChangeLogRepository.save(itemChange);
			}
		}
	}

	private RepositoryRevision getSyncRevision(Project project, RepositoryRevision revision){
		Long revisionNumber = revision.getRevision();
		RepositoryRevision revisionFromDB = revisionRepository.findByProjectAndRevision(project,revisionNumber);
		
		if(revisionFromDB != null)
			return revisionFromDB;
		else{
			revision.setProject(project);
			revision.setCreateAt(DateUtils.now());
			revision = revisionRepository.save(revision);
			return revision;
		}
	}
	
	private RepositoryItem getSyncItem(Project project, RepositoryItem item){
		String itemPath = item.getPath();
		String itemName = item.getName();
		RepositoryItem itemFromDB = itemRepository.findByPathAndName(itemPath, itemName);
		
		if(itemFromDB != null)
			return itemFromDB;
		else{
			item.setProject(project);
			item.setCreateAt(DateUtils.now());
			item = itemRepository.save(item);
			return item;
		}
	}
	
	private void refreshSyncLog(RepositorySyncLog lastSyncLog, Project project) {
		lastSyncLog = new RepositorySyncLog();
		lastSyncLog.setCreateAt(DateUtils.now());
		lastSyncLog.setProject(project);
		lastSyncLog.setHeadRevision(connection.getConnection().getLastRevisionNumber(project));
		
		syncLogRepository.save(lastSyncLog);
	}
	
	public RepositoryRevision getRevisionById(Long id){
		return revisionRepository.findOne(id);
	}
	
	public RepositoryItem getItemById(Long id){
		return itemRepository.findOne(id);
	}
	
	public String getFileContentInRevision(String path, Long revision){
		return this.connection.getConnection().getFileContent(path, revision);
	}
	
	public List<ChangeLogGroupModel> getChangeLogInRepositoryItem(RepositoryItem item){
		List<ItemChangeLog> changeLogs = this.itemChangeLogRepository.findAllChangeLogRepositoryItem(item);
		
		//TODO: Incluir tratamento do ItemChangeLogSearch
		return classParserHelper.getChangeLogGroupByRevision(changeLogs);
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
	
	public ItemChangeLogFilter getItemChangeLogFilter(){
		return (ItemChangeLogFilter) this.itemChangeLogSearch.getFilter();
	}
	
	public void setItemChangeLogFilter(ItemChangeLogFilter filter){
		if(this.itemChangeLogSearch.getFilter() == null)
			this.itemChangeLogSearch.setFilter(filter);
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
	
	@Autowired
	public void setRevisionItemRepository(RepositoryRevisionItemRepository revisionItemRepository){
		this.revisionItemRepository = revisionItemRepository;
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
	public void setItemChangeLogSearch(ItemChangeLogSearch search){
		this.itemChangeLogSearch = search;
	}
	
	@Autowired
	public void setItemAssetRepository(ItemAssetRepository repository){
		this.itemAssetRepository = repository;
	}
	
	@Autowired
	public void setItemChangeLogRepository(ItemChageLogRepositoryImpl repository){
		this.itemChangeLogRepository = repository;
	}
}
