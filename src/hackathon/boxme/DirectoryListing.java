package hackathon.boxme;

import java.util.LinkedList;
import java.util.List;

public class DirectoryListing {
	private List<String> files;
	private List<String> directories;

	public List<String> getFiles() {
		return files;
	}
	public void setFiles(List<String> files) {
		this.files = files;
	}
	public List<String> getDirectories() {
		return directories;
	}
	public void setDirectories(List<String> directories) {
		this.directories = directories;
	}
	
	public void insertPrefix(String prefix) {
		List<String> newFiles = new LinkedList<String>();
		List<String> newDirs = new LinkedList<String>();
		for (String file : files) {
			newFiles.add(prefix + file);
		}
		for (String dir : directories) {
			newDirs.add(prefix + dir);
		}
		this.directories = newDirs;
		this.files = newFiles;
	}
}
