/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.jvoliveira.sourceminer.domain.Metric;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RevisionItemMetric;
import br.com.jvoliveira.sourceminer.repository.custom.RevisionItemMetricRepositoryCustom;

/**
 * @author Joao Victor
 *
 */
public class RevisionItemMetricRepositoryImpl implements RevisionItemMetricRepositoryCustom{

	private EntityManager entityManager;

	@Autowired
	public RevisionItemMetricRepositoryImpl(EntityManager manager){
		this.entityManager = manager;
	}
	
	@Override
	public RevisionItemMetric findSpecificRecentMetric(RepositoryItem item, Metric metric) {
		
		String hql = " SELECT obj FROM RevisionItemMetric obj "
				+ " JOIN obj.metric metric"
				+ " JOIN obj.revisionItem revisionItem "
				+ " JOIN revisionItem.repositoryItem item "
				+ " WHERE item.id = :idItem "
				+ " AND metric.id = :idMetric "
				+ " ORDER BY obj.id DESC ";
		
		Query query = entityManager.createQuery(hql).setFirstResult(0).setMaxResults(1);
		query.setParameter("idItem", item.getId());
		query.setParameter("idMetric", metric.getId());
		
		return (RevisionItemMetric) query.getSingleResult();
	}

	@Override
	public List<RevisionItemMetric> findEachRecentMetric(RepositoryItem item, List<Metric> metrics) {
		List<RevisionItemMetric> result = new ArrayList<RevisionItemMetric>();
		
		metrics.stream().forEach(metric -> findAndGroupEachRecentMetric(item,metric,result));
		
		return result;
	}

	private Object findAndGroupEachRecentMetric(RepositoryItem item,
			Metric metric, List<RevisionItemMetric> result) {
		
		RevisionItemMetric itemMetric = findSpecificRecentMetric(item, metric);
		
		if(itemMetric != null)
			result.add(itemMetric);
		
		return null;
	}
	
}
