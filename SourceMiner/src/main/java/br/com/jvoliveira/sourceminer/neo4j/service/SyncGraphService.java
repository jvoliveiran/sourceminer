/**
 * 
 */
package br.com.jvoliveira.sourceminer.neo4j.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jvoliveira.arq.service.AbstractArqService;
import br.com.jvoliveira.sourceminer.component.javaparser.visitor.CallGraphVisitorExecutor;
import br.com.jvoliveira.sourceminer.component.javaparser.visitor.GraphElementDTO;
import br.com.jvoliveira.sourceminer.component.repositoryconnection.RepositoryConnection;
import br.com.jvoliveira.sourceminer.component.repositoryconnection.RepositoryConnectionSession;
import br.com.jvoliveira.sourceminer.component.repositoryconnection.RepositoryRevisionItemHelper;
import br.com.jvoliveira.sourceminer.domain.ItemAsset;
import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.domain.RepositorySyncLog;
import br.com.jvoliveira.sourceminer.domain.enums.AssetType;
import br.com.jvoliveira.sourceminer.neo4j.domain.ClassNode;
import br.com.jvoliveira.sourceminer.neo4j.domain.FieldAccess;
import br.com.jvoliveira.sourceminer.neo4j.domain.MethodCall;
import br.com.jvoliveira.sourceminer.neo4j.domain.RelationshipGraphElement;
import br.com.jvoliveira.sourceminer.neo4j.repository.ClassNodeRepository;
import br.com.jvoliveira.sourceminer.neo4j.repository.FieldAccessRepository;
import br.com.jvoliveira.sourceminer.neo4j.repository.MethodCallRepository;
import br.com.jvoliveira.sourceminer.repository.ItemAssetRepository;
import br.com.jvoliveira.sourceminer.repository.RepositoryItemRepository;
import br.com.jvoliveira.sourceminer.repository.impl.SyncLogRepositoryImpl;
import br.com.jvoliveira.sourceminer.sync.SyncRepositoryObserver;

/**
 * @author João Victor
 *
 */
@Service
public class SyncGraphService extends AbstractArqService<Project>{

	private RepositoryConnectionSession connection;
	private SyncRepositoryObserver observer;
	
	private RepositoryItemRepository itemRepository;
	private ItemAssetRepository itemAssetRepository;
	private ClassNodeRepository nodeRepository;
	private MethodCallRepository methodCallRepository;
	private FieldAccessRepository fieldAccessRepository;
	private SyncLogRepositoryImpl syncLogRepository;
	
	@Autowired
	public SyncGraphService(RepositoryConnectionSession connection){
		this.connection = connection;
	}
	
	public void syncGraphUsingConfigurationObserver(Project project, SyncRepositoryObserver observer, RepositoryConnection connection){
		if(observer.isInSync())
			return;
		
		this.connection = new RepositoryConnectionSession();
		this.connection.setConnection(connection);
		
		System.out.println("Execute sync asynchronously - "
			      + Thread.currentThread().getName());
		
		this.observer = observer;
		this.observer.startSync();
		
		notifyObservers("Iniciando sincronização de grafo");
		synchronizeGraphUsingConfiguration(project);
		notifyObservers("Finalizando sincronização de grafo");
		
		this.observer.closeSync();
	}

	//TODO: Passo 3: Criar método como serviço para remover methodCalls de Assets desativados
	//TODO: Passo 4: Criar método como serviço para remover ClassNode e respectivos methodCalls para classes removidas.
	public void synchronizeGraphUsingConfiguration(Project project) {
		itemsForFirstSync(project);
		boolean isFirstSync = isFirstSync(project);
		if(isFirstSync && project.isGraphOutOfSync())
			refreshSyncLog(project);
		else if(!isFirstSync && project.isGraphOutOfSync()){
			Map<RepositoryItem,ClassNode> itemNode = getItensChangedAfterLastSync(project);
			createRelationshipInGraph(project,itemNode,true);
			refreshSyncLog(project);
		}
	}
	
	private Map<RepositoryItem, ClassNode> getItensChangedAfterLastSync(Project project) {
		List<String> twoLastRevisions = syncLogRepository.findTwoMostRecentRevisions(project);
		List<RepositoryRevision> revisions = connection.getConnection().getRevisionsInRange(project, twoLastRevisions.get(1), twoLastRevisions.get(0));
		List<String> revisionsHash = (List<String>) RepositoryRevisionItemHelper.getRevisions(revisions);
		
		List<RepositoryItem> itemsChangeInRevisions = itemRepository.findItemsChangedInRevisions(project, revisionsHash);
		return RepositoryRevisionItemHelper.loadRepositoryItemWithClassNode(itemsChangeInRevisions, nodeRepository);
	}

	private boolean isFirstSync(Project project){
		return syncLogRepository.countSyncLogByProject(project) <= 1;
	}
	
	private void itemsForFirstSync(Project project){
		notifyObservers("Criando novos nós do grafo...");
		List<RepositoryItem> itemWithoutNode = itemRepository.findItemWithoutNode(project);
		if(!itemWithoutNode.isEmpty()){
			Map<RepositoryItem,ClassNode> itemNode = createNodeForRepositoryItem(project,itemWithoutNode);
			createRelationshipInGraph(project, itemNode, false);
		}
	}

	private Map<RepositoryItem,ClassNode> createNodeForRepositoryItem(Project project, List<RepositoryItem> itemWithouNode) {
		Map<RepositoryItem,ClassNode> result = new HashMap<>();
		Integer totalNos = itemWithouNode.size();
		Integer noAtual = 1;
		for(RepositoryItem item : itemWithouNode){
			ClassNode newClassNode = new ClassNode(item,project);
			nodeRepository.save(newClassNode);
			
			item.setGraphNodeId(newClassNode.getId());
			getDAO().update(item);
			result.put(item,newClassNode);
			notifyObservers("Criando nó "+noAtual+"/"+totalNos);
			noAtual++;
		}
		return result;
	}
	
	private void createRelationshipInGraph(Project project, Map<RepositoryItem,ClassNode> itemNode, boolean searchForMethodsCall) {
		Iterator<RepositoryItem> repositoryItems = itemNode.keySet().iterator();
		Integer totalItens = itemNode.keySet().size();
		Integer index = 1;
		while(repositoryItems.hasNext()){
			notifyCompleteObservers("Criando chamadas de métodos para " + index + "/" + totalItens + " artefatos","");
			RepositoryItem item = repositoryItems.next();
			ClassNode classNode = itemNode.get(item);
			
			Collection<RelationshipGraphElement> methodsCall = new ArrayList<>();
			if(searchForMethodsCall){
				List<MethodCall> queryResult = nodeRepository.getAllMethodsCallFromNode(item.getId());
				if(queryResult != null)
					methodsCall.addAll(queryResult);
			}
			processCallGraph(classNode,item,methodsCall);
			index++;
		}
		
	}
	
	private List<RelationshipGraphElement> processCallGraph(ClassNode classNode, RepositoryItem item, Collection<RelationshipGraphElement> methodsCall) {
		String headRevision = "-1";
		String fileContent = this.connection.getConnection().getFileContent(item.getFullPath(), headRevision);
		Map<String,Collection<GraphElementDTO>> resultMap = CallGraphVisitorExecutor.processCallGraph(fileContent, item.getPath());
		return identifyRelationWithMethodCall(classNode,item,resultMap,methodsCall);
	}
	
	private List<RelationshipGraphElement> identifyRelationWithMethodCall(ClassNode classNode, RepositoryItem item, Map<String,Collection<GraphElementDTO>> resultMap,
			Collection<RelationshipGraphElement> methodsCall) {
		List<RelationshipGraphElement> result = new ArrayList<>();
		
		Iterator<String> iteratorResultMap = resultMap.keySet().iterator();
		Integer nodeIndex = 1;
		while(iteratorResultMap.hasNext()){
			String className = iteratorResultMap.next();
			Collection<GraphElementDTO> methodsCalledInClass = resultMap.get(className);
			Integer methodIndex = 1;
			for(GraphElementDTO methodCalledInClass : methodsCalledInClass){
				notifySubLabelObservers("Nó ["+nodeIndex+"/"+resultMap.size()+"] - Método ["+methodIndex+"/"+methodsCalledInClass.size()+"]");
				List<RelationshipGraphElement> methodsCalled = createAndPersistMethodCall(item.getProject(), className, methodCalledInClass,classNode,methodsCall);
				if(methodsCalled != null)
					result.addAll(methodsCalled);
				methodIndex++;
			}
			nodeIndex++;
		}
		
		return result;
	}

	//FIXME: GARGALO! PROJETAR CONSULTAS!
	private List<RelationshipGraphElement> createAndPersistMethodCall(Project project, String className, GraphElementDTO methodCalledInClass, ClassNode classNode,
			Collection<RelationshipGraphElement> methodsCall) {
		//TODO: Refatorar concatenação de extensão. Sugestão: Selecionar extensão de arquivos para sincronização nas configurações do projeto.
		RepositoryItem itemCalled = itemRepository.findFirstByNameAndProject(className+".java",project);
		if(itemCalled == null)
			return null;
		
		List<RelationshipGraphElement> methodsCalled = new ArrayList<>();
		List<ItemAsset> dependencies = itemAssetRepository.findByRepositoryItemAndEnableAndAssetTypeIn(itemCalled, true, Arrays.asList(AssetType.METHOD, AssetType.FIELD));
		for (ItemAsset asset : dependencies) {
			ClassNode nodeCalled = new ClassNode(itemCalled,project);
			if(methodCalledInClass.isMethodCall() && !isExistsMethodCall(classNode,nodeCalled,asset,methodsCall) && methodCalledInClass.getElementName().equals(asset.getName())){
				MethodCall relation = buildMethodCall(project, classNode, asset, nodeCalled);
				methodsCalled.add(relation);
			}else if(methodCalledInClass.isAccessField() && methodCalledInClass.getElementName().equals(asset.getName())){
				FieldAccess fieldAccess = buildAccessField(project, classNode, asset, nodeCalled, methodCalledInClass);
				methodsCalled.add(fieldAccess);
			}
		}
		return methodsCalled;
	}

	private FieldAccess buildAccessField(Project project, ClassNode classNode, ItemAsset asset, ClassNode nodeCalled, GraphElementDTO methodCalledInClass) {
		FieldAccess relation = new FieldAccess(classNode, nodeCalled);
		relation.setFieldId(asset.getId());
		relation.setProjectId(project.getId());
		RepositoryItem item = new RepositoryItem(classNode.getRepositoryItemId());
		List<ItemAsset> methodWhichAccessField = itemAssetRepository.findByRepositoryItemAndEnableAndAssetTypeAndName(item, true, AssetType.METHOD, methodCalledInClass.getAuxiliarElementName());
		if(methodWhichAccessField != null && methodWhichAccessField.size() > 0){
			relation.setMethodId(methodWhichAccessField.iterator().next().getId());
			fieldAccessRepository.save(relation);
			return relation;
		}
		return null;
	}

	private MethodCall buildMethodCall(Project project, ClassNode classNode, ItemAsset asset, ClassNode nodeCalled) {
		MethodCall relation = new MethodCall(classNode, nodeCalled);
		relation.setItemAssetId(asset.getId());
		relation.setMethodName(asset.getName());
		relation.setMethodSignature(asset.getSignature());
		relation.setProjectId(project.getId());
		methodCallRepository.save(relation);
		return relation;
	}

	private boolean isExistsMethodCall(ClassNode classNode, ClassNode nodeCalled, ItemAsset asset,
			Collection<RelationshipGraphElement> methodsCall) {
		for(RelationshipGraphElement method : methodsCall){
			if(method instanceof MethodCall){
				MethodCall methodCall = (MethodCall) method;
				if(methodCall.getCalled().getId() == nodeCalled.getId()
						&& methodCall.getCaller().getId() == classNode.getId()
						&& methodCall.getItemAssetId() == asset.getId())
					return true;
			}else if(method instanceof FieldAccess){
				FieldAccess fieldAccess = (FieldAccess) method;
				if(fieldAccess.getCalled().getId() == nodeCalled.getId()
						&& fieldAccess.getCaller().getId() == classNode.getId()
						&& fieldAccess.getFieldId() == asset.getId())
					return true;
			}
		}
		return false;
	}
	
	private void refreshSyncLog(Project project) {
		RepositorySyncLog syncLog = project.getLastSync();
		syncLog.setGraphSyncDate(new Date());
		getDAO().update(syncLog);
	}

	
	public void notifyObservers(String mensagem){
		if(observer != null)
			observer.getNotification(mensagem);
	}
	
	public void notifyCompleteObservers(String mensagem, String subMensagem){
		if(observer != null){
			observer.getNotification(mensagem);
			observer.getSubNotification(subMensagem);
		}
	}
	
	public void notifySubLabelObservers(String mensagem){
		if(observer != null)
			observer.getSubNotification(mensagem);
	}

	@Autowired
	public void setRepositoryItemRepositoryImpl(RepositoryItemRepository repository){
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
	
	@Autowired
	public void setSyncLogRepository(SyncLogRepositoryImpl syncLogRepository){
		this.syncLogRepository = syncLogRepository;
	}
	
	@Autowired
	public void setFieldAccessRepository(FieldAccessRepository fieldRepository){
		this.fieldAccessRepository = fieldRepository;
	}
}
