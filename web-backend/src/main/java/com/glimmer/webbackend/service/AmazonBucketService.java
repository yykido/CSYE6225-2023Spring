package com.glimmer.webbackend.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassNAME AmazonBucketService
 * @Description TODO
 * @Author glimmer
 * @Date 2023/3/2 00:55
 * @Version 1.0
 */
@AllArgsConstructor
@Service
public class AmazonBucketService
{
  @Autowired
  private final S3Client s3client;


  public void upload(String bucketMame,
                     String s3path,
                     InputStream inputStream,
                     String contentType) throws IOException
  {
      s3client.putObject(PutObjectRequest.builder()
                                         .bucket(bucketMame)
                                         .key(s3path)
                                         .contentType(contentType)
                                         .build(),
                         RequestBody.fromInputStream(inputStream, inputStream.available()));

  }

  public void delete(String bucketName, String s3path)
  {
    s3client.deleteObject(b -> b.bucket(bucketName).key(s3path));
  }
}
