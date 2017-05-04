package br.com.jvoliveira.sourceminer.repository.custom;

import java.util.Collection;

import br.com.jvoliveira.sourceminer.domain.ItemAsset;

/**
 * 
 * @author João Victor
 *
 */
public interface ItemAssetRepositoryCustom {

	Collection<ItemAsset> methodChangedInRevision(String revision, Long projectId);
	
}
