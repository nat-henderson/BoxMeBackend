package hackathon.boxme;

import java.io.InputStream;

public class FileCopyStream {
	private InputStream inputStream;
	private long size;
	
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
}
