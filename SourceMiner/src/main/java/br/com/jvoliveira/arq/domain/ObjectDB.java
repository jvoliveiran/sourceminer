/**
 * 
 */
package br.com.jvoliveira.arq.domain;

import java.util.Date;

/**
 * Interface que representa todos os objetos persistidos no banco
 * 
 * @author Joao Victor
 * 
 */
public interface ObjectDB {

	void setId(Long id);
	
	Long getId();
	
	void setCreateAt(Date date);
	
	Date getCreateAt();
	
	void setUpdateAt(Date date);
	
	Date getUpdateAt();
	
}
