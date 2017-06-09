/**
 * 
 */
package br.com.jvoliveira.arq.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import br.com.jvoliveira.arq.domain.ObjectDB;

/**
 * @author João Victor
 *
 */
@Repository
public class BatchDAO {

	@Qualifier("jpaSessionFactory")
	@Autowired
	private SessionFactory sessionFactory;
	private Session session;
	
	private int batchSize = 150;
	
	private Session getSession() {
    	if (session == null || !session.isOpen()) 
            session = sessionFactory.openSession();
    	
        if (session.getTransaction() == null || !session.getTransaction().getStatus().isOneOf(TransactionStatus.ACTIVE)) 
            session.beginTransaction();

        return session;
    }

	public <T extends ObjectDB> T batchSave(T dbObject, int externalCounter) {
		try{
			getSession().save(dbObject);
			if(externalCounter % batchSize == 0){
				System.out.println("C O M I T A N D O : " + externalCounter);
				getSession().getTransaction().commit();
			}
		}catch (Exception e) {
			getSession().getTransaction().rollback();
		}
		return dbObject;	
	}
}
