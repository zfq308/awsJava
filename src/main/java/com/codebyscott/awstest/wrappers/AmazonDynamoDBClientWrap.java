package com.codebyscott.awstest.wrappers;

import com.amazonaws.services.dynamodbv2.model.*;

/**
 * Created by bradleyschwab on 4/4/17.
 */
public interface AmazonDynamoDBClientWrap {
    public ListTablesResult listTables();
    public DeleteTableResult deleteTable(String testTableName);
    public CreateTableResult createTable(CreateTableRequest createTableRequest);
    public Void putItem(PutItemRequest putItemRequest);
    public ScanResult scan(ScanRequest scanRequest);


}
