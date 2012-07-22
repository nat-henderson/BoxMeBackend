package hackathon.boxme;

import java.io.IOException;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;

public class S3PutFileRequest implements Runnable {
	
	String bucket;
	String filename;
	FileCopyStream stream;
	AmazonS3Client client;

	public S3PutFileRequest(String bucket, String filename,
			FileCopyStream stream, AmazonS3Client client) {
		this.bucket = bucket;
		this.filename = filename;
		this.stream = stream;
		this.client = client;
	}

	@Override
	public void run() {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(stream.getSize());
		client.putObject(bucket, filename, stream.getInputStream(), metadata);
		try {
			stream.getInputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
