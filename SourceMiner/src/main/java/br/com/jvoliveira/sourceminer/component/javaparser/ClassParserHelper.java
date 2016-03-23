/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.javaparser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import br.com.jvoliveira.sourceminer.domain.ItemAsset;
import br.com.jvoliveira.sourceminer.domain.ItemChangeLog;
import br.com.jvoliveira.sourceminer.domain.enums.ChangeFileType;

/**
 * @author Joao Victor
 *
 */
public class ClassParserHelper {
	
	JavaClassParser javaParser;
	
	public ClassParserHelper(){
		
	}
	
	public List<ItemAsset> generateActualClassAssets(List<ItemAsset> actualAssets, String newFileContent){
		List<ItemAsset> assetsInFileContent = getAllMethodsInFileContent(newFileContent);
		List<ItemAsset> itemAssets = new ArrayList<ItemAsset>();
		
		itemAssets.addAll(getMethodAssets(actualAssets, assetsInFileContent));
		//TODO: Implementar diferença de fields e imports
		
		return itemAssets;
	}
	
	private Collection<? extends ItemAsset> getMethodAssets(
			List<ItemAsset> actualAssets, List<ItemAsset> assetsInFileContent) {
		
		List<ItemAsset> assets = new ArrayList<ItemAsset>();
		
		for(ItemAsset newAsset : assetsInFileContent){
			
			for(ItemAsset oldAsset : actualAssets){
			
				if(oldAsset.isMethodAsset() 
						&& !oldAsset.hasSameBodyMethod(newAsset) && oldAsset.equals(newAsset)){
					
					oldAsset.setItemChageLog(new ItemChangeLog(ChangeFileType.UPDATED));
					
					assets.add(oldAsset);
					break;
				}else if(oldAsset.isMethodAsset() 
						&& oldAsset.hasSameBodyMethod(newAsset) && oldAsset.equals(newAsset)){
					newAsset.setNewAsset(false);
					break;
				}
				
			}
			
			if(!assets.contains(newAsset) && newAsset.isNewAsset()){
				newAsset.setItemChageLog(new ItemChangeLog(ChangeFileType.ADDED));
				assets.add(newAsset);
			}
			
		}
		
		List<ItemAsset> deletedAssets = actualAssets.stream()
			.filter(old -> !assets.contains(old) && old.isMethodAsset())
			.collect(Collectors.toList());
		
		deletedAssets.stream().forEach(old -> putDeletedAssetInList(old,assets));
		
		return assets;
	}
	
	private void putDeletedAssetInList(ItemAsset deletedAsset, List<ItemAsset> assets){
		deletedAsset.setItemChageLog(new ItemChangeLog(ChangeFileType.DELETED));
		assets.add(deletedAsset);
	}

	private List<ItemAsset> getAllMethodsInFileContent(String fileContent){
		javaParser = new JavaClassParser(fileContent);
		return javaParser.parserMethods();
	}
	
}
