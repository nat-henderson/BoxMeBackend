package hackathon.boxme;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

public interface StorageProvider {
	
	public void putFile(FileInputStream inputStream, List<String> credentials);
	
	public FileOutputStream getFile(String key, String credentials);
	
	public List<String> listAllFiles(String credentials);
	
}
