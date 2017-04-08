package com.codebyscott.awstest.wrappers.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import com.codebyscott.awstest.wrappers.AmazonDynamoDBClientWrap;
import org.apache.log4j.Logger;

/**
 * Created by bradleyschwab on 4/4/17.
 */
public class AmazonDynamoDBClientWrapImpl implements AmazonDynamoDBClientWrap {
    Logger logger = Logger.getLogger(AmazonDynamoDBClientWrapImpl.class);

    private AmazonDynamoDB dynamoDB = null;

    public AmazonDynamoDBClientWrapImpl(AmazonDynamoDB dynamoDB) {
        logger.debug("Starting DynamoDB wrapper");
        this.dynamoDB = dynamoDB;
    }
    @Override
    public ListTablesResult listTables() {
        ListTablesResult results = dynamoDB.listTables();
        logger.debug("listTable results " + results);
        return results;
    }

    @Override
    public DeleteTableResult deleteTable(String testTableName) {
        DeleteTableResult results = dynamoDB.deleteTable(testTableName);
        logger.debug("deleteTable results " + results);
        return results;
    }
    @Override
    public CreateTableResult createTable(CreateTableRequest createTableRequest) {
        CreateTableResult results = dynamoDB.createTable(createTableRequest);
        logger.debug("create table respone " + results);
        return results;
    }
    @Override
    public Void putItem(PutItemRequest putItemRequest) {
        dynamoDB.putItem(putItemRequest);
        logger.debug("PutItemRequest called with " + putItemRequest);
        return null;
    }
    @Override
    public ScanResult scan(ScanRequest scanRequest) {
        ScanResult results = dynamoDB.scan(scanRequest);
        logger.debug("Scan returned " + results);
        return results;
    }


}
