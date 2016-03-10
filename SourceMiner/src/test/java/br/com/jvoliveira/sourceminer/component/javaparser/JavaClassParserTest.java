/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser;

import java.util.List;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;

import br.com.jvoliveira.sourceminer.domain.ItemAsset;

/**
 * @author João Victor
 *
 */
public class JavaClassParserTest {

	private JavaClassParser classParser;
	
	@Before
	public void setUp(){
		String fileContent = "";
		classParser = new JavaClassParser(fileContent);
	}
	
	@Test
	public void testGetMethods(){
		List<ItemAsset> assets = classParser.parserMethods();
		
		Assert.assertNotNull(assets);
		Assert.assertTrue(assets.size() > 0);
	}
}
