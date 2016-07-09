/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository.custom;

import java.util.List;

import br.com.jvoliveira.sourceminer.domain.Metric;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RevisionItemMetric;

/**
 * @author Joao Victor
 *
 */
public interface RevisionItemMetricRepositoryCustom {

	List<RevisionItemMetric> findEachRecentMetric(RepositoryItem item, List<Metric> metrics);

	RevisionItemMetric findSpecificRecentMetric(RepositoryItem item, Metric metric);
	
}
