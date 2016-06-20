/**
 * 
 */
package br.com.jvoliveira.sourceminer.service;

import java.util.ArrayList;
import java.util.List;

import org.hsqldb.HsqlException;
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
import br.com.jvoliveira.sourceminer.repository.ItemChageLogRepositoryImpl;
import br.com.jvoliveira.sourceminer.repository.ProjectConfigurationRepository;
import br.com.jvoliveira.sourceminer.repository.ProjectRepository;
import br.com.jvoliveira.sourceminer.repository.RepositoryItemRepository;
import br.com.jvoliveira.sourceminer.repository.RepositoryRevisionItemRepository;
import br.com.jvoliveira.sourceminer.repository.RepositoryRevisionRepository;
import br.com.jvoliveira.sourceminer.repository.RepositorySyncLogRepository;
import br.com.jvoliveira.sourceminer.sync.SyncRepositoryObserver;

/**
 * @author Joao Victor
 *
 */

@Service
public class SyncRepositoryService extends AbstractArqService<Project> {

	private RepositoryConnectionSession connection;
	
	private RepositorySyncLogRepository syncLogRepository;
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
		
		synchronizeRepositoryUsingConfiguration(project);
		
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
		
		List<RepositoryItem> repositoryItens = new ArrayList<RepositoryItem>();
		
		if(lastSyncLog == null && !getConfig().isJoinParentBrach())
			repositoryItens = synchronizeRepositoryItem(project);
		
		List<RepositoryRevisionItem> repositoryItensDatabase = synchronizeRepositoryRevision(project);
		
		if(repositoryItensDatabase != null && getConfig().isFullAssetSync()){
			
			processRepositoryItemChanges(repositoryItensDatabase);
			processSimpleRepositoryItemChanges(repositoryItens);
			
		}else if(!getConfig().isFullAssetSync()){
			
			if(repositoryItensDatabase != null){
				for(RepositoryRevisionItem revisionItem : repositoryItensDatabase){
					if(!repositoryItens.contains(revisionItem.getRepositoryItem()))
						repositoryItens.add(revisionItem.getRepositoryItem());
				}
			}
			
			processSimpleRepositoryItemChanges(repositoryItens);
		}
		
		refreshSyncLog(lastSyncLog, project);
	}

	/**
	 * Sincroniza todos os itens do repositório, mesmo que não associados a uma revisão
	 * @param project
	 * @return 
	 */
	private List<RepositoryItem> synchronizeRepositoryItem(Project project){
		notifyObservers("CARREGANDO ARTEFATOS PARA SINCRONIZAÇÃO...");
		List<RepositoryItem> items = connection.getConnection().getAllProjectItens(project);
		
		notifyObservers("ARTEFATOS PARA SINCRONIZAÇÃO: " + items.size());
		
		for(RepositoryItem repositoryItem : items)
			getSyncItem(project, repositoryItem);
		
		notifyObservers("ARTEFATOS SINCRONIZADOS!!!");
		
		return items;
	}
	
	/**
	 * Associa os itens do repositório com as revisões.
	 * @param project
	 * @return 
	 */
	private List<RepositoryRevisionItem> synchronizeRepositoryRevision(Project project){
		Integer revisionStartSync = getRevisionToStartSync(project);
		
		if(revisionStartSync == 0)
			return null;
		
		getConfig().setSyncStartRevision(revisionStartSync);
		getConfig().setSyncEndRevision(-1);
		
		notifyObservers("CARREGANDO REVISÕES PARA SINCRONIZAÇÃO...");
		
		List<RepositoryRevisionItem> repositoryItens = connection.getConnection().
				getRevisionItensInProjectRange(project, getConfig());
		
		notifyObservers("ITENS COM REVISÃO PARA SINCRONIZAÇÃO: " + repositoryItens.size());
		
		for(RepositoryRevisionItem item : repositoryItens){
			item.setCreateAt(DateUtils.now());
			item.setProject(project);
			item.setRepositoryRevision(getSyncRevision(project,item.getRepositoryRevision()));
			item.setRepositoryItem(getSyncItem(project, item.getRepositoryItem()));
			
			revisionItemRepository.save(item);
		}
		
		notifyObservers("ITENS COM REVISÃO SINCRONIZADOS!!!");
		
		return repositoryItens;
	}
	
	/**
	 * Identifica a primeira revisão do repositório que não está sincronizada com o SourceMiner
	 * @param project
	 * @return
	 */
	private Integer getRevisionToStartSync(Project project) {
		Integer revisionStartSync = 1;
		
		Long revisionLastCommit = connection.getConnection().getLastRevisionNumber(project);
		
		RepositorySyncLog lastSyncLog = getLastSync(project);
		
		if(lastSyncLog != null){
			Long revisionLastSync = lastSyncLog.getHeadRevision();
			
			if(revisionLastSync < revisionLastCommit)
				revisionStartSync = revisionLastSync.intValue() + 1;
			else
				return 0;
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
	private void processRepositoryItemChanges(List<RepositoryRevisionItem> revisionItensInDatabase) {
		notifyObservers("SINCRONIZANDO ITEM CHANGE...");
		
		notifyObservers("ITENS CHANGE PARA SINCRONIZAÇÃO: " + revisionItensInDatabase.size());
		
		Integer itemChangeProcessed = 0;
		for(RepositoryRevisionItem revisionItem : revisionItensInDatabase){
			Long revisionNumber = revisionItem.getRepositoryRevision().getRevision();
			RepositoryItem item = revisionItem.getRepositoryItem();
			
			List<ItemAsset> actualItemAssets = itemAssetRepository.findByRepositoryItemAndEnable(item,true);
			
			actualItemAssets.stream().forEach(asset -> asset.setNewAsset(true));
			
			String fileContent = getFileContentInRevision(item.getPath(), revisionNumber);
			
			List<ItemAsset> assetsToSync = classParserHelper.generateActualClassAssets(actualItemAssets, fileContent, item.getFullPath());
			
			syncAssets(assetsToSync, revisionItem);
			
			itemChangeProcessed++;
			notifyObservers("SINCRONIZANDO ITEM CHANGE: " + itemChangeProcessed + "/" + revisionItensInDatabase.size() );
		}
		
		notifyObservers("ITEM CHANGE SINCRONIZADO!!!");
	}
	
	/**
	 * Sincronização simples de assets, usando apenas a ultima versão do repositório
	 * @param itensInDatabase
	 */
	private void processSimpleRepositoryItemChanges(List<RepositoryItem> itensInDatabase){
		notifyObservers("SINCRONIZANDO ITEM CHANGE...");
		
		notifyObservers("ITENS CHANGE PARA SINCRONIZAÇÃO: " + itensInDatabase.size());
		
		Integer itemChangeProcessed = 0;
		for(RepositoryItem item : itensInDatabase){
			List<ItemAsset> actualItemAssets = itemAssetRepository.findByRepositoryItemAndEnable(item,true);
			
			actualItemAssets.stream().forEach(asset -> asset.setNewAsset(true));
			
			String fileContent = getFileContentInRevision(item.getPath(), -1L);
			
			List<ItemAsset> assetsToSync = classParserHelper.generateActualClassAssets(actualItemAssets, fileContent, item.getFullPath());
			
			syncAssets(assetsToSync, new RepositoryRevisionItem(item));
			itemChangeProcessed++;
			notifyObservers("SINCRONIZANDO ITEM CHANGE: " + itemChangeProcessed + "/" + itensInDatabase.size() );
		}
		notifyObservers("ITEM CHANGE SINCRONIZADO!!!");
	}
	
	
	public String getFileContentInRevision(String path, Long revision){
		return this.connection.getConnection().getFileContent(path, revision);
	}
	
	/**
	 * Sincroniza todas as adições, alterações e exclusões de métodos, imports e atributos em uma classe.
	 * @param assetsToSync
	 * @param revisionItem
	 */
	private void syncAssets(List<ItemAsset> assetsToSync, RepositoryRevisionItem revisionItem) {
		System.out.println("SINCRONIZANDO ASSETS...");
		
		System.out.println("ASSETS PARA SINCRONIZAÇÃO: " + assetsToSync.size());
		for(ItemAsset asset : assetsToSync){
			
			if(asset.getId() == null){
				asset.setCreateAt(DateUtils.now());
				asset.setRepositoryItem(revisionItem.getRepositoryItem());
				
				if(asset.isAbleToSetImportRepositoryItem())
					setImportRepositoryItem(asset, revisionItem);
				
				itemAssetRepository.save(asset);
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
		System.out.println("ASSETS SINCRONIZADOS!!!");
	}

	private void setImportRepositoryItem(ItemAsset asset, RepositoryRevisionItem revisionItem) {
		RepositoryItem importItem = itemRepository.findItemByFullPath(asset.getSignature(), revisionItem.getProject(), revisionItem.getRepositoryItem().getExtension());
		if(importItem != null)
			asset.setImportRepositoryItem(importItem);
	}

	private RepositoryRevision getSyncRevision(Project project, RepositoryRevision revision){
		Long revisionNumber = revision.getRevision();
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
		if(lastSyncLog != null && lastSyncLog.getHeadRevision() == connection.getConnection().getLastRevisionNumber(project))
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
	public void setProjectConfigurationRepository(ProjectConfigurationRepository repository){
		this.projectConfigurationRepository = repository;
	}
	
	public void notifyObservers(String mensagem){
		if(observer != null);
			observer.getNotification(mensagem);
	}

}
