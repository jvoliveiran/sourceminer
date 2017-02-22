/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.javaparser.ast.CompilationUnit;

import br.com.jvoliveira.sourceminer.component.javaparser.visitor.JavaClassVisitor;

/**
 * @author João Victor
 *
 */
public class JavaClassVisitorTest {

	private String fileContent;
	private String filePath;
	private JavaClassVisitor visitor;
	private CompilationUnit compUnit;
	
	@Before
	public void setup(){
		try {
			filePath = "src/main/resources/test/nova_classe_teste.txt";
			File fileTXT = new File(filePath);
			fileContent = new String(Files.readAllBytes(Paths.get(fileTXT.getAbsolutePath())));
			compUnit = CompilationUnitHelper.getCompilationUnit(fileContent, filePath);
		} catch (IOException e) {
			fileContent = "";
		}
		visitor = new JavaClassVisitor();
	}
	
	@After
	public void tearDown(){
		
	}
	
	@Test
	public void generalTest(){
		visitor.visit(compUnit,null);
	}
	
}
