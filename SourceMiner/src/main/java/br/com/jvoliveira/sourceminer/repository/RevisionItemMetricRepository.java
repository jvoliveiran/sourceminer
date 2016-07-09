package br.com.jvoliveira.sourceminer.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.jvoliveira.sourceminer.domain.RevisionItemMetric;
import br.com.jvoliveira.sourceminer.repository.custom.RevisionItemMetricRepositoryCustom;

/**
 * 
 * @author Joao Victor
 *
 */
public interface RevisionItemMetricRepository extends CrudRepository<RevisionItemMetric, Long>, RevisionItemMetricRepositoryCustom{

}
