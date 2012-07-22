package hackathon.boxme;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class S3StorageProvider implements StorageProvider {
	
	public AmazonS3Client getClient(String credentials) {
		final String secret = credentials.split(" ")[1];
		final String access = credentials.split(" ")[0];
		return new AmazonS3Client(new AWSCredentials() {
			
			@Override
			public String getAWSSecretKey() {
				return secret;
			}
			
			@Override
			public String getAWSAccessKeyId() {
				return access;
			}
		});
	}

	@Override
	public void putFile(String fileKey, FileCopyStream stream,
			String receiverCredentials) {
		AmazonS3Client client = getClient(receiverCredentials);
		String bucket = fileKey.split(" ")[0];
		String filename = fileKey.split(" ")[1];
		new Thread(new S3PutFileRequest(bucket, filename, stream, client)).start();
	}

	@Override
	public FileCopyStream getFile(String fileKey, String senderCredentials) {
		FileCopyStream stream = new FileCopyStream();
		AmazonS3Client client = getClient(senderCredentials);
		S3Object obj = client.getObject(fileKey.split(" ")[0], fileKey.split(" ")[1]);
		stream.setInputStream(obj.getObjectContent());
		stream.setSize(obj.getObjectMetadata().getContentLength());
		return stream;
	}

	@Override
	public DirectoryListing getFilesUnderPath(String path, String credentials) {
		DirectoryListing dl = new DirectoryListing();
		List<String> dirs = new LinkedList<String>();
		List<String> files = new LinkedList<String>();
		AmazonS3Client client = getClient(credentials);
		String bucket = null;
		String fileName = null;
		if (path.split(" ").length > 1) {
			bucket = path.split(" ")[0];
			fileName = "";
			for (int i = 1; i < path.split(" ").length; i++) {
				fileName += path.split(" ")[i];
			}
		} else if (path.length() > 0) {
			bucket = path;
		}
		List<Bucket> buckets = client.listBuckets();
		for (Bucket b : buckets){
			if (bucket == null || b.getName().startsWith(bucket)) {
				ObjectListing ol = client.listObjects(path.split(" ")[0], path.split(" ")[1]);
				do {
					for (S3ObjectSummary sum : ol.getObjectSummaries()) {
						if (fileName == null || sum.getKey().startsWith(fileName)) {
							if (sum.getSize() == 0) {
								dirs.add(sum.getBucketName() + " " + sum.getKey());
							} else {
								files.add(sum.getBucketName() + " " + sum.getKey());
							}
						}
					}
					ol = client.listNextBatchOfObjects(ol);
				} while (ol.isTruncated());
			}
		}
		dl.setDirectories(dirs);
		dl.setFiles(files);
		return dl;
	}

	@Override
	public boolean copyFile(String fileKey, String senderCredentials,
			String receiverCredentials) {
		throw new RuntimeException(new OperationNotSupportedException());
	}

	@Override
	public List<String> listAllFiles(String credentials) {
		throw new RuntimeException(new OperationNotSupportedException());
	}

}
