package br.com.jvoliveira.sourceminer.component.repositoryconnection.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.treewalk.filter.PathFilterGroup;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import br.com.jvoliveira.arq.utils.StringUtils;
import br.com.jvoliveira.sourceminer.component.repositoryconnection.utils.PathModel.PathChangeModel;

/**
 * Collection of static methods for retrieving information from a repository.
 * http://www.javased.com/index.php?source_dir=gitblit/src/com/gitblit/utils/JGitUtils.java
 * 
 * @author James Moger
 * 
 */
public class JGitUtils {

	/**
	 * Determine if a repository has any commits. This is determined by checking
	 * the for loose and packed objects.
	 * 
	 * @param repository
	 * @return true if the repository has commits
	 */
	public static boolean hasCommits(Repository repository) {
		if (repository != null && repository.getDirectory().exists()) {
			return (new File(repository.getDirectory(), "objects").list().length > 2)
					|| (new File(repository.getDirectory(), "objects/pack").list().length > 0);
		}
		return false;
	}

	/**
	 * Returns the specified commit from the repository. If the repository does
	 * not exist or is empty, null is returned.
	 * 
	 * @param repository
	 * @param objectId
	 *            if unspecified, HEAD is assumed.
	 * @return RevCommit
	 */
	public static RevCommit getCommit(Repository repository, String objectId) {
		if (!hasCommits(repository)) {
			return null;
		}
		RevCommit commit = null;
		try {
			// resolve object id
			ObjectId branchObject;
			branchObject = repository.resolve(objectId);
			RevWalk walk = new RevWalk(repository);
			RevCommit rev = walk.parseCommit(branchObject);
			commit = rev;
			walk.dispose();
		} catch (Throwable t) {
			System.out.println(repository + " failed to get commit " + objectId);
		}
		return commit;
	}

	/**
	 * Returns the list of files in the specified folder at the specified
	 * commit. If the repository does not exist or is empty, an empty list is
	 * returned.
	 * 
	 * @param repository
	 * @param path
	 *            if unspecified, root folder is assumed.
	 * @param commit
	 *            if null, HEAD is assumed.
	 * @return list of files in specified path
	 */
	public static List<PathModel> getFilesInPath(Repository repository, String path, RevCommit commit) {
		List<PathModel> list = new ArrayList<PathModel>();
		if (!hasCommits(repository)) {
			return list;
		}
		if (commit == null) {
			commit = getCommit(repository, null);
		}
		final TreeWalk tw = new TreeWalk(repository);
		try {
			tw.addTree(commit.getTree());
			if (!StringUtils.isEmpty(path)) {
				PathFilter f = PathFilter.create(path);
				tw.setFilter(f);
				tw.setRecursive(false);
				boolean foundFolder = false;
				while (tw.next()) {
					if (!foundFolder && tw.isSubtree()) {
						tw.enterSubtree();
					}
					if (tw.getPathString().equals(path)) {
						foundFolder = true;
						continue;
					}
					if (foundFolder) {
						list.add(getPathModel(tw, path, commit));
					}
				}
			} else {
				tw.setRecursive(false);
				while (tw.next()) {
					list.add(getPathModel(tw, null, commit));
				}
			}
		} catch (IOException e) {
			System.out.println(repository + " failed to get files for commit " + commit.getName());
		} finally {
			tw.release();
		}
		Collections.sort(list);
		return list;
	}

	/**
	 * Returns the list of files changed in a specified commit. If the
	 * repository does not exist or is empty, an empty list is returned.
	 * 
	 * @param repository
	 * @param commit
	 *            if null, HEAD is assumed.
	 * @return list of files changed in a commit
	 */
	public static List<PathChangeModel> getFilesInCommit(Repository repository, RevCommit commit) {
		List<PathChangeModel> list = new ArrayList<PathChangeModel>();
		if (!hasCommits(repository)) {
			return list;
		}
		RevWalk rw = new RevWalk(repository);
		try {

			if (commit.getParentCount() == 0) {
				TreeWalk tw = new TreeWalk(repository);
				tw.reset();
				tw.setRecursive(true);
				tw.addTree(commit.getTree());
				while (tw.next()) {
					list.add(new PathChangeModel(tw.getPathString(), tw.getPathString(), 0, tw.getRawMode(0),
							tw.getObjectId(0).getName(), commit.getId().getName(), ChangeType.ADD,
							commit.getAuthorIdent().getName(), commit.getAuthorIdent().getWhen()));
				}
				tw.release();
			} else {
				RevCommit parent = rw.parseCommit(commit.getParent(0).getId());
				DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
				df.setRepository(repository);
				df.setDiffComparator(RawTextComparator.DEFAULT);
				df.setDetectRenames(true);
				List<DiffEntry> diffs = df.scan(parent.getTree(), commit.getTree());
				for (DiffEntry diff : diffs) {
					String objectId = null;
					if (FileMode.GITLINK.equals(diff.getNewMode())) {
						objectId = diff.getNewId().name();
					}

					if (diff.getChangeType().equals(ChangeType.DELETE)) {
						list.add(new PathChangeModel(diff.getOldPath(), diff.getOldPath(), 0,
								diff.getNewMode().getBits(), objectId, commit.getId().getName(), diff.getChangeType(),
								commit.getAuthorIdent().getName(), commit.getAuthorIdent().getWhen()));
					} else if (diff.getChangeType().equals(ChangeType.RENAME)) {
						list.add(new PathChangeModel(diff.getOldPath(), diff.getNewPath(), 0,
								diff.getNewMode().getBits(), objectId, commit.getId().getName(), diff.getChangeType(),
								commit.getAuthorIdent().getName(), commit.getAuthorIdent().getWhen()));
					} else {
						list.add(new PathChangeModel(diff.getNewPath(), diff.getNewPath(), 0,
								diff.getNewMode().getBits(), objectId, commit.getId().getName(), diff.getChangeType(),
								commit.getAuthorIdent().getName(), commit.getAuthorIdent().getWhen()));
					}
				}
			}
		} catch (Throwable t) {
			System.out.println(repository + " failed to determine files in commit!");
		} finally {
			rw.dispose();
		}
		return list;
	}

	/**
	 * Returns a path model of the current file in the treewalk.
	 * 
	 * @param tw
	 * @param basePath
	 * @param commit
	 * @return a path model of the current file in the treewalk
	 */
	private static PathModel getPathModel(TreeWalk tw, String basePath, RevCommit commit) {
		String name;
		long size = 0;
		if (StringUtils.isEmpty(basePath)) {
			name = tw.getPathString();
		} else {
			name = tw.getPathString().substring(basePath.length() + 1);
		}
		ObjectId objectId = tw.getObjectId(0);
		try {
			if (!tw.isSubtree() && (tw.getFileMode(0) != FileMode.GITLINK)) {
				size = tw.getObjectReader().getObjectSize(objectId, Constants.OBJ_BLOB);
			}
		} catch (Throwable t) {
			System.out.println("failed to retrieve blob size for " + tw.getPathString());
		}
		return new PathModel(name, tw.getPathString(), size, tw.getFileMode(0).getBits(), objectId.getName(),
				commit.getName());
	}

	/**
	 * Returns the UTF-8 string content of a file in the specified tree.
	 * 
	 * @param repository
	 * @param tree
	 *            if null, the RevTree from HEAD is assumed.
	 * @param blobPath
	 * @param charsets
	 *            optional
	 * @return UTF-8 string content
	 */
	public static String getStringContent(Repository repository, RevTree tree, String blobPath, String... charsets) {
		byte[] content = getByteContent(repository, tree, blobPath);
		if (content == null) {
			return null;
		}
		return StringUtils.decodeString(content, charsets);
	}

	/**
	 * Retrieves the raw byte content of a file in the specified tree.
	 * 
	 * @param repository
	 * @param tree
	 *            if null, the RevTree from HEAD is assumed.
	 * @param path
	 * @return content as a byte []
	 */
	public static byte[] getByteContent(Repository repository, RevTree tree, final String path) {
		RevWalk rw = new RevWalk(repository);
		TreeWalk tw = new TreeWalk(repository);
		tw.setFilter(PathFilterGroup.createFromStrings(Collections.singleton(path)));
		byte[] content = null;
		try {
			tw.reset(tree);
			while (tw.next()) {
				if (tw.isSubtree() && !path.equals(tw.getPathString())) {
					tw.enterSubtree();
					continue;
				}
				ObjectId entid = tw.getObjectId(0);
				FileMode entmode = tw.getFileMode(0);
				if (entmode != FileMode.GITLINK) {
					RevObject ro = rw.lookupAny(entid, entmode.getObjectType());
					rw.parseBody(ro);
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					ObjectLoader ldr = repository.open(ro.getId(), Constants.OBJ_BLOB);
					byte[] tmp = new byte[4096];
					InputStream in = ldr.openStream();
					int n;
					while ((n = in.read(tmp)) > 0) {
						os.write(tmp, 0, n);
					}
					in.close();
					content = os.toByteArray();
				}
			}
		} catch (Throwable t) {
			System.out.println(repository + " can't find " + path + " in tree " + tree.name());
		} finally {
			rw.dispose();
			tw.release();
		}
		return content;
	}

}
