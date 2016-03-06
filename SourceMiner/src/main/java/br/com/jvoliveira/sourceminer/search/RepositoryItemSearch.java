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

	private RepositoryItemFilter filter;
	
	public void setFilter(RepositoryItemFilter filter){
		this.filter = filter;
	}
	
	@Override
	public void createSearchQuery(StringBuilder sql) {
		sql.append("SELECT ri FROM RepositoryItem ri ");
		sql.append("WHERE 1=1 ");
		
		if(filter.isValidName())
			sql.append(" AND ri.name LIKE :name ");
			
	}
	
	@Override
	public void prepareQueryArgs(Query q) {
		if(filter.isValidName())
			q.setParameter("name", "%"+filter.getName()+"%");
	}
	
	
}
