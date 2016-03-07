package br.com.jvoliveira.sourceminer.search;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.com.jvoliveira.arq.search.AbstractArqSearch;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.search.filter.RepositoryItemFilter;

/**
 * 
 */

/**
 * @author Joao Victor
 *
 */
@Repository
public class RepositoryItemSearch extends AbstractArqSearch<RepositoryItem>{

	@Override
	public void createSearchQuery(StringBuilder sql) {
		sql.append("SELECT ri FROM RepositoryItem ri ");
		sql.append("WHERE 1=1 ");
		
		if(((RepositoryItemFilter)filter).isValidName())
			sql.append(" AND ri.name LIKE :name ");
		
		if(((RepositoryItemFilter)filter).getProject() != null)
			sql.append(" AND ri.project = :project ");
			
	}
	
	@Override
	public void prepareQueryArgs(Query q) {
		if(((RepositoryItemFilter)filter).isValidName())
			q.setParameter("name", "%"+((RepositoryItemFilter)filter).getName()+"%");
		
		if(((RepositoryItemFilter)filter).getProject() != null)
			q.setParameter("project", ((RepositoryItemFilter)filter).getProject());
	}
	
	
}
