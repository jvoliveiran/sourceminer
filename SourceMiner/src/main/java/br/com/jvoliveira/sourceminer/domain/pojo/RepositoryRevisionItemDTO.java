/**
 * 
 */
package br.com.jvoliveira.sourceminer.domain.pojo;

import br.com.jvoliveira.sourceminer.domain.RepositoryRevisionItem;

/**
 * @author João Victor
 *
 */
public class RepositoryRevisionItemDTO {

	private RepositoryRevisionItem revItem;
	
	private Long assetsChanged;
	
	public RepositoryRevisionItemDTO(RepositoryRevisionItem revItem, Long assetsChanged){
		this.revItem = revItem;
		this.assetsChanged = assetsChanged;
	}

	public RepositoryRevisionItem getRevItem() {
		return revItem;
	}

	public void setRevItem(RepositoryRevisionItem revItem) {
		this.revItem = revItem;
	}

	public Long getAssetsChanged() {
		return assetsChanged;
	}

	public void setAssetsChanged(Long assetsChanged) {
		this.assetsChanged = assetsChanged;
	}
	
}
