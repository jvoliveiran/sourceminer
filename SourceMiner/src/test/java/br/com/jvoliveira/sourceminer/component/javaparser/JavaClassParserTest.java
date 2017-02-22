/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
	private String fileContent;
	private String filePath;
	
	@Before
	public void setUp(){
		try {
			File fileTXT = new File("src/main/resources/test/stringutils.txt");
			fileContent = new String(Files.readAllBytes(Paths.get(fileTXT.getAbsolutePath())));
		} catch (IOException e) {
			fileContent = "";
		}
		
		classParser = new JavaClassParser(fileContent, filePath);
	}
	
	@Test
	public void testGetMethods(){
		List<ItemAsset> assets = classParser.parserMethods();
		
		Assert.assertNotNull(assets);
		//Assert.assertTrue(assets.size() == 2);
	}
	
	@Test
	public void testGetAllAttributes(){
		List<ItemAsset> assets = classParser.parserField();
		
		Assert.assertTrue(assets.size() == 3);
	}
	
}
