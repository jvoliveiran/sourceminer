/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.jvoliveira.sourceminer.domain.ItemAsset;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;

/**
 * @author João Victor
 *
 */
public interface ItemAssetRepository extends CrudRepository<ItemAsset, Long>{

	List<ItemAsset> findByRepositoryItem(RepositoryItem repositoryItem);
	
}
