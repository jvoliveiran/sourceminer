package br.com.jvoliveira.sourceminer.domain;

import br.com.jvoliveira.arq.domain.ObjectDB;
import br.com.jvoliveira.sourceminer.domain.enums.AssetType;

/**
 * 
 * @author Joao Victor
 *
 */
public interface GenericAsset extends ObjectDB{

	public String getName();
	
	public void setName(String name);
	
	public String getSignature();
	
	public void setSignature(String signature);
	
	public AssetType getAssetType();
	
	public void setAssetType(AssetType assetType);
	
}
