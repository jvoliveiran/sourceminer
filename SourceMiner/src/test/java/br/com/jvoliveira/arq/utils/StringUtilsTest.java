/**
 * 
 */
package br.com.jvoliveira.arq.utils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Joao Victor
 *
 */
public class StringUtilsTest {

	@Before
	public void setup(){
		
	}
	
	@After
	public void tearDown(){
		
	}
	
	@Test
	public void testGetFileNameInPath(){
		String path = "/jgitcomponent/src/main/java/br/com/jvoliveira/svnkitcomponent/SVNKitHelper.java";
		String fileName = StringUtils.getFileNameInPath(path, "/");
		Assert.assertEquals("SVNKitHelper.java", fileName);
	}
	
	@Test
	public void testGetExtensionInFileName(){
		String fileNameWithExtension = "App.java";
		String extension = StringUtils.getExtensionInFileName(fileNameWithExtension);
		Assert.assertEquals("java", extension);
	}
}
