/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.commitparser;

import java.util.Collection;

import br.com.jvoliveira.sourceminer.domain.Feature;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.domain.Task;

/**
 * @author João Victor
 *
 */
public interface CommitParser {

	public Collection<Task> extractTaskFromMessage(RepositoryRevision revision);
	
	public Collection<Feature> extractFeatureFromMessage(RepositoryRevision revision);
	
}
