package com.glimmer.webbackend.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

import java.net.URI;

/**
 * @ClassNAME AmazonConfig
 * @Description TODO
 * @Author glimmer
 * @Date 2023/3/1 23:05
 * @Version 1.0
 */
@Configuration
public class AmazonConfig {

  @Value("${aws_region}")
  private String awsRegion;

  @Bean
  public S3Client s3client() {
    S3Client s3Client = S3Client
        .builder()
        .credentialsProvider(InstanceProfileCredentialsProvider.create())
        .region(Region.of(awsRegion)).build();
    return s3Client;
  }
}
