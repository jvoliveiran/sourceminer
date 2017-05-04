/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository.impl;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.jvoliveira.sourceminer.domain.ItemAsset;
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
		String sql = "SELECT itemAsset.id_item_asset, itemAsset.id_repository_item "
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
		
		return query.getResultList();
	}

}
