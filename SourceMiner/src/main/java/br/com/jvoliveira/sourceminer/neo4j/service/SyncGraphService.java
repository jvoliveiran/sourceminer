/**
 * 
 */
package br.com.jvoliveira.sourceminer.neo4j.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jvoliveira.arq.service.AbstractArqService;
import br.com.jvoliveira.sourceminer.component.repositoryconnection.RepositoryConnection;
import br.com.jvoliveira.sourceminer.component.repositoryconnection.RepositoryConnectionSession;
import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.neo4j.domain.ClassNode;
import br.com.jvoliveira.sourceminer.neo4j.repository.ClassNodeRepository;
import br.com.jvoliveira.sourceminer.repository.impl.RepositoryItemRepositoryImpl;
import br.com.jvoliveira.sourceminer.sync.SyncRepositoryObserver;

/**
 * @author João Victor
 *
 */
@Service
public class SyncGraphService extends AbstractArqService<Project>{

	//ATRIBUTOS
	private RepositoryConnectionSession connection;
	private SyncRepositoryObserver observer;
	
	private RepositoryItemRepositoryImpl itemRepository;
	private ClassNodeRepository nodeRepository;
	
	//CONSTRUTOR
	
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
		List<RepositoryItem> itemWithouNode = itemRepository.findItemWithoutNode(project);
		createNodeForRepositoryItem(itemWithouNode);
		//TODO: Utilizar a mesma lista de items para sincronizar chamada de métodos
	}

	private void createNodeForRepositoryItem(List<RepositoryItem> itemWithouNode) {
		for(RepositoryItem item : itemWithouNode){
			ClassNode newClassNode = new ClassNode(item);
			nodeRepository.save(newClassNode);
			
			item.setGraphNodeId(newClassNode.getId());
			getDAO().update(item);
		}
	}
	
	@Autowired
	public void setRepositoryItemRepositoryImpl(RepositoryItemRepositoryImpl repository){
		this.itemRepository = repository;
	}
	
	@Autowired
	public void setClassNodeRepository(ClassNodeRepository nodeRepository){
		this.nodeRepository = nodeRepository;
	}
}
