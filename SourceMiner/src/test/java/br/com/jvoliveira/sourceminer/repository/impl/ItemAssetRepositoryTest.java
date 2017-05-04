/**
 * 
 */
package br.com.jvoliveira.sourceminer.repository.impl;

import static org.hamcrest.CoreMatchers.*;

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

import br.com.jvoliveira.sourceminer.domain.ItemAsset;
import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.repository.ItemAssetRepository;

/**
 * @author João Victor
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class ItemAssetRepositoryTest {

	private Project project;
	private String revision;
	
	@Autowired
	private ItemAssetRepository repository;
	
	@Before
	public void setUp(){
		project = new Project();
		project.setId(2L);
		revision = "f929f6f530933f68bcae3647261d4d12eb89b1de";
	}
	
	@After
	public void tearDown(){
		
	}
	
	@Test
	public void methodChangedInRevisionTest(){
		Collection<ItemAsset> result = repository.methodChangedInRevision(revision, project.getId());
		Assert.assertThat(result.size(), is(equalTo(11)));
	}
	
}
