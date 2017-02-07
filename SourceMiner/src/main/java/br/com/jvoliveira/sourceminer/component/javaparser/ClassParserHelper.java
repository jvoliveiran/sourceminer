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
	
	public List<ItemAsset> generateActualClassAssets(List<ItemAsset> actualAssets, String newFileContent, String newFilePath){
		List<ItemAsset> assetsInFileContent = getAllAssetsInFileContent(newFileContent, newFilePath);
		List<ItemAsset> itemAssets = new ArrayList<ItemAsset>();
		
		itemAssets.addAll(getDiffAssets(actualAssets, assetsInFileContent));
		
		return itemAssets;
	}
	
	private Collection<? extends ItemAsset> getDiffAssets(
			List<ItemAsset> actualAssets, List<ItemAsset> assetsInFileContent) {
		
		List<ItemAsset> assets = new ArrayList<ItemAsset>();
		
		for(ItemAsset newAsset : assetsInFileContent){
			
			for(ItemAsset oldAsset : actualAssets){
			
				if(oldAsset.isMethodAsset() 
						&& !oldAsset.hasSameBodyMethod(newAsset) && oldAsset.equals(newAsset)){
					
					oldAsset.setItemChageLog(new ItemChangeLog(ChangeFileType.UPDATED));
					oldAsset.setValue(newAsset.getValue());
					
					assets.add(oldAsset);
					break;
				}else if(oldAsset.isMethodAsset() 
						&& oldAsset.hasSameBodyMethod(newAsset) && oldAsset.equals(newAsset)){
					newAsset.setNewAsset(false);
					oldAsset.setNewAsset(false);
					break;
				}else if(!oldAsset.isMethodAsset() && oldAsset.equals(newAsset)){
					newAsset.setNewAsset(false);
					oldAsset.setNewAsset(false);
					break;
				}
				
			}
			
			if(!assets.contains(newAsset) && newAsset.isNewAsset()){
				newAsset.setItemChageLog(new ItemChangeLog(ChangeFileType.ADDED));
				assets.add(newAsset);
			}
			
		}
		
		List<ItemAsset> deletedAssets = actualAssets.stream()
			.filter(old -> !assets.contains(old) && old.isMethodAsset() && old.isNewAsset())
			.collect(Collectors.toList());
		
		deletedAssets.stream().forEach(old -> putDeletedAssetInList(old,assets));
		
		return assets;
	}
	
	private void putDeletedAssetInList(ItemAsset deletedAsset, List<ItemAsset> assets){
		deletedAsset.setItemChageLog(new ItemChangeLog(ChangeFileType.DELETED));
		assets.add(deletedAsset);
	}

	private List<ItemAsset> getAllAssetsInFileContent(String fileContent, String newFilePath){
		javaParser = new JavaClassParser(fileContent, newFilePath);
		return javaParser.getAll();
	}
	
	public List<ChangeLogGroupModel> getChangeLogGroupByRevision(List<ItemChangeLog> itemChangeLogs){
		List<ChangeLogGroupModel> modelList = new ArrayList<ChangeLogGroupModel>();
		
		itemChangeLogs = itemChangeLogs.stream()
				.sorted((item1, item2) -> (-1)*(item1.getId().compareTo(item2.getId())))
				.collect(Collectors.toList());
		
		String revisionGroup = "0";
		for(ItemChangeLog changeLog : itemChangeLogs){	
			if(revisionGroup != changeLog.getRevision()){
				modelList.add(new ChangeLogGroupModel(changeLog.getRevision()));
				revisionGroup = changeLog.getRevision();
			}
			
			modelList.get(modelList.size() - 1).addLog(changeLog);
		}
		
		return modelList;
	}
}
