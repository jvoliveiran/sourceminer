/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.metric;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * @author Joao Victor
 *
 */
public class LOCMetricTest {

	private String fileContent;
	
	@Before
	public void setUp(){
		fileContent = getFileContent("src/main/resources/test/simple_class.txt");
	}
	
	@After
	public void tearDown(){
		
	}
	
	@Test
	public void testCalculate(){
		GenericMetric metric = new LOCMetric();
		Double value = metric.calculate(fileContent);
		
		Assert.assertThat(value, CoreMatchers.is(4D));
	}
	
	@Test
	public void testCalculateBigClass(){
		fileContent = getFileContent("src/main/resources/test/classe_teste.txt");
		GenericMetric metric = new LOCMetric();
		Double value = metric.calculate(fileContent);
		
		Assert.assertThat(value, CoreMatchers.is(37D));
	}
	
	private String getFileContent(String filePath){
		try {
			File fileTXT = new File(filePath);
			return new String(Files.readAllBytes(Paths.get(fileTXT.getAbsolutePath())));
		} catch (IOException e) {
			return "";
		}
	}
	
}
