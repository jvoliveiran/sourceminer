/**
 * 
 */
package br.com.jvoliveira.sourceminer.domain.pojo;

import java.util.ArrayList;
import java.util.List;

import br.com.jvoliveira.sourceminer.domain.ItemAsset;

/**
 * @author Joao Victor
 *
 */
public class ItemAssetGroupType {

	private List<ItemAsset> imports;
	private List<ItemAsset> methods;
	private List<ItemAsset> attributes;
	
	public ItemAssetGroupType(List<ItemAsset> allAssets){
		imports = new ArrayList<ItemAsset>();
		methods = new ArrayList<ItemAsset>();
		attributes = new ArrayList<ItemAsset>();
		
		allAssets.stream()
				.forEach(asset -> setAssetInGroup(asset));
	}

	private void setAssetInGroup(ItemAsset asset) {
		if(asset.isMethodAsset())
			methods.add(asset);
		else if(asset.isAttributeAsset())
			attributes.add(asset);
		else if(asset.isImportAsset())
			imports.add(asset);
	}

	public List<ItemAsset> getImports() {
		return imports;
	}

	public List<ItemAsset> getMethods() {
		return methods;
	}

	public List<ItemAsset> getAttributes() {
		return attributes;
	}
}
