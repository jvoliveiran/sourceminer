/**
 * 
 */
package br.com.jvoliveira.sourceminer.search;

import javax.persistence.Query;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import br.com.jvoliveira.arq.search.AbstractArqSearch;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.search.filter.RepositoryRevisionFilter;

/**
 * @author Joao Victor
 *
 */
@Repository
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RepositoryRevisionSearch extends AbstractArqSearch<RepositoryRevision>{
	
	private Integer pageLimit = 10;

	@Override
	public void createSearchQuery(StringBuilder sql) {
		sql.append("SELECT rr FROM RepositoryRevision rr ");
		sql.append("WHERE 1=1 ");
		
		if(((RepositoryRevisionFilter)filter).isValidRevision())
			sql.append(" AND rr.revision = :revision ");
		
		if(((RepositoryRevisionFilter)filter).isValidAuthor())
			sql.append(" AND rr.author LIKE :author ");
		
		if(((RepositoryRevisionFilter)filter).isValidComment())
			sql.append(" AND rr.comment LIKE :comment ");
		
		if(((RepositoryRevisionFilter)filter).getProject() != null)
			sql.append(" AND rr.project = :project ");
	}

	@Override
	public void prepareQueryArgs(Query q) {
		if(((RepositoryRevisionFilter)filter).isValidRevision())
			q.setParameter("revision", ((RepositoryRevisionFilter)filter).getRevision());
		
		if(((RepositoryRevisionFilter)filter).isValidAuthor())
			q.setParameter("author", "%"+((RepositoryRevisionFilter)filter).getAuthor()+"%");
		
		if(((RepositoryRevisionFilter)filter).isValidComment())
			q.setParameter("comment", "%"+((RepositoryRevisionFilter)filter).getComment()+"%");
		
		if(((RepositoryRevisionFilter)filter).getProject() != null)
			q.setParameter("project", ((RepositoryRevisionFilter)filter).getProject());
		
	}
	
	public boolean isShowMoreRevisions(){
		return pageLimit > 10;
	}

	public Integer incrementPageLimit(){
		this.pageLimit = pageLimit + 10;
		return pageLimit;
	}
	
	public Integer getPageLimit(){
		return this.pageLimit;
	}
	
	public Integer resetPageLimit(){
		this.pageLimit = 10;
		return pageLimit;
	}
	
}
