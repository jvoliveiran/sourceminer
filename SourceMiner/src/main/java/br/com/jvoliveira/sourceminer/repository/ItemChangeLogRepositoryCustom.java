/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository;

import java.util.List;

import br.com.jvoliveira.sourceminer.domain.ItemChangeLog;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;

/**
 * @author João Victor
 *
 */
public interface ItemChangeLogRepositoryCustom {

	List<ItemChangeLog> findAllChangeLogRepositoryItem(RepositoryItem repositoryItem);
	
}
