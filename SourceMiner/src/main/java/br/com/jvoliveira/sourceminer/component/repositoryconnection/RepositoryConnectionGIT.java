/**
 * 
 */
package br.com.jvoliveira.sourceminer.component.repositoryconnection;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import br.com.jvoliveira.sourceminer.component.repositoryconnection.utils.JGitUtils;
import br.com.jvoliveira.sourceminer.component.repositoryconnection.utils.PathModel;
import br.com.jvoliveira.sourceminer.domain.Project;
import br.com.jvoliveira.sourceminer.domain.ProjectConfiguration;
import br.com.jvoliveira.sourceminer.domain.RepositoryConnector;
import br.com.jvoliveira.sourceminer.domain.RepositoryItem;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevision;
import br.com.jvoliveira.sourceminer.domain.RepositoryRevisionItem;
import br.com.jvoliveira.sourceminer.exceptions.RepositoryConnectionException;

/**
 * @author Joao Victor
 *
 */
public class RepositoryConnectionGIT implements RepositoryConnection {

	private RepositoryConnector connector;
	private Git gitSource;

	private RepositoryParseGIT parse = new RepositoryParseGIT();
	
	public RepositoryConnectionGIT(){
		
	}
	
	public RepositoryConnectionGIT(RepositoryConnector connector){
		this.connector = connector;
	}

	@Override
	public RepositoryConnector getConnector() {
		return this.connector;
	}

	@Override
	public void setConnector(RepositoryConnector connector) {
		this.connector = connector;
	}

	@Override
	public RepositoryParse getParse() {
		return parse;
	}

	@Override
	public void openConnection() throws RepositoryConnectionException {
		if (connector.getRepositoryLocation().getLocationType().isLocal())
			gitSource = openLocalConnection(connector.getRepositoryLocation().getUrl());

		else if (connector.getRepositoryLocation().getLocationType().isRemote())
			gitSource = openRemoteConnection();

	}

	private Git openRemoteConnection() {
		Git git = null;
		String username = connector.getUsername();
		String password = connector.getPassword();
		String sourcePath = connector.getRepositoryLocation().getUrl();
		String localPath = "/Users/MacBook/sourcermine_git_repo/" + connector.getName();

		if (isExistLocalRepository())
			return openLocalConnection(localPath);

		CredentialsProvider credential = new UsernamePasswordCredentialsProvider(username, password);
		try {
			git = Git.cloneRepository().setDirectory(new File(localPath)).setURI(sourcePath)
					.setCredentialsProvider(credential).call();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		return git;
	}

	private boolean isExistLocalRepository() {
		String localPath = "/Users/MacBook/sourcermine_git_repo/" + connector.getName();

		File varTmpDir = new File(localPath);
		return varTmpDir.exists();
	}

	private Git openLocalConnection(String localPath) {
		Git git = null;
		try {
			git = Git.open(new File(localPath));
			git.pull().call();
		} catch (IOException | GitAPIException e) {
			e.printStackTrace();
		}
		return git;
	}

	@Override
	public void closeConnection() {
		if (gitSource != null)
			gitSource.close();
	}

	@Override
	public boolean testConnection() throws RepositoryConnectionException {
		try {
			openConnection();
			showGitStatus();
		} catch (NoWorkTreeException e) {
			e.printStackTrace();
			return false;
		} catch (GitAPIException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (isConnectionOpened())
				closeConnection();
		}
		return true;
	}

	private void showGitStatus() throws NoWorkTreeException, GitAPIException {
		Status status = gitSource.status().call();
		System.out.println("Added: " + status.getAdded());
		System.out.println("Changed: " + status.getChanged());
		System.out.println("Conflicting: " + status.getConflicting());
		System.out.println("ConflictingStageState: " + status.getConflictingStageState());
		System.out.println("IgnoredNotInIndex: " + status.getIgnoredNotInIndex());
		System.out.println("Missing: " + status.getMissing());
		System.out.println("Modified: " + status.getModified());
		System.out.println("Removed: " + status.getRemoved());
		System.out.println("Untracked: " + status.getUntracked());
		System.out.println("UntrackedFolders: " + status.getUntrackedFolders());
	}

	@Override
	public Boolean isConnectionOpened() {
		return this.gitSource != null;
	}

	@Override
	public Boolean isGit() {
		return true;
	}

	@Override
	public Boolean isSVN() {
		return false;
	}

	@Override
	public Boolean isCVS() {
		return false;
	}

	@Override
	public List<RepositoryItem> getAllProjectItens(Project project) {
		List<RepositoryItem> resultItem = new ArrayList<>();
		Repository repository = gitSource.getRepository();
		Ref head;
		try {
			head = repository.getRef(project.getPath());
			// a RevWalk allows to walk over commits based on some filtering
			// that is defined
			RevWalk walk = new RevWalk(repository);

			RevCommit commit = walk.parseCommit(head.getObjectId());
			RevTree tree = commit.getTree();

			// now use a TreeWalk to iterate over all files in the Tree
			// recursively
			// you can set Filters to narrow down the results if needed
			TreeWalk treeWalk = new TreeWalk(repository);
			treeWalk.addTree(tree);
			treeWalk.setRecursive(false);
			while (treeWalk.next()) {
				if (treeWalk.isSubtree())
					treeWalk.enterSubtree();
				else if (isJavaFile(treeWalk))
					resultItem.add(parse.parseToRepositoryItem(treeWalk));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultItem;
	}

	private boolean isJavaFile(TreeWalk treeWalk) {
		return treeWalk.getNameString().contains(".java");
	}

	@Override
	public List<RepositoryRevisionItem> getRevisionItensInProjectRange(Project project, ProjectConfiguration config) {
		List<RepositoryRevisionItem> revisionItemLogs = new ArrayList<>();
		
		validateSyncRevisionInterval(config,project);
		
		ObjectId objFrom = ObjectId.fromString(config.getSyncStartRevision());
		ObjectId objUntil = ObjectId.fromString(config.getSyncEndRevision());
		try {
			Iterable<RevCommit> commits = gitSource.log().addRange(objFrom, objUntil).call();
			Iterator<RevCommit> i = commits.iterator();
			RevCommit commit = null;
			RevWalk walk = new RevWalk(gitSource.getRepository());
			while (i.hasNext()) {
				commit = walk.parseCommit(i.next());
				PathModel commitObj = JGitUtils.getFilesInPath(gitSource.getRepository(), null, commit).get(0);
				commitObj.setChangedFiles(JGitUtils.getFilesInCommit(gitSource.getRepository(), commit));

				revisionItemLogs.addAll(parse.parsePathModelToRepositoryRevisionItem(commitObj));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoHeadException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}

		return revisionItemLogs;
	}

	private void validateSyncRevisionInterval(ProjectConfiguration config, Project project) {
		if(config.getSyncEndRevision() == null)
			config.setSyncEndRevision(getLastRevisionNumber(project));
		if(config.getSyncStartRevision() == null || config.getSyncStartRevision().equals(""))
			config.setSyncStartRevision(getFirstRevisionNumber(project));
			
	}

	@Override
	public List<RepositoryRevision> getAllProjectRevision(Project project) {
		List<RepositoryRevision> revisions = new ArrayList<>();
		try {
			Iterable<RevCommit> commits = gitSource.log().call();
			for (RevCommit commit : commits)
				revisions.add(parse.parseToRepositoryRevision(commit));
		} catch (GitAPIException e) {
			e.printStackTrace();
		}

		return revisions;
	}

	@Override
	public List<RepositoryRevision> getRevisionsInRange(Project project, String start, String end) {
		ObjectId objFrom = ObjectId.fromString(start);
		ObjectId objUntil = ObjectId.fromString(end);
		List<RepositoryRevision> revisions = new ArrayList<>();
		try {
			Iterable<RevCommit> commits = gitSource.log().addRange(objFrom, objUntil).call();
			for (RevCommit commit : commits)
				revisions.add(parse.parseToRepositoryRevision(commit));

		} catch (MissingObjectException | IncorrectObjectTypeException | GitAPIException e) {
			e.printStackTrace();
		}

		return revisions;
	}

	@Override
	public String getLastRevisionNumber(Project project) {

		try {
			Repository repository = gitSource.getRepository();
			Ref head = repository.getRef("HEAD");
			return head.getObjectId().getName();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public String getFileContent(String path, String revision) {
		ObjectId commitRevision = null;
		try {			
			if(revision.equals("-1"))
				commitRevision = gitSource.getRepository().resolve(Constants.HEAD);
			else
				commitRevision = ObjectId.fromString(revision);
			
			RevWalk revWalk = new RevWalk(gitSource.getRepository());
			RevCommit commit = revWalk.parseCommit(commitRevision);
			
			RevTree tree = commit.getTree();
			TreeWalk treeWalk = new TreeWalk(gitSource.getRepository());
			treeWalk.addTree(tree);
			treeWalk.setRecursive(true);
			treeWalk.setFilter(PathFilter.create(path));
			if (!treeWalk.next()) {
			  return null;
			}
			ObjectId objectId = treeWalk.getObjectId(0);
			ObjectLoader loader = gitSource.getRepository().open(objectId);
			
			InputStream in = loader.openStream();
			String result = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
			if(result == null)
				return "";
			
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Em repositórios GIT a sincronização em um intervalo deve sempre considerar a partir
	 * da revisão anterior ao intervalo de revisões desejados
	 */
	@Override
	public String getNextRevisionToSync(Project project, String revisionLastSync) {
		try {
			gitSource.pull().call();
		} catch (GitAPIException e) {
			System.err.println("Erro ao atualizar repositório local a partir do repositório remoto");
			e.printStackTrace();
		}
		if(revisionLastSync == null || revisionLastSync.trim().equals("") || revisionLastSync.trim().equals("0"))
			return getNextOutOfSyncRevision(project,revisionLastSync);
		else
			return revisionLastSync;
	}
	
	public String getNextOutOfSyncRevision(Project project, String revisionLastSync) {
		try {
			RevWalk walk = new RevWalk(gitSource.getRepository());
			walk.markStart(walk.parseCommit(ObjectId.fromString(getLastRevisionNumber(project))));
			
			if(revisionLastSync.equals(getLastRevisionNumber(project)))
				return "0";
			else if(revisionLastSync.equals("")){
				walk.sort(RevSort.REVERSE);
				return walk.next().getName();
			}	
			
			boolean isItLastSyncRev = false;
			String nextRevisionToSync = null;
			
			while(!isItLastSyncRev){
				RevCommit commit = walk.next();
				if(!commit.getName().equals(revisionLastSync))
					nextRevisionToSync = commit.getName();
				else
					return nextRevisionToSync;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getFirstRevisionNumber(Project project){
		try {
			RevWalk walk = new RevWalk(gitSource.getRepository());
			walk.markStart(walk.parseCommit(ObjectId.fromString(getLastRevisionNumber(project))));
			walk.sort(RevSort.REVERSE);
			return walk.next().getName();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getParentRevisionNumber(Project project, String revision){
		RevWalk revWalk = new RevWalk(gitSource.getRepository());
		try {
			revWalk.markStart(revWalk.parseCommit(ObjectId.fromString(revision)));
			revWalk.next();
			return revWalk.next().getName();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
