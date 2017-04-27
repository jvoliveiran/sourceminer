/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.repositoryconnection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.neo4j.domain.ClassNode;
import br.com.jvoliveira.sourceminer.neo4j.repository.ClassNodeRepository;

/**
 * @author João Victor
 *
 */
public class RepositoryRevisionItemHelper {

	public static Collection<String> getRevisions(List<RepositoryRevision> revisions){
		Collection<String> revisionsHash = new HashSet<>();
		for(RepositoryRevision rev : revisions)
			revisionsHash.add(rev.getRevision());
		return new ArrayList<String>(revisionsHash);
	}
	
	public static Map<RepositoryItem, ClassNode> loadRepositoryItemWithClassNode(List<RepositoryItem> items, ClassNodeRepository repository){
		List<Long> idsRepositoryItem = new ArrayList<>();
		for(RepositoryItem item : items)
			idsRepositoryItem.add(item.getId());
		List<ClassNode> nodesToAssociate = repository.findAllNodesOfItems(idsRepositoryItem);
		
		return associateElements(items, nodesToAssociate);
	}

	public static Map<RepositoryItem, ClassNode> associateElements(List<RepositoryItem> items, List<ClassNode> nodesToAssociate) {
		Map<RepositoryItem,ClassNode> itemWithNode = new HashMap<>();
		Iterator<RepositoryItem> iteratorItems = items.iterator();
		while(iteratorItems.hasNext()){
			RepositoryItem item = iteratorItems.next();
			for(ClassNode node : nodesToAssociate){
				if(node.getRepositoryItemId().equals(item.getId())){
					itemWithNode.put(item, node);
					break;
				}	
			}
		}
		return itemWithNode;
	}
	
}
