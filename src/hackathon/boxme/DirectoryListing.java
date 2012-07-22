package hackathon.boxme;

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
}
