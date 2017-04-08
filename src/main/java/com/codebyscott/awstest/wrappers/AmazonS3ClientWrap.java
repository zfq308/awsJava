package com.codebyscott.awstest.wrappers;

import com.amazonaws.services.ec2.model.*;
import com.amazonaws.services.s3.model.*;

import java.io.InputStream;
import java.util.List;

/**
 * Created by bradleyschwab on 4/2/17.
 */
public interface AmazonS3ClientWrap {

    public List<Bucket> listBuckets();
    public Bucket createBucket(String bucket);
    public PutObjectResult putObject(String bucket, String key, InputStream contents, long contentsLength);
    public S3ObjectInputStream getObject(String bucket, String key);
    public BucketVersioningConfiguration getBucketVersioningConfiguration(String bucket);
    public Void setBucketVersioningConfiguration(SetBucketVersioningConfigurationRequest setBucketVersioningConfigurationRequest);
    public ObjectMetadata getObjectMetadataRequest(String bucket, String key);
    public ObjectListing listObjects(String bucketName);
}
