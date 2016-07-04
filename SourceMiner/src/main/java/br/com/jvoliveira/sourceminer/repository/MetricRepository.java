/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.jvoliveira.sourceminer.domain.Metric;

/**
 * @author Joao Victor
 *
 */
public interface MetricRepository extends CrudRepository<Metric,Long>{

}
