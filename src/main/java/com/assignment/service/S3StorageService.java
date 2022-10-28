package com.assignment.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * AWS storage service to upload the contents to S3 bucket
 * @author GS
 *
 */
@Slf4j
@Service
public class S3StorageService implements StorageService {

	private AmazonS3 s3client;

	@Value("${s3.endpointUrl}")
	private String endpointUrl;

	@Value("${s3.bucketName}")
	private String bucketName;

	@Value("${s3.accessKey}")
	private String accessKey;

	@Value("${s3.secretKey}")
	private String secretKey;

	/**
	 * uploade the contents of the jsonString to the s3 bucket
	 */
	@Override
	public String uploadFile(String jsonFile) {
		File tmpFile = null;
		try {
			tmpFile = File.createTempFile("test", ".tmp");
			FileWriter writer = new FileWriter(tmpFile);
			writer.write(jsonFile);
			writer.close();
			String s3FileName = generateFileName();
			s3client.putObject(new PutObjectRequest(bucketName, s3FileName, tmpFile)
					.withCannedAcl(CannedAccessControlList.BucketOwnerFullControl));
			return s3FileName;
		} catch (IOException e) {
			log.error("Error while uploading the case details to S3 bucket", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (tmpFile != null && tmpFile.exists()) {
					tmpFile.delete();
				}
			} catch (Exception ex) {
				log.warn("Couldn't delete the temp file: {}", tmpFile);
				// No need to do anything
			}
		}

	}

	private String generateFileName() {

		return "case-details-" + LocalDateTime.now() + ".json";
	}

	/**
	 * initialize the s3client with AWSCredential after the S3StorageService
	 * initialization
	 */
	@SuppressWarnings("deprecation")
	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}

}
