/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import br.com.jvoliveira.sourceminer.domain.Feature;
import br.com.jvoliveira.sourceminer.domain.Project;

/**
 * @author João Victor
 *
 */
public interface FeatureRepository extends CrudRepository<Feature, Long>{

	Collection<Feature> findByPathAndProject(String path, Project project);
	
}
