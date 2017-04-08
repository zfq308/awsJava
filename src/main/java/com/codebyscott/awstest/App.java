package com.codebyscott.awstest;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.codebyscott.awstest.wrappers.*;
import com.codebyscott.awstest.wrappers.impl.*;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;


/**
 * Hello world!
 *
 */
public class App 
{

    // this is a test

    public static Logger logger = Logger.getLogger(App.class);

    public static void main( String[] args ) throws Exception {;
        EnvironmentVariableCredentialsProvider credentials = new EnvironmentVariableCredentialsProvider();

        AmazonEC2 ec2Client =
                AmazonEC2ClientBuilder.standard().withCredentials(credentials).build();
        AmazonEC2ClientBuilder.standard().withCredentials(new ProfileCredentialsProvider("java1")).build();

        AmazonEC2ClientWrap wrap1 = new AmzonEC2ClientWrapImpl(ec2Client);

        Ec2Client ec2 = new Ec2Client(wrap1);

        ec2.createEC2("devTest02","devTest2Key","10.0.0.0/16","10.0.1.0/24");

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(credentials).build();

        AmazonS3ClientWrap wrap2 = new AmazonS3ClientWrapImpl(s3Client);

        S3Client s3 = new S3Client(wrap2);

        String contents = "this is a test";
        s3.createStorage("code.by.scott.message.test4","text002.txt", IOUtils.toInputStream(contents,"UTF-8"),contents.length());

        AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard().withCredentials(credentials).build();
        AmazonDynamoDBClientWrap wrap3 = new AmazonDynamoDBClientWrapImpl(dynamoDB);
        DDBClient ddbClient = new DDBClient(wrap3);
        ddbClient.createDDB();

        AmazonSNS snsClient = AmazonSNSClientBuilder.standard().withCredentials(credentials).build();
        AmazonSNSClientWrap wrap4 = new AmazonSNSClientWrapImpl(snsClient);
        SNSClient sns = new SNSClient(wrap4);
        sns.sendMessage("topic0101",10);

        AmazonSQS sqsClient = AmazonSQSClientBuilder.standard().withCredentials(credentials).build();
        AmazonSQSClientWrap wrap5 = new AmazonSQSClientWrapImpl(sqsClient);
        SQSClient sqs = new SQSClient(wrap5);
        sqs.createMessageQueue();

    }
}
