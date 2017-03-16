/**
 * 
 */
package br.com.jvoliveira.sourceminer.neo4j.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jvoliveira.arq.service.AbstractArqService;
import br.com.jvoliveira.sourceminer.component.repositoryconnection.RepositoryConnection;
import br.com.jvoliveira.sourceminer.component.repositoryconnection.RepositoryConnectionSession;
import br.com.jvoliveira.sourceminer.domain.ItemAsset;
import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.enums.AssetType;
import br.com.jvoliveira.sourceminer.neo4j.domain.ClassNode;
import br.com.jvoliveira.sourceminer.neo4j.domain.MethodCall;
import br.com.jvoliveira.sourceminer.neo4j.repository.ClassNodeRepository;
import br.com.jvoliveira.sourceminer.neo4j.repository.MethodCallRepository;
import br.com.jvoliveira.sourceminer.repository.ItemAssetRepository;
import br.com.jvoliveira.sourceminer.repository.impl.RepositoryItemRepositoryImpl;
import br.com.jvoliveira.sourceminer.sync.SyncRepositoryObserver;

/**
 * @author João Victor
 *
 */
@Service
public class SyncGraphService extends AbstractArqService<Project>{

	private RepositoryConnectionSession connection;
	private SyncRepositoryObserver observer;
	
	private RepositoryItemRepositoryImpl itemRepository;
	private ItemAssetRepository itemAssetRepository;
	private ClassNodeRepository nodeRepository;
	private MethodCallRepository methodCallRepository;
	
	public void syncGraphUsingConfigurationObserver(Project project, SyncRepositoryObserver observer, RepositoryConnection connection){
		if(observer.isInSync())
			return;
		
		this.connection = new RepositoryConnectionSession();
		this.connection.setConnection(connection);
		
		System.out.println("Execute sync asynchronously - "
			      + Thread.currentThread().getName());
		
		this.observer = observer;
		this.observer.startSync();
		
		synchronizeGraphUsingConfiguration(project);
		
		this.observer.closeSync();
	}

	public void synchronizeGraphUsingConfiguration(Project project) {
		List<RepositoryItem> itemWithoutNode = itemRepository.findItemWithoutNode(project);
		Map<RepositoryItem,ClassNode> itemNode = createNodeForRepositoryItem(itemWithoutNode);
		
		//TODO: Utilizar a mesma lista de items para sincronizar chamada de métodos
		createRelationshipInGraph(itemNode);
		
	}

	private Map<RepositoryItem,ClassNode> createNodeForRepositoryItem(List<RepositoryItem> itemWithouNode) {
		Map<RepositoryItem,ClassNode> result = new HashMap<>();
		for(RepositoryItem item : itemWithouNode){
			ClassNode newClassNode = new ClassNode(item);
			nodeRepository.save(newClassNode);
			
			item.setGraphNodeId(newClassNode.getId());
			getDAO().update(item);
			result.put(item,newClassNode);
		}
		return result;
	}
	
	private void createRelationshipInGraph(Map<RepositoryItem,ClassNode> itemNode) {
		Iterator<RepositoryItem> repositoryItems = itemNode.keySet().iterator();
		
		while(repositoryItems.hasNext()){
			RepositoryItem item = repositoryItems.next();
			ClassNode classNode = itemNode.get(item);
			List<MethodCall> relations = identifyRelationWithDependency(classNode, item);
		}
		
	}
	
	private List<MethodCall> identifyRelationWithDependency(ClassNode classNode, RepositoryItem item) {
		List<MethodCall> result = new ArrayList<>();
		List<ItemAsset> dependencies = itemAssetRepository.findByRepositoryItemAndEnableAndAssetType(item, true, AssetType.IMPORT);
		for(ItemAsset asset : dependencies){
			RepositoryItem importItem = asset.getImportRepositoryItem();
			if(importItem != null){
				MethodCall relation = new MethodCall(classNode, new ClassNode(importItem));
				methodCallRepository.save(relation);
				result.add(relation);
			}
		}
		return result;
	}

	@Autowired
	public void setRepositoryItemRepositoryImpl(RepositoryItemRepositoryImpl repository){
		this.itemRepository = repository;
	}
	
	@Autowired
	public void setClassNodeRepository(ClassNodeRepository nodeRepository){
		this.nodeRepository = nodeRepository;
	}
	
	@Autowired
	public void setItemAssetRepository(ItemAssetRepository itemAssetRepository){
		this.itemAssetRepository = itemAssetRepository;
	}
	
	@Autowired
	public void setMethodCallRepository(MethodCallRepository methodCallRepository){
		this.methodCallRepository = methodCallRepository;
	}
}
