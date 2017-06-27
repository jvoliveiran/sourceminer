/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.jvoliveira.sourceminer.domain.ItemAsset;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.enums.AssetType;
import br.com.jvoliveira.sourceminer.repository.custom.ItemAssetRepositoryCustom;

/**
 * @author João Victor
 *
 */
public interface ItemAssetRepository extends CrudRepository<ItemAsset, Long>, ItemAssetRepositoryCustom{

	List<ItemAsset> findByRepositoryItemAndEnable(RepositoryItem repositoryItem, Boolean enable);
	List<ItemAsset> findByRepositoryItemAndEnableAndAssetType(RepositoryItem repositoryItem, Boolean enable, AssetType assetType);
	List<ItemAsset> findByRepositoryItemAndEnableAndAssetTypeIn(RepositoryItem repositoryItem, Boolean enable, Collection<AssetType> assetType);
	List<ItemAsset> findByRepositoryItemAndEnableAndAssetTypeAndName(RepositoryItem repositoryItem, Boolean enable, AssetType assetType, String name);
	
}
