package br.com.jvoliveira.sourceminer.repository.custom;

import java.util.List;

import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;

/**
 * @author Joao Victor
 *
 */
public interface RepositoryItemRepositoryCustom {

	public RepositoryItem findItemByFullPath(String importPath, Project project, String extension); 
	
	public String findLastRevisionInFile(String filePath, Long idProject);
	
	public List<RepositoryItem> findItemWithoutNode(Project project);
	
	public List<RepositoryItem> findItemsChangedInRevisions(Project project, List<String> revisions);
}
