/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.jvoliveira.sourceminer.domain.RepositoryRevisionItem;

/**
 * @author Joao Victor
 *
 */
public interface RepositoryRevisionItemRepository extends CrudRepository<RepositoryRevisionItem, Long>{

}
