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
import br.com.jvoliveira.sourceminer.component.javaparser.ClassParserHelper;
import br.com.jvoliveira.sourceminer.component.repositoryconnection.RepositoryConnection;
import br.com.jvoliveira.sourceminer.component.repositoryconnection.RepositoryConnectionSession;
import br.com.jvoliveira.sourceminer.domain.ItemAsset;
import br.com.jvoliveira.sourceminer.domain.ItemChangeLog;
import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.ProjectConfiguration;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevisionItem;
import br.com.jvoliveira.sourceminer.domain.RepositorySyncLog;
import br.com.jvoliveira.sourceminer.repository.ItemAssetRepository;
import br.com.jvoliveira.sourceminer.repository.ProjectConfigurationRepository;
import br.com.jvoliveira.sourceminer.repository.ProjectRepository;
import br.com.jvoliveira.sourceminer.repository.RepositoryItemRepository;
import br.com.jvoliveira.sourceminer.repository.RepositoryRevisionItemRepository;
import br.com.jvoliveira.sourceminer.repository.RepositoryRevisionRepository;
import br.com.jvoliveira.sourceminer.repository.SyncLogRepository;
import br.com.jvoliveira.sourceminer.repository.impl.ItemChageLogRepositoryImpl;
import br.com.jvoliveira.sourceminer.sync.SyncRepositoryObserver;

/**
 * @author Joao Victor
 *
 */

@Service
public class SyncRepositoryService extends AbstractArqService<Project> {

	private RepositoryConnectionSession connection;
	
	private RevisionItemMetricService revisionItemMetricService;
	
	private SyncLogRepository syncLogRepository;
	private RepositoryRevisionRepository revisionRepository;
	private RepositoryItemRepository itemRepository;
	private RepositoryRevisionItemRepository revisionItemRepository;
	private ItemAssetRepository itemAssetRepository;
	private ProjectConfigurationRepository projectConfigurationRepository;
	private ItemChageLogRepositoryImpl itemChangeLogRepository;
	
	private ClassParserHelper classParserHelper;
	
	private ProjectConfiguration config;
	
	private SyncRepositoryObserver observer;
	
	@Autowired
	public SyncRepositoryService(RepositoryConnectionSession connection,
			ProjectRepository repository){
		this.connection = connection;
		this.repository = repository;
		
		this.classParserHelper = new ClassParserHelper();
	}
	
	/**
	 * Iniciar sincronização utilizando observer
	 * @param project
	 * @param observer
	 */
	public void synchronizeRepositoryUsingConfigurationObserver(Project project, SyncRepositoryObserver observer, RepositoryConnection connection){
		if(observer.isInSync())
			return;
		
		this.connection = new RepositoryConnectionSession();
		this.connection.setConnection(connection);
		
		System.out.println("Execute sync asynchronously - "
			      + Thread.currentThread().getName());
		
		this.observer = observer;
		this.observer.startSync();
		notifyObservers("Iniciando sincronização de repositório");
		
		synchronizeRepositoryUsingConfiguration(project);
		
		notifyObservers("Finalizando sincronização de repositório");
		
		this.observer.closeSync();
	}
	
	/**
	 * Método de sincronização principal. Considera as configurações de sincronização
	 * definidas para o projeto.
	 * @param project
	 */
	public void synchronizeRepositoryUsingConfiguration(Project project){
		
		this.config = projectConfigurationRepository.findOneByProject(project);
		RepositorySyncLog lastSyncLog = getLastSync(project);
		
		List<RepositoryItem> repositoryItensInDatabase = new ArrayList<RepositoryItem>();
		
		if(lastSyncLog == null && !getConfig().isJoinParentBrach())
			repositoryItensInDatabase = synchronizeAllRepositoryItemByProject(project);
		
		List<RepositoryRevisionItem> repositoryRevisionItensInDatabase = synchronizeRepositoryRevision(project);
		
		if(isContinuousFullSync(repositoryRevisionItensInDatabase)){
			processRepositoryItemChanges(repositoryRevisionItensInDatabase);
			processSimpleRepositoryItemChanges(repositoryItensInDatabase);
		}else if(!getConfig().isFullAssetSync()){
			identifyRevisionItensInSyncWithDatabase(repositoryItensInDatabase, repositoryRevisionItensInDatabase);
			processSimpleRepositoryItemChanges(repositoryItensInDatabase);
		}
		
		refreshSyncLog(lastSyncLog, project);
	}
	
	/**
	 * Solicitação de sincronização completa a partir da segunda sincronização.
	 * @param repositoryItensDatabase
	 * @return
	 */
	private boolean isContinuousFullSync(List<RepositoryRevisionItem> repositoryItensDatabase){
		if(getConfig().isFullAssetSync() && repositoryItensDatabase != null)
			return true;
		return false;
	}
	
	public void identifyRevisionItensInSyncWithDatabase(List<RepositoryItem> repositoryItensInDatabase, 
			List<RepositoryRevisionItem> repositoryRevisionItensInDatabase){
		if(repositoryRevisionItensInDatabase != null){
			for(RepositoryRevisionItem revisionItem : repositoryRevisionItensInDatabase){
				if(!repositoryItensInDatabase.contains(revisionItem.getRepositoryItem()))
					repositoryItensInDatabase.add(revisionItem.getRepositoryItem());
			}
		}
	}

	/**
	 * Sincroniza todos os itens do repositório, mesmo que não associados a uma revisão
	 * @param project
	 * @return 
	 */
	private List<RepositoryItem> synchronizeAllRepositoryItemByProject(Project project){
		notifyObservers("[1-3] Carregando artefatos para sincronização...");
		List<RepositoryItem> items = connection.getConnection().getAllProjectItens(project);
		
		notifyObservers("[1-3] Sincronizando artefatos: " + items.size());
		
		for(RepositoryItem repositoryItem : items)
			getSyncItem(project, repositoryItem);
		
		notifyObservers("[1-3] Artefatos Sincronizados!");
		
		return items;
	}
	
	/**
	 * Associa os itens do repositório com as revisões.
	 * @param project
	 * @return 
	 */
	private List<RepositoryRevisionItem> synchronizeRepositoryRevision(Project project){
		String revisionStartSync = getRevisionToStartSync(project);
		
		if(revisionStartSync == "0")
			return null;
		
		getConfig().setSyncStartRevision(revisionStartSync);
		getConfig().setSyncEndRevision(connection.getConnection().getLastRevisionNumber(project));
		
		notifyObservers("[2-3] Carregando revisões para sincronização...");
		
		List<RepositoryRevisionItem> repositoryItens = connection.getConnection().
				getRevisionItensInProjectRange(project, getConfig());
		
		notifyObservers("[2-3] Associação de artefatos com revisões: " + repositoryItens.size());
		
		for(RepositoryRevisionItem item : repositoryItens){
			if(item.getRepositoryItem() == null || item.getRepositoryRevision() == null)
				continue;
			item.setCreateAt(DateUtils.now());
			item.setProject(project);
			item.setRepositoryRevision(getSyncRevision(project,item.getRepositoryRevision()));
			item.setRepositoryItem(getSyncItem(project, item.getRepositoryItem()));
			
			revisionItemRepository.save(item);
		}
		
		notifyObservers("[2-3] Revisões sincronizadas!");
		
		return repositoryItens;
	}
	
	/**
	 * Identifica a primeira revisão do repositório que não está sincronizada com o SourceMiner
	 * @param project
	 * @return
	 */
	private String getRevisionToStartSync(Project project) {
		String revisionStartSync = "";
		RepositorySyncLog lastSyncLog = getLastSync(project);
		
		if(lastSyncLog != null){
			String revisionLastSync = lastSyncLog.getHeadRevision();
			revisionStartSync = connection.getConnection().getNextRevisionToSync(project, revisionLastSync);
		}
		
		return revisionStartSync;
	}
	
	/**
	 * Recupera a última revisão de um projeto específico no repositório
	 * @param project
	 * @return
	 */
	public RepositorySyncLog getLastSync(Project project){
		return syncLogRepository.findFirstByProjectOrderByIdDesc(project);
	}
	
	/**
	 * Identifica as mudanças geradas em cada artefato associados a uma determinada revisão.
	 * @param revisionItem
	 */
	private void processRepositoryItemChanges(List<RepositoryRevisionItem> revisionRevisionItensInDatabase) {
		notifyObservers("[3-3] Carregando histórico de mudanças...");
		notifyObservers("[3-3] Modificações em aterfatos para sincronização: " + revisionRevisionItensInDatabase.size());
		
		Integer itemChangeProcessed = 0;
		for(RepositoryRevisionItem revisionItem : revisionRevisionItensInDatabase){
			if(revisionItem.getRepositoryItem() == null)
				continue;
			String revisionNumber = revisionItem.getRepositoryRevision().getRevision();
			RepositoryItem item = revisionItem.getRepositoryItem();
			String fileContent = "";
			try{
				fileContent = getFileContentInRevision(item.getPath(), revisionNumber);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if(revisionItem.getCommitType() == null)
				System.out.println(revisionItem.getId());
				
			if(revisionItem.existFileInRepository())
				revisionItemMetricService.generateSingleRevisionItemMetric(fileContent, revisionItem, this.config);
			
			List<ItemAsset> actualItemAssets = getActualAssetsByItem(item);
			List<ItemAsset> assetsToSync = classParserHelper.generateActualClassAssets(actualItemAssets, fileContent, item.getFullPath());
			
			syncAssets(assetsToSync, revisionItem);
			itemChangeProcessed++;
			notifyObservers("[3-3] Modificações em aterfatos para sincronização: " + itemChangeProcessed + "/" + revisionRevisionItensInDatabase.size() );
		}
		
		notifyObservers("[3-3] Modificações sincronizadas!");
	}
	
	/**
	 * Sincronização simples de assets, usando apenas a ultima versão do repositório
	 * @param itensInDatabase
	 */
	private void processSimpleRepositoryItemChanges(List<RepositoryItem> itensInDatabase){
		notifyObservers("[3-3] Carregando histórico de mudanças...");
		notifyObservers("[3-3] Modificações em aterfatos para sincronização: " + itensInDatabase.size());
		
		Integer itemChangeProcessed = 0;
		for(RepositoryItem item : itensInDatabase){
			if(item == null)
				continue;
			String fileContent = getFileContentInRevision(item.getPath(), String.valueOf(-1));
			
			List<ItemAsset> actualItemAssets = getActualAssetsByItem(item);
			List<ItemAsset> assetsToSync = classParserHelper.generateActualClassAssets(actualItemAssets, fileContent, item.getFullPath());
			
			syncAssets(assetsToSync, new RepositoryRevisionItem(item));
			itemChangeProcessed++;
			notifyObservers("[3-3] Modificações em aterfatos para sincronização: " + itemChangeProcessed + "/" + itensInDatabase.size() );
		}
		notifyObservers("[3-3] Modificações sincronizadas!");
	}
	
	private List<ItemAsset> getActualAssetsByItem(RepositoryItem item){
		List<ItemAsset> actualItemAssets = itemAssetRepository.findByRepositoryItemAndEnable(item,true);
		actualItemAssets.stream().forEach(asset -> asset.setNewAsset(true));
		return actualItemAssets;
	}
	
	
	public String getFileContentInRevision(String path, String revision){
		return this.connection.getConnection().getFileContent(path, revision);
	}
	
	/**
	 * Sincroniza todas as adições, alterações e exclusões de métodos, imports e atributos em uma classe.
	 * @param assetsToSync
	 * @param revisionItem
	 */
	private void syncAssets(List<ItemAsset> assetsToSync, RepositoryRevisionItem revisionItem) {
		for(ItemAsset asset : assetsToSync){
			if(!isValidItemAsset(asset, revisionItem))
				continue;
			
			if(asset.getId() == null){
				asset.setCreateAt(DateUtils.now());
				asset.setRepositoryItem(revisionItem.getRepositoryItem());
				
				if(asset.isAbleToSetImportRepositoryItem())
					setImportRepositoryItem(asset, revisionItem);
				try{
					itemAssetRepository.save(asset);
				}catch(Exception e){
					e.printStackTrace();
					continue;
				}
			}else if(asset.getItemChageLog().getChangeType().isDelete()){
				asset.setUpdateAt(DateUtils.now());
				asset.setEnable(false);
				itemAssetRepository.save(asset);
			}else if(asset.getItemChageLog().getChangeType().isUpdate()){
				asset.setUpdateAt(DateUtils.now());
				itemAssetRepository.save(asset);
			}
			
			if(getConfig().isSyncChangeLog() && revisionItem.getId() != null){
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
	}
	
	private boolean isValidItemAsset(ItemAsset asset, RepositoryRevisionItem revisionItem) {
		if(asset.isImportAsset())
			return isValidItemAssetImport(asset, revisionItem);
		else
			return true;
	}

	private boolean isValidItemAssetImport(ItemAsset asset, RepositoryRevisionItem revisionItem) {
		if(asset.getName() != null)
			return true;
		
		RepositoryItem item = itemRepository.findItemByFullPath(asset.getSignature(), revisionItem.getProject(), revisionItem.getRepositoryItem().getExtension());
		if(item != null){
			asset.setName(asset.getSignature());
			return true;
		}else
			return false;
	}

	private void setImportRepositoryItem(ItemAsset asset, RepositoryRevisionItem revisionItem) {
		RepositoryItem importItem = itemRepository.findItemByFullPath(asset.getSignature(), revisionItem.getProject(), revisionItem.getRepositoryItem().getExtension());
		if(importItem != null)
			asset.setImportRepositoryItem(importItem);
	}

	private RepositoryRevision getSyncRevision(Project project, RepositoryRevision revision){
		String revisionNumber = revision.getRevision();
		RepositoryRevision revisionFromDB = revisionRepository.findByProjectAndRevision(project,revisionNumber);
		
		if(revisionFromDB != null)
			return revisionFromDB;
		else{
			revision.setProject(project);
			revision.setCreateAt(DateUtils.now());
			try{
				revision = revisionRepository.save(revision);
			}catch(Exception hsqlException){
				revision.setComment("Rev " + revision.getRevision());
				revision = revisionRepository.save(revision);
			}
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
		if(lastSyncLog != null && lastSyncLog.getHeadRevision().equals(connection.getConnection().getLastRevisionNumber(project)))
			return;
		
		lastSyncLog = new RepositorySyncLog();
		lastSyncLog.setCreateAt(DateUtils.now());
		lastSyncLog.setProject(project);
		lastSyncLog.setHeadRevision(connection.getConnection().getLastRevisionNumber(project));
		
		syncLogRepository.save(lastSyncLog);
		
		project.setLastSync(lastSyncLog);
		getDAO().update(project);
		
	}
	
	private ProjectConfiguration getConfig() {
		return config;
	}
	
	@Autowired
	public void setRevisionItemRepository(RepositoryRevisionItemRepository revisionItemRepository){
		this.revisionItemRepository = revisionItemRepository;
	}
	
	@Autowired
	public void setItemAssetRepository(ItemAssetRepository repository){
		this.itemAssetRepository = repository;
	}
	
	@Autowired
	public void setItemChangeLogRepository(ItemChageLogRepositoryImpl repository){
		this.itemChangeLogRepository = repository;
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
	public void setProjectConfigurationRepository(ProjectConfigurationRepository repository){
		this.projectConfigurationRepository = repository;
	}
	
	@Autowired
	public void setRevisionItemMetricService(RevisionItemMetricService service){
		this.revisionItemMetricService = service;
	}
	
	public void notifyObservers(String mensagem){
		if(observer != null)
			observer.getNotification(mensagem);
	}

}
