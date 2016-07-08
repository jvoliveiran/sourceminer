/**
 * 
 */
package br.com.jvoliveira.sourceminer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jvoliveira.arq.service.AbstractArqService;
import br.com.jvoliveira.sourceminer.component.metric.MetricAnalizerHelper;
import br.com.jvoliveira.sourceminer.domain.ProjectConfiguration;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevisionItem;
import br.com.jvoliveira.sourceminer.domain.RevisionItemMetric;
import br.com.jvoliveira.sourceminer.repository.RevisionItemMetricRepository;

/**
 * @author Joao Victor
 *
 */
@Service
public class RevisionItemMetricService extends AbstractArqService<RevisionItemMetric> {

	@Autowired
	public RevisionItemMetricService(RevisionItemMetricRepository repository){
		this.repository = repository;
	}
	
	public List<RevisionItemMetric> generateSingleRevisionItemMetric(String fileContent, 
			RepositoryRevisionItem revisionItem, ProjectConfiguration config){
		
		//TODO: Carregar metricas para evitar erro de lazy
		List<RevisionItemMetric> metricResults = MetricAnalizerHelper.calculateMetrics(config.getMetricCase(), fileContent);
		
		metricResults.stream().forEach(metricResult -> persistMetricResult(metricResult,revisionItem));
		
		return metricResults;
	}

	private RevisionItemMetric persistMetricResult(RevisionItemMetric metricResult, RepositoryRevisionItem revisionItem) {
		metricResult.setRevisionItem(revisionItem);
		this.repository.save(metricResult);
		return metricResult;
	}
	
}
