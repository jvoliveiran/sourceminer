/**
 * 
 */
package br.com.jvoliveira.sourceminer.component;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import br.com.jvoliveira.sourceminer.domain.RepositoryConnector;
import br.com.jvoliveira.sourceminer.domain.enums.RepositoryLocation;
import br.com.jvoliveira.sourceminer.domain.enums.RepositoryVersionManager;

/**
 * @author Joao Victor
 *
 */
@Transactional
public class RepositoryConnectionSVNTest {

	private RepositoryConnector connector;
	private RepositoryConnectionSVN connection;
	
	@Before
	public void setup(){
		connector = new RepositoryConnector();
		connector.setUrl("file:///Users/MacBook/Documents/SVNRepo");
		connector.setLocation(RepositoryLocation.LOCAL);
		connector.setVersionManager(RepositoryVersionManager.SVN);
		
		connection = new RepositoryConnectionSVN(connector);
	}
	
	@After
	public void tearDown(){
		connection.closeConnection();
	}
	
	@Test
	public void testOpenConnection(){
		connection.openConnection();
		Assert.assertTrue(connection.isConnectionOpened());
	}
}
