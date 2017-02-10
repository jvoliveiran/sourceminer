package br.com.jvoliveira.sourceminer.component.repositoryconnection.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.lib.FileMode;

/**
 * PathModel is a serializable model class that represents a file or a folder,
 * including all its metadata and associated commit id.
 * 
 * @author James Moger
 * 
 */
public class PathModel implements Serializable, Comparable<PathModel> {

	private static final long serialVersionUID = 1L;

	public final String name;
	public final String path;
	public final long size;
	public final int mode;
	public final String commitId;
	public boolean isParentPath;
	public final String author;
	public final Date date;
	public List<PathChangeModel> changedFiles;

	public PathModel(String name, String path, long size, int mode, String objectId, String commitId) {
		this.name = name;
		this.path = path;
		this.size = size;
		this.mode = mode;
		this.commitId = commitId;
		this.author = null;
		this.date = null;
	}
	
	public PathModel(String name, String path, long size, int mode, String objectId, String author, Date date) {
		this.name = name;
		this.path = path;
		this.size = size;
		this.mode = mode;
		this.author = author;
		this.date = date;
		this.commitId = objectId;
	}

	public boolean isSubmodule() {
		return FileMode.GITLINK.equals(mode);
	}

	public boolean isTree() {
		return FileMode.TREE.equals(mode);
	}
	
	public void setChangedFiles(List<PathChangeModel> changedFiles){
		this.changedFiles = changedFiles;
	}
	
	public List<PathChangeModel> getChangedFiles(){
		return this.changedFiles;
	}

	@Override
	public int hashCode() {
		return commitId.hashCode() + path.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PathModel) {
			PathModel other = (PathModel) o;
			return this.path.equals(other.path);
		}
		return super.equals(o);
	}

	@Override
	public int compareTo(PathModel o) {
		boolean isTree = isTree();
		boolean otherTree = o.isTree();
		if (isTree && otherTree) {
			return path.compareTo(o.path);
		} else if (!isTree && !otherTree) {
			if (isSubmodule() && o.isSubmodule()) {
				return path.compareTo(o.path);
			} else if (isSubmodule()) {
				return -1;
			} else if (o.isSubmodule()) {
				return 1;
			}
			return path.compareTo(o.path);
		} else if (isTree && !otherTree) {
			return -1;
		}
		return 1;
	}

	/**
	 * PathChangeModel is a serializable class that represents a file changed in
	 * a commit.
	 * 
	 * @author James Moger
	 * 
	 */
	public static class PathChangeModel extends PathModel {

		private static final long serialVersionUID = 1L;

		public final ChangeType changeType;

		public PathChangeModel(String name, String path, long size, int mode, String objectId, String commitId,
				ChangeType type, String author, Date date) {
			super(name, path, size, mode, objectId, author, date);
			this.changeType = type;
		}

		@Override
		public int hashCode() {
			return super.hashCode();
		}

		@Override
		public boolean equals(Object o) {
			return super.equals(o);
		}
	}
}
