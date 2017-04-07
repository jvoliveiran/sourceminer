/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.util.List;

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

/**
 * @author João Victor
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class SyncLogRepositoryTest {

	@Autowired
	private SyncLogRepositoryImpl repository;
	
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
	public void findTwoMostRecentRevisionsTest(){
		List<String> result = repository.findTwoMostRecentRevisions(project);
		Assert.assertThat(result.size(), is(equalTo(1)));
	}
	
	@Test
	public void countSyncLogByProjectTest(){
		Integer total = repository.countSyncLogByProject(project);
		Assert.assertNotNull(total);
	}
}
