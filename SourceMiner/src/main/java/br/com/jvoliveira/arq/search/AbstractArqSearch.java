/**
 * 
 */
package br.com.jvoliveira.arq.search;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.jvoliveira.arq.domain.ObjectDB;


/**
 * @author Joao Victor
 *
 */
public abstract class AbstractArqSearch<T extends ObjectDB> {
	
	protected Class<T> typeClass;
	
	protected List<T> result;
	
	protected EntityManager entityManager;
	
	protected StringBuilder sql;
	
	public abstract void createSearchQuery(StringBuilder sql);
	public abstract void prepareQueryArgs(Query q);
	
	@SuppressWarnings("unchecked")
	public AbstractArqSearch(){
		this.typeClass = (Class<T>) (
	               (ParameterizedType) getClass().getGenericSuperclass())
	              .getActualTypeArguments()[0];
		
		init();
	}
	
	protected void init(){
		this.result = new ArrayList<>();
	}
	
	public List<T> searchWithFilter(){
		
		sql = new StringBuilder();
		
		createSearchQuery(sql);
		
		Query query = entityManager.createQuery(sql.toString());
		
		prepareQueryArgs(query);
		
		result = query.getResultList();
		
		return result;
	}
	
	public boolean hasResult(){
		if(result != null && result.size() > 0)
			return true;
		return false;
	}
	
	@Autowired
	public void setEntityManager(EntityManager em){
		this.entityManager = em;
	}
	public List<T> getResult() {
		return result;
	}
	
}
