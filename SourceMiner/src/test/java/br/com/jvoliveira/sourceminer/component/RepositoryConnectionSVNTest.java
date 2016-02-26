/**
 * 
 */
package br.com.jvoliveira.sourceminer.component;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryConnector;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
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
	
	private Project project;
	
	@Before
	public void setup(){
		connector = new RepositoryConnector();
		connector.setUrl("file:///Users/MacBook/Documents/SVNRepo");
		connector.setLocation(RepositoryLocation.LOCAL);
		connector.setVersionManager(RepositoryVersionManager.SVN);
		
		connection = new RepositoryConnectionSVN(connector);
		
		project = new Project();
		project.setRepositoryConnector(connector);
		project.setPath("/jgitcomponent");
		
		testOpenConnection();
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
	
	@Test
	public void testGetAllRepositoryItens(){
		List<RepositoryItem> repositoryItens = connection.getAllProjectItens(project);
		Assert.assertNotNull(repositoryItens);
	}
	
	@Test
	public void testGetAllProjectRevisions(){
		List<RepositoryRevision> repositoryRevision = connection.getAllProjectRevision(project);
		Assert.assertNotNull(repositoryRevision);
	}
	
	@Test
	public void testGetLastProjectRevision(){
		List<RepositoryRevision> repositoryRevision = connection.getRevisionsInRange(project, -1, -1);
		Long lastRevision = repositoryRevision.get(0).getRevision();
		Assert.assertNotNull(repositoryRevision);
		Assert.assertEquals((new Long(15)).longValue(), lastRevision.longValue());
	}
}