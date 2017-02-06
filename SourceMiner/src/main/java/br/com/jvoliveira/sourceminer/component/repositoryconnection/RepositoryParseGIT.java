/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.repositoryconnection;

import org.eclipse.jgit.treewalk.TreeWalk;

import br.com.jvoliveira.arq.utils.StringUtils;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevisionItem;

/**
 * @author Joao Victor
 *
 */
public class RepositoryParseGIT implements RepositoryParse{

	@Override
	public <T> RepositoryItem parseToRepositoryItem(T entry) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public RepositoryItem parseTreeWalkToRepositoryItem(TreeWalk treeWalk){
		RepositoryItem repositoryItem = new RepositoryItem();
		
		repositoryItem.setName(treeWalk.getNameString());
		repositoryItem.setPath(treeWalk.getPathString());
		repositoryItem.setExtension(StringUtils.getExtensionInFileName(treeWalk.getNameString()));
		
		return repositoryItem;
	}

	@Override
	public <T> RepositoryRevision parseToRepositoryRevision(T entry) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> RepositoryRevisionItem parseToRepositoryRevisionItem(T entry) {
		// TODO Auto-generated method stub
		return null;
	}

}
