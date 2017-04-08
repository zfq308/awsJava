package com.codebyscott.awstest;

import com.amazonaws.services.s3.model.*;
import com.codebyscott.awstest.wrappers.AmazonS3ClientWrap;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bradleyschwab on 4/3/17.
 */
public class S3Client {

    Logger logger = Logger.getLogger(S3Client.class);

    AmazonS3ClientWrap wrap;

    public S3Client(AmazonS3ClientWrap wrap) {
        this.wrap = wrap;
    }

    public void createStorage(String testBucketName, String testFileName, InputStream contents, long contentsLength)
        throws IOException {

        if (!wrap.listBuckets().stream().anyMatch(b -> b.getName().equals(testBucketName))) {
            Bucket b = wrap.createBucket(testBucketName);
            setVersioning(testBucketName,true);
        }

        wrap.putObject(testBucketName,testFileName, contents, contentsLength);

        S3ObjectInputStream objectInputStream = wrap.getObject(testBucketName, testFileName);
        logger.debug("File contents: " + IOUtils.toString(objectInputStream,"UTF-8"));


        logger.debug(getFileNames(testBucketName));

        ObjectMetadata meta = wrap.getObjectMetadataRequest(testBucketName,testFileName);
        if (meta != null) {
            logger.debug(meta.getVersionId());
            logger.debug(meta.getETag());
            logger.debug(meta.getLastModified());
        } else {
            logger.debug(testFileName + " was not found");
        }
    }

    /**
     * Set versioning on or off on the bucket, only if needed
     *
     * @param bucketName
     * @param versioningOn
     */
    public void setVersioning(String bucketName, boolean versioningOn) {
        BucketVersioningConfiguration bucketVersioningConfiguration = wrap.getBucketVersioningConfiguration(bucketName);
        logger.debug(bucketVersioningConfiguration.getStatus());
        if (bucketVersioningConfiguration.getStatus().equals("Enabled") && !versioningOn) {
            BucketVersioningConfiguration toggle = new BucketVersioningConfiguration().withStatus("Suspended");
            SetBucketVersioningConfigurationRequest request = new SetBucketVersioningConfigurationRequest(bucketName, toggle);
            wrap.setBucketVersioningConfiguration(request);
        } else if (!bucketVersioningConfiguration.getStatus().equals("Enabled") && versioningOn) {
            BucketVersioningConfiguration toggle = new BucketVersioningConfiguration().withStatus("Enabled");
            SetBucketVersioningConfigurationRequest request = new SetBucketVersioningConfigurationRequest(bucketName, toggle);
            wrap.setBucketVersioningConfiguration(request);
        }
    }

    public List<String> getFileNames(String bucketName) {
        ObjectListing objectListing = wrap.listObjects(bucketName);
        List<S3ObjectSummary> summaries = objectListing.getObjectSummaries();
        List<String> filesNames = summaries.stream().map(x -> x.getKey()).collect(Collectors.toList());
        return filesNames;
    }

}
