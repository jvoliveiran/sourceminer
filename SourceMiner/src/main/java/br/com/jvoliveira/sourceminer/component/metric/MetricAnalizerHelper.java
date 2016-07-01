/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.metric;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.jvoliveira.config.ApplicationContextProvider;
import br.com.jvoliveira.sourceminer.domain.CaseMetricItem;
import br.com.jvoliveira.sourceminer.domain.Metric;
import br.com.jvoliveira.sourceminer.domain.MetricCase;
import br.com.jvoliveira.sourceminer.domain.RevisionItemMetric;


/**
 * @author Joao Victor
 *
 */
public class MetricAnalizerHelper {
	
	public static List<RevisionItemMetric> calculateMetrics(MetricCase metricCase, String fileContent){
		List<RevisionItemMetric> metricsResult = new ArrayList<RevisionItemMetric>();
		
		List<CaseMetricItem> caseMetricItens = metricCase.getCaseMetricItems();
		Iterator<CaseMetricItem> iteratorCaseMetricItem = caseMetricItens.iterator();
		
		while(iteratorCaseMetricItem.hasNext()){
			CaseMetricItem caseMetricItem = iteratorCaseMetricItem.next();
			RevisionItemMetric result = calculateMetric(caseMetricItem, fileContent);
			metricsResult.add(result);
		}	
		
		return metricsResult;
	}

	private static RevisionItemMetric calculateMetric(CaseMetricItem caseMetricItem, String fileContent) {
		Metric metric = caseMetricItem.getMetric();
		
		String beanName = metric.getBeanName();
		GenericMetric metricImpl = getMetricImpl(beanName);
		
		Double metricValue = metricImpl.calculate(fileContent);
		
		return createRevisionItemMetric(metric, metricValue);
	}

	private static GenericMetric getMetricImpl(String beanName) {
		return (GenericMetric) ApplicationContextProvider.getBean(beanName);
	}

	private static RevisionItemMetric createRevisionItemMetric(Metric metric, Double metricValue) {
		RevisionItemMetric revisionItemMetric = new RevisionItemMetric();
		
		revisionItemMetric.setMetric(metric);
		revisionItemMetric.setValue(metricValue);
		
		return revisionItemMetric;
	}
}
