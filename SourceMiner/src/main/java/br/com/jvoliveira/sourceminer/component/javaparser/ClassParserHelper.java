/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.jvoliveira.sourceminer.domain.ItemAsset;
import br.com.jvoliveira.sourceminer.domain.enums.AssetType;

/**
 * @author Joao Victor
 *
 */
@Component
public class ClassParserHelper {
	
	JavaClassParser javaParser;
	
	public ClassParserHelper(){
		
	}
	
	public List<ItemAsset> generateActualClassAssets(List<ItemAsset> actualAssets, String newFileContent){
		List<ItemAsset> assetsInFileContent = getAllMethodsInFileContent(newFileContent);
		List<ItemAsset> itemAssets = new ArrayList<ItemAsset>();
		
		itemAssets.addAll(getMethodAssets(actualAssets, assetsInFileContent));
		//Implementar diferença de fields e imports
		
		return itemAssets;
	}
	
	private Collection<? extends ItemAsset> getMethodAssets(
			List<ItemAsset> actualAssets, List<ItemAsset> assetsInFileContent) {
		
		//TODO
		
		List<String> actualMethodsSignature = actualAssets
												.stream()
												.filter(method -> method.getAssetType().equals(AssetType.METHOD))
												.map(ItemAsset::getSignature)
												.collect(Collectors.toList());
		
		List<ItemAsset> methodAsset = assetsInFileContent
			.stream()
			.filter(asset -> !actualMethodsSignature.contains(asset.getSignature()))
			.collect(Collectors.toList());
		
		return methodAsset;
	}

	public List<ItemAsset> getDiffMethodsBetweenFileContent(String oldContent, String actualContent){
		
		List<ItemAsset> oldAssets = getAllMethodsInFileContent(oldContent);
		List<ItemAsset> actualAssets = getAllMethodsInFileContent(actualContent);
		
		//TODO: Implementar comparação
		return null;
	}

	private List<ItemAsset> getAllMethodsInFileContent(String fileContent){
		javaParser = new JavaClassParser(fileContent);
		return javaParser.parserMethods();
	}
	
}
