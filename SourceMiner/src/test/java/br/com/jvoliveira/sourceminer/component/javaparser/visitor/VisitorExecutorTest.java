/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser.visitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author João Victor
 *
 */
public class VisitorExecutorTest {

	private VisitorExecutor executor;
	private String fileContent;
	private String filePath;
	
	@Before
	public void setUp() throws IOException, IllegalAccessException, InstantiationException{
		filePath = "src/main/resources/test/nova_classe_teste.txt";
		File fileTXT = new File(filePath);
		fileContent = new String(Files.readAllBytes(Paths.get(fileTXT.getAbsolutePath())));
		executor = new VisitorExecutor();
		executor.addVisitorByClass(FieldDeclarationVisitor.class);
		executor.addVisitorByClass(VariableDeclaratorVisitor.class);
		executor.addVisitorByClass(MethodCallVisitor.class);
	}
	
	@After
	public void tearDown(){
		
	}
	
	@Test
	public void executeTest(){
		executor.executeVisitorChain(fileContent, filePath, null);
	}
}
