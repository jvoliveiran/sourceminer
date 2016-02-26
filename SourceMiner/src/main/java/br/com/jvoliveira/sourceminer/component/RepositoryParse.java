/**
 * 
 */
package br.com.jvoliveira.sourceminer.component;

import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevisionItem;

/**
 * @author Joao Victor
 *
 */
public interface RepositoryParse {

	<T> RepositoryItem parseToRepositoryItem(T entry);
	
	<T> RepositoryRevision parseToRepositoryRevision(T entry);
	
	<T> RepositoryRevisionItem parseToRepositoryRevisionItem(T entry);
}
