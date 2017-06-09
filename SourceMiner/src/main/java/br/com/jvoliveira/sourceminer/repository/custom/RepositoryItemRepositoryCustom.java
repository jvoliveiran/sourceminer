package br.com.jvoliveira.sourceminer.repository.custom;

import java.util.Collection;
import java.util.List;

import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevisionItem;

/**
 * @author Joao Victor
 *
 */
public interface RepositoryItemRepositoryCustom {

	public RepositoryItem findItemByFullPath(String importPath, Project project, String extension); 
	
	public String findLastRevisionInFile(String filePath, Long idProject);
	
	public List<RepositoryItem> findItemWithoutNode(Project project);
	
	public List<RepositoryItem> findItemsChangedInRevisions(Project project, List<String> revisions);
	
	public List<RepositoryRevisionItem> findItemsModifiedPreviouslyIn(Collection<Long> ids, RepositoryRevision revision);
	
	public RepositoryItem findByPathAndNameAndProjectOptimized(String path, String name, Project project);
}
