package com.codebyscott.awstest.wrappers.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.codebyscott.awstest.wrappers.AmazonS3ClientWrap;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.List;

/**
 * Created by bradleyschwab on 4/4/17.
 */
public class AmazonS3ClientWrapImpl implements AmazonS3ClientWrap {
    Logger logger = Logger.getLogger(AmazonS3ClientWrapImpl.class);

    private AmazonS3 s3Client;

    public AmazonS3ClientWrapImpl(AmazonS3 s3Client) {
        logger.debug("Starting client wrapper");
        this.s3Client = s3Client;
    }
    @Override
    public List<Bucket> listBuckets() {
        List<Bucket> result = s3Client.listBuckets();
        logger.debug("listBuckets " + result);
        return result;
    }
    @Override
    public Bucket createBucket(String bucket) {
        Bucket result = s3Client.createBucket(bucket);
        logger.debug("create bucket results " + result);
        return result;
    }
    @Override
    public PutObjectResult putObject(String bucket, String key, InputStream contents, long contentsLength) {
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType("text/plain");
        meta.setContentLength(contentsLength);
        PutObjectResult result =  s3Client.putObject(bucket, key, contents, meta);
        logger.debug("putObject result " + result);
        return  result;
    }
    @Override
    public S3ObjectInputStream getObject(String bucket, String key) {
        S3Object object =s3Client.getObject(bucket,key);
        return object.getObjectContent();
    }
    @Override
    public ObjectMetadata getObjectMetadataRequest(String bucket, String key) {
        GetObjectMetadataRequest getObjectMetadataRequest = new GetObjectMetadataRequest(bucket,key);
        ObjectMetadata metadata = s3Client.getObjectMetadata(getObjectMetadataRequest);
        logger.debug("getObjectMetadataRequest results " + metadata);
        return metadata;
    }
    @Override
    public BucketVersioningConfiguration getBucketVersioningConfiguration(String bucket) {
        BucketVersioningConfiguration result = s3Client.getBucketVersioningConfiguration(bucket);
        logger.debug("getBucketVersionConfiguration result " + result);
        return result;
    }
    @Override
    public Void setBucketVersioningConfiguration(SetBucketVersioningConfigurationRequest setBucketVersioningConfigurationRequest) {
        s3Client.setBucketVersioningConfiguration(setBucketVersioningConfigurationRequest);
        logger.debug("setBucketVersioningConfiguration returned");
        return null;
    }
    @Override
    public ObjectListing listObjects(String bucketName) {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName);
        ObjectListing listing = s3Client.listObjects(listObjectsRequest);
        logger.debug("listObjects " + listing);
        return listing;
    }

}
