/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.jvoliveira.sourceminer.domain.CaseMetricItem;
import br.com.jvoliveira.sourceminer.domain.MetricCase;

/**
 * @author Joao Victor
 *
 */
public interface CaseMetricItemRepository extends CrudRepository<CaseMetricItem, Long>{

	List<CaseMetricItem> findByMetricCase(MetricCase metricCase);
	
}
