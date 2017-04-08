package com.codebyscott.awstest;


import com.amazonaws.services.s3.model.*;
import com.codebyscott.awstest.wrappers.AmazonS3ClientWrap;
import org.apache.commons.io.IOUtils;

import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by bradleyschwab on 4/4/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class S3ClientTest {
    @Mock
    AmazonS3ClientWrap wrap;

    @Test
    public void testCreateEC2() throws IOException {

        String testBucketName = "foo";
        String testFileName = "bar";
        S3Client s3Client = new S3Client(wrap);


        List<Bucket> bucketList = Arrays.asList(new Bucket("one"), new Bucket("two"));
        when(wrap.listBuckets()).thenReturn(bucketList);

        Bucket testBucket = new Bucket("foo");
        when(wrap.createBucket(testBucketName)).thenReturn(testBucket);

        when(wrap.putObject(anyString(), anyString(), any(), anyLong())).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                return null;
            }
        });


        HttpRequestBase base = new HttpRequestBase() {
            @Override
            public String getMethod() {
                return "GET";
            }
        };
        S3ObjectInputStream objectInputStream = new S3ObjectInputStream(IOUtils.toInputStream("hi", "UTF-8"), base);
        when(wrap.getObject(testBucketName, testFileName)).thenReturn(objectInputStream);

        ObjectMetadata meta = new ObjectMetadata();
        meta.setLastModified(new Date());
        when(wrap.getObjectMetadataRequest(testBucketName,testFileName)).thenReturn(meta);


        BucketVersioningConfiguration conf = new BucketVersioningConfiguration("Off");
        when(wrap.getBucketVersioningConfiguration(testBucketName)).thenReturn(conf);

        when(wrap.setBucketVersioningConfiguration(any())).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                return null;
            }
        });


        ObjectListing objectListing = new ObjectListing();
        objectListing.setBucketName(testBucketName);
        when(wrap.listObjects(testBucketName)).thenReturn(objectListing);

        s3Client.createStorage(testBucketName, testFileName, new IOUtils().toInputStream("fox","UTF-8"), 0l);


    }
    @Test
    public void versioningTest() {
        S3Client s3Client = new S3Client(wrap);

        BucketVersioningConfiguration conf = new BucketVersioningConfiguration("Enabled");
        when(wrap.getBucketVersioningConfiguration(anyString())).thenReturn(conf);

        when(wrap.setBucketVersioningConfiguration(any())).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                return null;
            }
        });

        s3Client.setVersioning("foo",false);
    }


    @Test
    public void getFileNamesTest() {
        S3Client s3Client = new S3Client(wrap);
        ObjectListing objectListing = new ObjectListing();
        objectListing.setBucketName("foo");

        when(wrap.listObjects("foo")).thenReturn(objectListing);
        List<String> s = s3Client.getFileNames("foo");
        assertEquals("emptyList test",0,s.size());
    }
}
