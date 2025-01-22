package com.neo.powersearch.search.service.impl;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.neo.powersearch.search.service.FileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class FileUploadS3ServiceImpl implements FileUploadService {
    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public FileUploadS3ServiceImpl(@Value("${aws.accessKeyId}") String accessKeyId,
                                   @Value("${aws.secretKey}") String secretKey,
                                   @Value("${aws.region}") String region) {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKeyId,secretKey,region);
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .build();
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        File fileObject = convertMultipartFileToFile(file);
        String fileName = file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObject));
        fileObject.delete();
        return "File Uploaded: " + fileName;
    }

    public File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convFileName = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFileName);
        fos.write(file.getBytes());
        fos.close();
        return convFileName;
    }
}
