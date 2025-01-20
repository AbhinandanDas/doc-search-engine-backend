package com.neo.powersearch.search.service.impl;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.neo.powersearch.search.config.S3BucketConfiguration;
import com.neo.powersearch.search.service.FileDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FileDownloadS3ServiceImpl implements FileDownloadService {

    private final AmazonS3 s3Client;

    @Autowired
    private S3BucketConfiguration s3BucketConfiguration;
    public FileDownloadS3ServiceImpl(@Value("${aws.accessKeyId}") String accessKeyId,
                                   @Value("${aws.secretKey}") String secretKey,
                                   @Value("${aws.region}") String region) {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKeyId,secretKey,region);
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .build();
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadFile(String fileName) throws IOException {
        // Get the file from the S3 bucket.
        S3Object s3Object = s3Client.getObject(s3BucketConfiguration.getBucketName(),fileName);

        //Extract the input stream of the file
        InputStreamResource resource = new InputStreamResource(s3Object.getObjectContent());

        //Return file as a response entity
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"").body(resource);
    }

}
