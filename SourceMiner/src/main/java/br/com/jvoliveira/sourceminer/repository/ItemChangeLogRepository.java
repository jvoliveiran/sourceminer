/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.jvoliveira.sourceminer.domain.ItemChangeLog;

/**
 * @author João Victor
 *
 */
public interface ItemChangeLogRepository extends CrudRepository<ItemChangeLog, Long>{

}
