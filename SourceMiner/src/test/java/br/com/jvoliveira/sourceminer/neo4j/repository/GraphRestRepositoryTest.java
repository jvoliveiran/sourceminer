/**
 * 
 */
package br.com.jvoliveira.sourceminer.neo4j.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.util.Collection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.neo4j.domain.ClassNode;

/**
 * @author João Victor
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class GraphRestRepositoryTest {

	@Autowired
	private GraphRestRepository repository;

	private Project project;
	
	@Before
	public void setUp(){
		project = new Project();
		project.setId(2L);
	}
	
	@After
	public void tearDown(){
		
	}
	
	@Test
	public void findDependencyGraphCallFromTest(){
		String className = "ProjectController.java";
		Collection<ClassNode> nodes = repository.findDependecyGraphCallFrom(className,project.getId());
		Assert.assertNotNull(nodes);
		Assert.assertThat(nodes.size(), is(equalTo(4)));
	}
	
	@Test
	public void findDependencyGraphCallToTest(){
		String className = "SyncGraphService.java";
		Collection<ClassNode> nodes = repository.findDependecyGraphCallTo(className,project.getId());
		Assert.assertNotNull(nodes);
		Assert.assertThat(nodes.size(), is(equalTo(3)));
	}
}
