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

public class ClassParserHelperTest {

	private ClassParserHelper parserHelper;
	private JavaClassParser classParser;
	private String oldFileContent;
	private String newFileContent;
	
	@Before
	public void setUp(){
		try {
			File fileTXT = new File("src/main/resources/test/classe_teste.txt");
			oldFileContent = new String(Files.readAllBytes(Paths.get(fileTXT.getAbsolutePath())));
			
			fileTXT = new File("src/main/resources/test/nova_classe_teste.txt");
			newFileContent = new String(Files.readAllBytes(Paths.get(fileTXT.getAbsolutePath())));
		} catch (IOException e) {
			oldFileContent = "";
			newFileContent = "";
		}
		
		classParser = new JavaClassParser(oldFileContent);
		parserHelper = new ClassParserHelper();
	}
	
	@Test
	public void testGenerateActualClassAssets(){
		List<ItemAsset> metodosAntigos = classParser.getAll();
		List<ItemAsset> newMethods = parserHelper.generateActualClassAssets(metodosAntigos, newFileContent);
		
		Assert.assertTrue(newMethods.size() == 1);
	}
}
