/**
 * 
 */
package br.com.jvoliveira.sourceminer.search.filter;

import br.com.jvoliveira.arq.search.ArqFilter;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.enums.AssetType;
import br.com.jvoliveira.sourceminer.domain.enums.ChangeFileType;

/**
 * @author Joao Victor
 *
 */
public class ItemChangeLogFilter implements ArqFilter{
	
	private Boolean checkAssetType;
	private AssetType assetType;
	
	private Boolean checkChangeFileType;
	private ChangeFileType changeFileType;

	private RepositoryItem item;
	
	@Override
	public boolean hasFilterToSearch() {
		if(!isValidAssetType())
			return false;
		
		if(isValidChangeFileType())
			return false;
		
		if(item == null)
			return false;
		
		return true;
	}
	
	public boolean isValidChangeFileType(){
		return checkChangeFileType != null
				&& checkChangeFileType
				&& changeFileType != null;
	}
	
	public boolean isValidAssetType(){
		return checkAssetType != null
				&& checkAssetType
				&& assetType != null;
	}

	public Boolean getCheckAssetType() {
		return checkAssetType;
	}

	public void setCheckAssetType(Boolean checkAssetType) {
		this.checkAssetType = checkAssetType;
	}

	public AssetType getAssetType() {
		return assetType;
	}

	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
	}

	public Boolean getCheckChangeFileType() {
		return checkChangeFileType;
	}

	public void setCheckChangeFileType(Boolean checkChangeFileType) {
		this.checkChangeFileType = checkChangeFileType;
	}

	public ChangeFileType getChangeFileType() {
		return changeFileType;
	}

	public void setChangeFileType(ChangeFileType changeFileType) {
		this.changeFileType = changeFileType;
	}

	public RepositoryItem getItem() {
		return item;
	}

	public void setItem(RepositoryItem item) {
		this.item = item;
	}

}
