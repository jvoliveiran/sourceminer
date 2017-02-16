/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.repositoryconnection;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.CoreMatchers.*;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.ProjectConfiguration;
import br.com.jvoliveira.sourceminer.domain.RepositoryConnector;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RepositoryLocation;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevisionItem;
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
	
	@Test
	public void testGetRevisionItensInProjectRange(){
		ProjectConfiguration config = new ProjectConfiguration();
		config.setSyncStartRevision("69adbcc323f35ceafdfe256bdb9d065e53896a93");
		config.setSyncEndRevision("073dde90d2592fc9bc26a79ecef5b2b0fe4bb396");
		try {
			repoGit.openConnection();
			List<RepositoryRevisionItem> result = repoGit.getRevisionItensInProjectRange(null, config);
			Assert.assertNotNull(result);
			Assert.assertThat(result.size(), is(equalTo(3)));
		} catch (RepositoryConnectionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetAllProjectRevision(){
		try {
			repoGit.openConnection();
			List<RepositoryRevision> result = repoGit.getAllProjectRevision(null);
			Assert.assertThat(result.size(), is(greaterThan(119)));
		} catch (RepositoryConnectionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetRevisionsInRange(){
		try {
			repoGit.openConnection();
			String from = "69adbcc323f35ceafdfe256bdb9d065e53896a93";
			String until = "073dde90d2592fc9bc26a79ecef5b2b0fe4bb396";
			List<RepositoryRevision> result = repoGit.getRevisionsInRange(null,from,until);
			Assert.assertThat(result.size(), is(equalTo(1)));
		} catch (RepositoryConnectionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetLastRevisionNumber(){
		try {
			repoGit.openConnection();
			String result = repoGit.getLastRevisionNumber(null);
			Assert.assertThat(result, is(equalTo("afbc97e5b7f392250cdf199bde2f77e0692f3a1b")));
		} catch (RepositoryConnectionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetFileContent(){
		String filePath = "SourceMiner/src/main/java/br/com/jvoliveira/sourceminer/component/repositoryconnection/RepositoryConnection.java";
		String revision = "6a4fc9196530eddd732ac1241bbd0e77fd3c3933";
		try{
			repoGit.openConnection();
			String result = repoGit.getFileContent(filePath, revision);
			Assert.assertNotNull(result);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetNextOutOfSyncRevisionToSync(){
		String revision = "6a4fc9196530eddd732ac1241bbd0e77fd3c3933";
		String expectedResult = "5940597901a44789084b0a060f39f7e36ed10b16";
		try{
			repoGit.openConnection();
			String result = repoGit.getNextOutOfSyncRevision(null, revision);
			Assert.assertNotNull(result);
			Assert.assertThat(result, is(equalTo(expectedResult)));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetNextOutOfSyncRevisionToSyncAgainstHeadRevision(){
		String revision = "afbc97e5b7f392250cdf199bde2f77e0692f3a1b";
		String expectedResult = "0";
		try{
			repoGit.openConnection();
			String result = repoGit.getNextOutOfSyncRevision(null, revision);
			Assert.assertNotNull(result);
			Assert.assertThat(result, is(equalTo(expectedResult)));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetNextOutOfSyncRevisionToSyncAgainstFirstRevision(){
		String revision = "";
		String expectedResult = "134c4e0bb11407dc3d93fc6742f825b6a56a7ded";
		try{
			repoGit.openConnection();
			String result = repoGit.getNextOutOfSyncRevision(null, revision);
			Assert.assertNotNull(result);
			Assert.assertThat(result, is(equalTo(expectedResult)));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetParentRevisionNumber(){
		String revision = "ef415ae004c730062d083eba5a220a1a45bbfc20";
		String expectedResult = "7e57de0cf5188cd6014280322e3d5913f7a3a793";
		try{
			repoGit.openConnection();
			String result = repoGit.getParentRevisionNumber(null, revision);
			Assert.assertNotNull(result);
			Assert.assertThat(result, is(equalTo(expectedResult)));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetNextRevisionToSync(){
		String revision = "1b0f8e4fceb2e189af574c353cf5b7cbca35f240";
		String expectedResult = "1b0f8e4fceb2e189af574c353cf5b7cbca35f240";
		try{
			repoGit.openConnection();
			String result = repoGit.getNextRevisionToSync(null, revision);
			Assert.assertNotNull(result);
			Assert.assertThat(result, is(equalTo(expectedResult)));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetNextRevisionToSyncFromBeginning(){
		String revision = "";
		String expectedResult = "134c4e0bb11407dc3d93fc6742f825b6a56a7ded";
		try{
			repoGit.openConnection();
			String result = repoGit.getNextRevisionToSync(null, revision);
			Assert.assertNotNull(result);
			Assert.assertThat(result, is(equalTo(expectedResult)));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
