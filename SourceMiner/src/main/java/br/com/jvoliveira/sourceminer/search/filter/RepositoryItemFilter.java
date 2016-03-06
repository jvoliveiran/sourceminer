/**
 * 
 */
package br.com.jvoliveira.sourceminer.search.filter;

/**
 * @author Joao Victor
 *
 */
public class RepositoryItemFilter {

	private Boolean checkName;
	private String name;
	
	public Boolean getCheckName() {
		return checkName;
	}

	public void setCheckName(Boolean checkName) {
		this.checkName = checkName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isValidName(){
		return checkName != null 
				&& checkName
				&& name != null
				&& !name.trim().equals("");
	}
}
