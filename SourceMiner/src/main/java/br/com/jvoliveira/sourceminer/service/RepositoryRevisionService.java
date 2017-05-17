/**
 * 
 */
package br.com.jvoliveira.sourceminer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jvoliveira.arq.service.AbstractArqService;
import br.com.jvoliveira.sourceminer.domain.ItemAsset;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevisionItem;
import br.com.jvoliveira.sourceminer.domain.pojo.ConflictReport;
import br.com.jvoliveira.sourceminer.neo4j.domain.ClassNode;
import br.com.jvoliveira.sourceminer.neo4j.repository.ClassNodeRepository;
import br.com.jvoliveira.sourceminer.repository.ItemAssetRepository;
import br.com.jvoliveira.sourceminer.repository.RepositoryItemRepository;
import br.com.jvoliveira.sourceminer.repository.RepositoryRevisionRepository;

/**
 * @author Joao Victor
 *
 */
@Service
public class RepositoryRevisionService extends AbstractArqService<RepositoryRevision>{

	private ItemAssetRepository itemAssetRepository;
	private ClassNodeRepository classNodeRepository;
	private RepositoryItemRepository itemRepository;
	
	@Autowired
	public RepositoryRevisionService(RepositoryRevisionRepository repository) {
		this.repository = repository;
	}
	
	public RepositoryRevision getRevisionById(Long id){
		return this.repository.findOne(id);
	}
	
	public List<ConflictReport> processIndirectConflictReport(RepositoryRevision revision){
		Collection<ItemAsset> changedMethods = itemAssetRepository.methodChangedInRevision(revision.getRevision(), revision.getProject().getId());
		Iterator<ItemAsset> changedMethodsIterator = changedMethods.iterator();
		List<ConflictReport> conflictReportResult = new ArrayList<>();
		while(changedMethodsIterator.hasNext()){
			ItemAsset asset = changedMethodsIterator.next();
			ConflictReport conflictReport = analyzeCallGraphConflictForAsset(asset,revision);
			if(conflictReport != null)
				conflictReportResult.add(conflictReport);
		}
		return conflictReportResult;
	}
	
	private ConflictReport analyzeCallGraphConflictForAsset(ItemAsset asset, RepositoryRevision revision) {
		Collection<ClassNode> corelatedNodes = classNodeRepository.getNodesCallingMethod(asset.getId());
		if(corelatedNodes.isEmpty())
			return null;
		Collection<Long> ids = new ArrayList<>();
		corelatedNodes.stream().forEach(classNode -> ids.add(classNode.getRepositoryItemId()));
		Collection<RepositoryRevisionItem> items = itemRepository.findItemsModifiedPreviouslyIn(ids,revision);
		
		if(items.isEmpty())
			return null;
		return buildConflictReportItem(asset, items);
	}

	private ConflictReport buildConflictReportItem(ItemAsset asset, Collection<RepositoryRevisionItem> items) {
		Collection<Long> itensIncluded = new ArrayList<>();
		ConflictReport conflictReport = new ConflictReport();
		conflictReport.setAsset(asset);
		conflictReport.setItem(asset.getRepositoryItem());
		conflictReport.setOtherCallers(new ArrayList<>());
		items.stream().forEach(revItem -> {
			if(!itensIncluded.contains(revItem.getRepositoryItem().getId())){
				itensIncluded.add(revItem.getRepositoryItem().getId());
				conflictReport.getOtherCallers().add(revItem);
			}
		});
		return conflictReport;
	}

	@Autowired
	public void setItemAssetRepository(ItemAssetRepository repository){
		this.itemAssetRepository = repository;
	}
	
	@Autowired
	public void setClassNodeRepository(ClassNodeRepository repository){
		this.classNodeRepository = repository;
	}
	
	@Autowired
	public void setRepositoryItemRepository(RepositoryItemRepository repository){
		this.itemRepository = repository;
	}
}
