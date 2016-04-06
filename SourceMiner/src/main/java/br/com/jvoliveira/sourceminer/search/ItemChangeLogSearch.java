/**
 * 
 */
package br.com.jvoliveira.sourceminer.search;

import javax.persistence.Query;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import br.com.jvoliveira.arq.search.AbstractArqSearch;
import br.com.jvoliveira.sourceminer.domain.ItemChangeLog;
import br.com.jvoliveira.sourceminer.search.filter.ItemChangeLogFilter;

/**
 * @author Joao Victor
 *
 */
@Repository
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ItemChangeLogSearch extends AbstractArqSearch<ItemChangeLog>{

	@Override
	public void createSearchQuery(StringBuilder sql) {
		sql.append(" SELECT changeLog FROM ItemChangeLog AS changeLog ");
		sql.append(" JOIN changeLog.asset AS asset ");
		sql.append(" JOIN changeLog.revisionItem AS revisionItem ");
		sql.append(" JOIN revisionItem.repositoryItem AS repositoryItem ");
		sql.append(" WHERE 1=1 ");
		
		if(((ItemChangeLogFilter)filter).isValidChangeFileType())
			sql.append(" AND changeLog.changeType LIKE :changeFileType ");
		
		if(((ItemChangeLogFilter)filter).isValidAssetType())
			sql.append(" AND asset.assetType LIKE :assetType ");
		
		if(((ItemChangeLogFilter)filter).getItem() != null)
			sql.append(" AND repositoryItem = :item ");
		
	}

	@Override
	public void prepareQueryArgs(Query q) {
		if(((ItemChangeLogFilter)filter).isValidChangeFileType())
			q.setParameter("changeFileType", ((ItemChangeLogFilter)filter).getChangeFileType());
		
		if(((ItemChangeLogFilter)filter).isValidAssetType())
			q.setParameter("assetType", ((ItemChangeLogFilter)filter).getAssetType());
		
		if(((ItemChangeLogFilter)filter).getItem() != null)
			q.setParameter("item", ((ItemChangeLogFilter)filter).getItem());
	}

}
