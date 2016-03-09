/**
 * 
 */
package br.com.jvoliveira.sourceminer.domain;

import br.com.jvoliveira.arq.domain.ObjectDB;

/**
 * @author Joao Victor
 *
 */
public interface AssetChangeLog extends ObjectDB{

	public String getSignature();
	
	public void setSignature(String signature);
	
}
