/**
 * 
 */
package br.com.jvoliveira.sourceminer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jvoliveira.arq.service.AbstractArqService;
import br.com.jvoliveira.sourceminer.domain.Metric;
import br.com.jvoliveira.sourceminer.domain.MetricCase;
import br.com.jvoliveira.sourceminer.repository.MetricCaseRepository;
import br.com.jvoliveira.sourceminer.repository.MetricRepository;

/**
 * @author Joao Victor
 *
 */
@Service
public class MetricCaseService extends AbstractArqService<MetricCase>{

	
	private MetricRepository metricRepository;
	
	@Autowired
	public MetricCaseService(MetricCaseRepository repository, MetricRepository metricRepository){
		this.repository = repository;
		this.metricRepository = metricRepository;
	}
	
	public List<Metric> getAllMetrics(){
		return (List<Metric>) metricRepository.findAll();
	}
	
	private MetricCaseRepository getRepository(){
		return (MetricCaseRepository) this.repository;
	}
}
