/**
 * 
 */
package br.com.jvoliveira.sourceminer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jvoliveira.arq.service.AbstractArqService;
import br.com.jvoliveira.arq.utils.ArqUtils;
import br.com.jvoliveira.sourceminer.domain.CaseMetricItem;
import br.com.jvoliveira.sourceminer.domain.Metric;
import br.com.jvoliveira.sourceminer.domain.MetricCase;
import br.com.jvoliveira.sourceminer.repository.CaseMetricItemRepository;
import br.com.jvoliveira.sourceminer.repository.MetricCaseRepository;
import br.com.jvoliveira.sourceminer.repository.MetricRepository;

/**
 * @author Joao Victor
 *
 */
@Service
public class MetricCaseService extends AbstractArqService<MetricCase>{

	private MetricRepository metricRepository;
	private CaseMetricItemRepository caseMetricItemRepository;
	
	@Autowired
	public MetricCaseService(MetricCaseRepository repository, MetricRepository metricRepository,
			CaseMetricItemRepository caseMetricItemRepository){
		this.repository = repository;
		this.metricRepository = metricRepository;
		this.caseMetricItemRepository = caseMetricItemRepository;
	}
	
	public List<Metric> getAllMetrics(){
		return (List<Metric>) metricRepository.findAll();
	}
	
	public CaseMetricItem parseItemMetric(String idMetric, String valueMetric) {
		CaseMetricItem caseMetric = new CaseMetricItem();
		
		Metric metric = getMetric(Long.parseLong(idMetric));
		
		caseMetric.setId(ArqUtils.generateRandomNegative());
		caseMetric.setMetric(metric);
		caseMetric.setThreshold(Double.parseDouble(valueMetric));
		
		return caseMetric;
	}
	
	@Override
	protected MetricCase createObject(MetricCase obj) {
		MetricCase metricCase =  super.createObject(obj);
		
		List<CaseMetricItem> caseMetricItemList = metricCase.getCaseMetricItems();
		
		caseMetricItemList.stream().forEach(item -> saveCaseMetricItem(item, metricCase));			
		
		return metricCase;
	}

	private Object saveCaseMetricItem(CaseMetricItem item, MetricCase metricCase) {
		item.setMetricCase(metricCase);
		caseMetricItemRepository.save(item);
		return item;
	}
	
	public Metric getMetric(Long idMetric){
		return metricRepository.findOne(idMetric);
	}
}
