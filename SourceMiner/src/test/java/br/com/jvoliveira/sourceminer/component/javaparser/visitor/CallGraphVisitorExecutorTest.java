/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser.visitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author João Victor
 *
 */
public class CallGraphVisitorExecutorTest {

	private String fileContent;
	private String filePath;
	
	@Before
	public void setUp() throws IOException, IllegalAccessException, InstantiationException{
		filePath = "src/main/resources/test/nova_classe_teste3.txt";
		File fileTXT = new File(filePath);
		fileContent = new String(Files.readAllBytes(Paths.get(fileTXT.getAbsolutePath())));
	}
	
	@After
	public void tearDown(){
		
	}
	
	@Test
	public void processCallGraphTest(){
		Map<String,Collection<String>> output = CallGraphVisitorExecutor.processCallGraph(fileContent, filePath);
		printMapItens(output);
	}
	
	private void printMapItens(Map<String,Collection<String>> output){
		System.out.println("CallGraph result: ");
		Iterator<String> iteratorKeys = output.keySet().iterator();
		while (iteratorKeys.hasNext()) {
			String key = iteratorKeys.next();
			List<String> methodsCalled = (List<String>) output.get(key);
			for(String methodCalled : methodsCalled)
				System.out.println(key + " | " + methodCalled);
		}
	}
	
}
