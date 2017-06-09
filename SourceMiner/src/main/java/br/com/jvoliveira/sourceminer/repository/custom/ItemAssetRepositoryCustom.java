package br.com.jvoliveira.sourceminer.repository.custom;

import java.util.Collection;
import java.util.List;

import br.com.jvoliveira.sourceminer.domain.ItemAsset;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;

/**
 * 
 * @author João Victor
 *
 */
public interface ItemAssetRepositoryCustom {

	Collection<ItemAsset> methodChangedInRevision(String revision, Long projectId);
	List<ItemAsset> findByRepositoryItemAndEnableOptimized(RepositoryItem repositoryItem, Boolean enable);
	
}
