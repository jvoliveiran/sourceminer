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

import br.com.jvoliveira.sourceminer.component.repositoryconnection.RepositoryConnectionSVN;
import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryConnector;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.domain.enums.RepositoryLocationType;
import br.com.jvoliveira.sourceminer.domain.enums.RepositoryVersionManager;
import br.com.jvoliveira.sourceminer.exceptions.RepositoryConnectionException;

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
		connector.getRepositoryLocation().setUrl("file:///Users/MacBook/Documents/SVNRepo");
		connector.getRepositoryLocation().setLocation(RepositoryLocationType.LOCAL);
		connector.getRepositoryLocation().setVersionManager(RepositoryVersionManager.SVN);
		
		connection = new RepositoryConnectionSVN(connector);
		
		project = new Project();
		//project.setRepositoryConnector(connector);
		project.setPath("/jgitcomponent");
		
		testOpenConnection();
	}
	
	@After
	public void tearDown(){
		connection.closeConnection();
	}
	
	@Test
	public void testOpenConnection(){
		try {
			connection.openConnection();
		} catch (RepositoryConnectionException e) {
			e.printStackTrace();
		}
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
		List<RepositoryRevision> repositoryRevision = connection.getRevisionsInRange(project, String.valueOf(-1), String.valueOf(-1));
		Long lastRevision = Long.valueOf(repositoryRevision.get(0).getRevision());
		Assert.assertNotNull(repositoryRevision);
		Assert.assertEquals((new Long(15)).longValue(), lastRevision.longValue());
	}
}
