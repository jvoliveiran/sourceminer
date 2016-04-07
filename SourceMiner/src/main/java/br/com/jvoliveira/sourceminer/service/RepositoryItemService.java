/**
 * 
 */
package br.com.jvoliveira.sourceminer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jvoliveira.arq.service.AbstractArqService;
import br.com.jvoliveira.sourceminer.component.javaparser.ChangeLogGroupModel;
import br.com.jvoliveira.sourceminer.component.javaparser.ClassParserHelper;
import br.com.jvoliveira.sourceminer.component.repositoryconnection.RepositoryConnectionSession;
import br.com.jvoliveira.sourceminer.domain.ItemChangeLog;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.repository.ItemChageLogRepositoryImpl;
import br.com.jvoliveira.sourceminer.repository.RepositoryItemRepository;
import br.com.jvoliveira.sourceminer.search.ItemChangeLogSearch;
import br.com.jvoliveira.sourceminer.search.filter.ItemChangeLogFilter;

/**
 * @author Joao Victor
 *
 */
@Service
public class RepositoryItemService extends AbstractArqService<RepositoryItem>{

	private RepositoryConnectionSession connection;
	private ClassParserHelper classParserHelper;
	
	private ItemChangeLogSearch itemChangeLogSearch;
	
	private ItemChageLogRepositoryImpl itemChangeLogRepository;
	
	@Autowired
	public RepositoryItemService(RepositoryItemRepository repository,
			RepositoryConnectionSession connection) {
		this.repository = repository;
		this.connection = connection;
		this.classParserHelper = new ClassParserHelper();
	}

	public RepositoryItem getItemById(Long id){
		return this.repository.findOne(id);
	}
	
	public void searchItemChangeLog(RepositoryItem item, ItemChangeLogFilter filter){
		filter.setItem(item);
		itemChangeLogSearch.setFilter(filter);
		itemChangeLogSearch.searchWithFilter();
	}
	
	public void setItemChangeLogFilter(ItemChangeLogFilter filter){
		if(this.itemChangeLogSearch.getFilter() == null)
			this.itemChangeLogSearch.setFilter(filter);
	}
	
	public String getFileContentInRevision(String path, Long revision){
		return this.connection.getConnection().getFileContent(path, revision);
	}
	
	public List<ChangeLogGroupModel> getChangeLogInRepositoryItem(RepositoryItem item){
		List<ItemChangeLog> changeLogs = null;
		
		if(getItemChangeLogFilter().hasFilterToSearch())
			changeLogs = itemChangeLogSearch.searchWithFilter();
		else
			changeLogs = this.itemChangeLogRepository.findAllChangeLogRepositoryItem(item);
		
		return classParserHelper.getChangeLogGroupByRevision(changeLogs);
	}
	
	public ItemChangeLogFilter getItemChangeLogFilter(){
		return (ItemChangeLogFilter) this.itemChangeLogSearch.getFilter();
	}
	
	@Autowired
	public void setItemChangeLogSearch(ItemChangeLogSearch search){
		this.itemChangeLogSearch = search;
	}
	
	@Autowired
	public void setItemChangeLogRepository(ItemChageLogRepositoryImpl repository){
		this.itemChangeLogRepository = repository;
	}
}
