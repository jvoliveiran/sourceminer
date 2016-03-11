/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.jvoliveira.sourceminer.domain.ItemAsset;

/**
 * @author Joao Victor
 *
 */
@Component
public class ClassParserHelper {
	
	JavaClassParser javaParser;
	
	public ClassParserHelper(){
		
	}
	
	public List<ItemAsset> getDiffMethodsBetweenFileContent(String oldContent, String actualContent){
		
		List<ItemAsset> oldAssets = getAllMethodsInFileContent(oldContent);
		List<ItemAsset> actualAssets = getAllMethodsInFileContent(oldContent);
		
		//TODO: Implementar comparação
		return null;
	}

	private List<ItemAsset> getAllMethodsInFileContent(String fileContent){
		javaParser = new JavaClassParser(fileContent);
		return javaParser.parserMethods();
	}
	
}
