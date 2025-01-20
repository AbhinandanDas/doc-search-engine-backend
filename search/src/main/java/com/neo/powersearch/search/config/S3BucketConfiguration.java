package com.neo.powersearch.search.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3BucketConfiguration {

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public String getBucketName() {
        return bucketName;
    }
}
