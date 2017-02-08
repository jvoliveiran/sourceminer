/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.repositoryconnection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;

import br.com.jvoliveira.arq.utils.StringUtils;
import br.com.jvoliveira.sourceminer.component.repositoryconnection.utils.PathModel;
import br.com.jvoliveira.sourceminer.component.repositoryconnection.utils.PathModel.PathChangeModel;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevisionItem;
import br.com.jvoliveira.sourceminer.domain.enums.CommitType;

/**
 * @author Joao Victor
 *
 */
public class RepositoryParseGIT implements RepositoryParse{

	@Override
	public <T> RepositoryItem parseToRepositoryItem(T entry) {
		TreeWalk treeWalk = (TreeWalk) entry;
		RepositoryItem repositoryItem = new RepositoryItem();
		
		repositoryItem.setName(treeWalk.getNameString());
		repositoryItem.setPath(treeWalk.getPathString());
		repositoryItem.setExtension(StringUtils.getExtensionInFileName(treeWalk.getNameString()));
		
		return repositoryItem;
	}

	@Override
	public <T> RepositoryRevision parseToRepositoryRevision(T entry) {
		RevCommit commit = (RevCommit) entry;
		RepositoryRevision repositoryRevision = new RepositoryRevision();
		
		repositoryRevision.setRevision(commit.getId().name());
		repositoryRevision.setAuthor(commit.getAuthorIdent().getName());
		repositoryRevision.setDateRevision(commit.getAuthorIdent().getWhen());
		
		return repositoryRevision;
	}
	
	public List<RepositoryRevisionItem> parsePathModelToRepositoryRevisionItem(PathModel pathModel) {
		List<PathChangeModel> changedFiles = pathModel.getChangedFiles();
		List<RepositoryRevisionItem> result = new ArrayList<>();
		for(PathChangeModel changedFile : changedFiles){
			RepositoryRevisionItem revisionItem = new RepositoryRevisionItem();
			
			revisionItem.setRepositoryItem(parsePathChangeModelToRepositoryItem(changedFile));
			revisionItem.setRepositoryRevision(parsePathModelToRepositoryRevision(pathModel));
			revisionItem.setCommitType(getCommitTypeByPathChangeModel(changedFile));
			result.add(revisionItem);
		}
		
		return result;
	}
	
	public CommitType getCommitTypeByPathChangeModel(PathChangeModel changedFile) {
		if(changedFile.changeType.equals(ChangeType.ADD))
			return CommitType.ADD;
		else if(changedFile.changeType.equals(ChangeType.DELETE))
			return CommitType.DEL;
		else
			return CommitType.MOD;
	}

	public RepositoryRevision parsePathModelToRepositoryRevision(PathModel pathModel) {
		RepositoryRevision revision = new RepositoryRevision();
		
		revision.setRevision(pathModel.commitId);
		revision.setAuthor(pathModel.author);
		revision.setDateRevision(pathModel.date);
		
		return revision;
	}

	public RepositoryItem parsePathChangeModelToRepositoryItem(PathChangeModel pathModel){
		RepositoryItem repositoryItem = new RepositoryItem();
		
		repositoryItem.setName(pathModel.name);
		repositoryItem.setPath(pathModel.path);
		repositoryItem.setExtension(StringUtils.getExtensionInFileName(pathModel.path));
		
		return repositoryItem;
	}

	@Override
	public <T> RepositoryRevisionItem parseToRepositoryRevisionItem(T entry) {
		// TODO Auto-generated method stub
		return null;
	}

}
