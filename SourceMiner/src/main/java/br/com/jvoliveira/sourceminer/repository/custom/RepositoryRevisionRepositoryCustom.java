/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository.custom;

import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;

/**
 * @author João Victor
 *
 */
public interface RepositoryRevisionRepositoryCustom {

	public RepositoryRevision findByProjectAndRevisionOptimized(Project project, String revision);
	
}
