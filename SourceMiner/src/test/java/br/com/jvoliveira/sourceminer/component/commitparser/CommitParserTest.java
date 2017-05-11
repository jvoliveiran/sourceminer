/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.commitparser;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.util.Collection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.jvoliveira.sourceminer.domain.Feature;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.domain.Task;

/**
 * @author João Victor
 *
 */

public class CommitParserTest {

	private CommitParser parser;
	private RepositoryRevision revision;
	
	@Before
	public void setup(){
		parser = new SimpleCommitParser();
		revision = new RepositoryRevision();
	}
	
	@After
	public void tearDown(){		
	}
	
	@Test
	public void testExtractTaskFromMessage(){
		revision.setComment(" #1 test \n #325 @path/to/uc");
		Collection<Task> tasks = parser.extractTaskFromMessage(revision);
		Assert.assertThat(tasks.size(), is(equalTo(2)));
	}
	
	@Test
	public void testExtractFeatureFromMessage(){
		revision.setComment("#1 test \n #325 @path/to/uc #12 @path/to/uc #1 ");
		Collection<Feature> features = parser.extractFeatureFromMessage(revision);
		Assert.assertThat(features.size(), is(equalTo(2)));
	}
}
