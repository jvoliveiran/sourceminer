/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.repositoryconnection;

import static org.hamcrest.Matchers.*;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryConnector;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RepositoryLocation;
import br.com.jvoliveira.sourceminer.domain.enums.RepositoryLocationType;
import br.com.jvoliveira.sourceminer.exceptions.RepositoryConnectionException;

/**
 * @author Joao Victor
 *
 */
public class RepositoryConnectionGITTest {

	private RepositoryConnectionGIT repoGit;
	private RepositoryConnector connector;
	
	@Before
	public void setup(){
		repoGit = new RepositoryConnectionGIT();
		connector = new RepositoryConnector();
		connector.setUsername("jvoliveiran@gmail.com");
		connector.setPassword("");
		connector.setName("Test-Sourcerminer");
		
		RepositoryLocation repoLocation = new RepositoryLocation();
		repoLocation.setUrl("https://github.com/jvoliveiran/sourceminer");
		repoLocation.setLocationType(RepositoryLocationType.REMOTE);
		
		connector.setRepositoryLocation(repoLocation);
		
		repoGit.setConnector(connector);
	}
	
	@After
	public void tearDown(){
		
	}
	
	@Test
	public void testConnectionGit(){
		try {
			repoGit.testConnection();
		} catch (RepositoryConnectionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetAllProjectItens(){
		Project project = new Project();
		project.setPath("refs/heads/master");
		try {
			repoGit.openConnection();
			List<RepositoryItem> result = repoGit.getAllProjectItens(project);
			Assert.assertThat(result.size(), greaterThan(100));
			Assert.assertNotNull(result);
		} catch (RepositoryConnectionException e) {
			e.printStackTrace();
		}
	}
	
}
