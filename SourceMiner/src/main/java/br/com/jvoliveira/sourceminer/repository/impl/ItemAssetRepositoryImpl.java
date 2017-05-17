/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.jvoliveira.sourceminer.domain.ItemAsset;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.repository.custom.ItemAssetRepositoryCustom;

/**
 * @author João Victor
 *
 */
@Repository
public class ItemAssetRepositoryImpl implements ItemAssetRepositoryCustom{

	private EntityManager manager;
	
	@Autowired
	public ItemAssetRepositoryImpl(EntityManager manager) {
		this.manager = manager;
	}
	
	@Override
	public Collection<ItemAsset> methodChangedInRevision(String revision, Long projectId) {
		String sql = "SELECT itemAsset.id_item_asset, itemAsset.signature, itemAsset.id_repository_item, item.name "
					+ " FROM item_asset itemAsset "
					+ " INNER JOIN item_change_log assetChange USING(id_item_asset) "
					+ " INNER JOIN repository_revision_item revisionItem USING(id_repository_revision_item) "
					+ " INNER JOIN repository_revision rev USING(id_repository_revision) "
					+ " INNER JOIN repository_item item ON item.id_repository_item = itemAsset.id_repository_item "
					+ " WHERE rev.revision = :revision "
					+ " AND rev.id_project = :projectId "
					+ " AND itemAsset.asset_type = 'METHOD' "
					+ " AND assetChange.change_file_type = 'UPDATED' "
					+ " AND itemAsset.enable = true";
		Query query = manager.createNativeQuery(sql);
		query.setParameter("revision", revision);
		query.setParameter("projectId", projectId);
		
		List<Object[]> result = query.getResultList();
		Collection<ItemAsset> output = new ArrayList<>();
		buildItemAsset(result, output);
		return output;
	}

	private void buildItemAsset(List<Object[]> result, Collection<ItemAsset> output) {
		for(Object[] resultItem : result){
			ItemAsset asset = new ItemAsset();
			asset.setId(((BigInteger) resultItem[0]).longValue());
			asset.setSignature((String) resultItem[1]);
			asset.setRepositoryItem(new RepositoryItem());
			asset.getRepositoryItem().setId(((BigInteger) resultItem[2]).longValue());
			asset.getRepositoryItem().setName((String) resultItem[3]);
			output.add(asset);
		}
	}

}
