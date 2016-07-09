/**
 * 
 */
package br.com.jvoliveira.sourceminer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jvoliveira.arq.service.AbstractArqService;
import br.com.jvoliveira.sourceminer.component.metric.MetricAnalizerHelper;
import br.com.jvoliveira.sourceminer.domain.CaseMetricItem;
import br.com.jvoliveira.sourceminer.domain.ProjectConfiguration;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevisionItem;
import br.com.jvoliveira.sourceminer.domain.RevisionItemMetric;
import br.com.jvoliveira.sourceminer.repository.CaseMetricItemRepository;
import br.com.jvoliveira.sourceminer.repository.RevisionItemMetricRepository;

/**
 * @author Joao Victor
 *
 */
@Service
public class RevisionItemMetricService extends AbstractArqService<RevisionItemMetric> {

	private CaseMetricItemRepository caseMetricItemRepository;
	
	@Autowired
	public RevisionItemMetricService(RevisionItemMetricRepository repository, CaseMetricItemRepository caseMetricItemRepository){
		this.repository = repository;
		this.caseMetricItemRepository = caseMetricItemRepository;
	}
	
	public List<RevisionItemMetric> generateSingleRevisionItemMetric(String fileContent, 
			RepositoryRevisionItem revisionItem, ProjectConfiguration config){
		List<RevisionItemMetric> metricResults = new ArrayList<RevisionItemMetric>();
		
		if(config.getMetricCase() != null){
			List<CaseMetricItem> caseMetricItems = caseMetricItemRepository.findByMetricCase(config.getMetricCase());
			metricResults = MetricAnalizerHelper.calculateMetrics(caseMetricItems, fileContent);
			
			metricResults.stream().forEach(metricResult -> persistMetricResult(metricResult,revisionItem));
		}
		
		return metricResults;
	}

	private RevisionItemMetric persistMetricResult(RevisionItemMetric metricResult, RepositoryRevisionItem revisionItem) {
		metricResult.setRevisionItem(revisionItem);
		this.repository.save(metricResult);
		return metricResult;
	}
	
}
