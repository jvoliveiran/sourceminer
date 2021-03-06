/**
 * 
 */
package br.com.jvoliveira.sourceminer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jvoliveira.arq.service.AbstractArqService;
import br.com.jvoliveira.sourceminer.component.javaparser.ChangeLogGroupModel;
import br.com.jvoliveira.sourceminer.component.javaparser.ClassParserHelper;
import br.com.jvoliveira.sourceminer.component.repositoryconnection.RepositoryConnectionSession;
import br.com.jvoliveira.sourceminer.domain.CaseMetricItem;
import br.com.jvoliveira.sourceminer.domain.ItemAsset;
import br.com.jvoliveira.sourceminer.domain.ItemChangeLog;
import br.com.jvoliveira.sourceminer.domain.Metric;
import br.com.jvoliveira.sourceminer.domain.ProjectConfiguration;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RevisionItemMetric;
import br.com.jvoliveira.sourceminer.domain.pojo.ItemAssetGroupType;
import br.com.jvoliveira.sourceminer.repository.CaseMetricItemRepository;
import br.com.jvoliveira.sourceminer.repository.ItemAssetRepository;
import br.com.jvoliveira.sourceminer.repository.ProjectConfigurationRepository;
import br.com.jvoliveira.sourceminer.repository.RepositoryItemRepository;
import br.com.jvoliveira.sourceminer.repository.RevisionItemMetricRepository;
import br.com.jvoliveira.sourceminer.repository.impl.ItemChageLogRepositoryImpl;
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
	private ItemAssetRepository itemAssetRepository;
	private RevisionItemMetricRepository itemMetricRepository;
	private ProjectConfigurationRepository projectConfigRepository;
	private CaseMetricItemRepository caseMetricRepository;
	
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
	
	public String getFileContentInRevision(String path, String revision){
			return this.connection.getConnection().getFileContent(path, revision);
	}
	
	public String getFileContentInLastRevision(RepositoryItem item){
		String headRevision = getLastRevisionNumber(item);
		return getFileContentInRevision(item.getPath(), headRevision);
	}
	
	private String getLastRevisionNumber(RepositoryItem item) {
		return ((RepositoryItemRepository)this.repository).findLastRevisionInFile(item.getPath(),item.getProject().getId());
	}

	public List<ChangeLogGroupModel> getChangeLogInRepositoryItem(RepositoryItem item){
		List<ItemChangeLog> changeLogs = null;
		
		if(getItemChangeLogFilter().hasFilterToSearch())
			changeLogs = itemChangeLogSearch.searchWithFilter();
		else
			changeLogs = this.itemChangeLogRepository.findAllChangeLogRepositoryItem(item);
		
		return classParserHelper.getChangeLogGroupByRevision(changeLogs);
	}
	
	public ItemAssetGroupType getItemAssetGroupType(RepositoryItem item){
		List<ItemAsset> assets = itemAssetRepository.findByRepositoryItemAndEnable(item, true);
		
		return new ItemAssetGroupType(assets);
	}
	
	public ItemChangeLogFilter getItemChangeLogFilter(){
		return (ItemChangeLogFilter) this.itemChangeLogSearch.getFilter();
	}
	
	public List<RevisionItemMetric> getRevisionItemMetric(RepositoryItem item){
		ProjectConfiguration projectConfig = projectConfigRepository.findOneByProject(item.getProject());
		
		List<CaseMetricItem> caseMetricItems = caseMetricRepository.findByMetricCase(projectConfig.getMetricCase());
		
		List<Metric> metrics = new ArrayList<>();
		
		caseMetricItems.stream().forEach(caseMetric -> metrics.add(caseMetric.getMetric()));
		
		return itemMetricRepository.findEachRecentMetric(item, metrics);
	}
	
	@Autowired
	public void setItemChangeLogSearch(ItemChangeLogSearch search){
		this.itemChangeLogSearch = search;
	}
	
	@Autowired
	public void setItemChangeLogRepository(ItemChageLogRepositoryImpl repository){
		this.itemChangeLogRepository = repository;
	}
	
	@Autowired
	public void setItemAssetRepository(ItemAssetRepository repository){
		this.itemAssetRepository = repository;
	}
	
	@Autowired
	public void setRevisionItemMetricRepository(RevisionItemMetricRepository repository){
		this.itemMetricRepository = repository;
	}
	
	@Autowired
	public void setProjectConfigRepository(ProjectConfigurationRepository repository){
		this.projectConfigRepository = repository;
	}
	
	@Autowired
	public void setCaseMetricItemRepository(CaseMetricItemRepository repository){
		this.caseMetricRepository = repository;
	}
}
