package com.codebyscott.awstest;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.codebyscott.awstest.wrappers.AmazonDynamoDBClientWrap;
import com.codebyscott.awstest.wrappers.AmazonEC2ClientWrap;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by bradleyschwab on 4/4/17.
 */
public class DDBClient {

    public static Logger logger = Logger.getLogger(DDBClient.class);

    AmazonDynamoDBClientWrap wrap = null;

    public DDBClient(AmazonDynamoDBClientWrap wrap) {
        this.wrap = wrap;
    }

    //public void createDDB(String dbName, KeySchemaElement keySchemaElement) {

    public void createDDB() {

        String testTableName = "foobar";

        ListTablesResult tables = wrap.listTables();
        logger.debug(tables.getTableNames());
        if (tables.getTableNames().contains(testTableName)) {
            logger.debug("Test table " + testTableName + " already exists, removing it");
            DeleteTableResult deleteTableResult = wrap.deleteTable(testTableName);
            logger.debug(deleteTableResult);
            //sleep a couple of seconds to allow delete to happen
            logger.debug("a cheap way to wait for table to be deleted");
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        List<AttributeDefinition> attributeDefinitionList = Arrays.asList(
                new AttributeDefinition("id",ScalarAttributeType.S)

        );

        List<KeySchemaElement> keyList = Arrays.asList(
                new KeySchemaElement().withAttributeName("id").withKeyType(KeyType.HASH)
        );

        CreateTableRequest createTableRequest = new CreateTableRequest()
                .withTableName(testTableName)
                .withAttributeDefinitions(attributeDefinitionList)
                .withKeySchema(keyList)
                .withProvisionedThroughput(new ProvisionedThroughput(1l,1l));

        CreateTableResult createTableResult = wrap.createTable(createTableRequest);
        logger.debug(createTableResult);
        createTableResult.getTableDescription();
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        loadData(testTableName,100);
        countOld(testTableName);
    }
    // crude way to stuff some data into the table
    public int loadData(String tableName, int numberToAdd) {
        Random random  = new Random();
        for (int i = 0; i < numberToAdd; i++) {
            int age = random.nextInt(100);
            Map<String,AttributeValue> entries = new HashMap<>();
            entries.put("id", new AttributeValue().withS(UUID.randomUUID().toString()));
            entries.put("age", new AttributeValue().withN(Integer.toString(age)));
            if (age > 50) {
                entries.put("AARP",new AttributeValue().withBOOL(true));
            }
            PutItemRequest putItemRequest = new PutItemRequest()
                    .withTableName(tableName)
                    .withItem(entries);

            wrap.putItem(putItemRequest);
            logger.debug("added " + i);
        }
        return numberToAdd;
    }

    List<AttributeValue> scanList = Arrays.asList(
            new AttributeValue().withBOOL(true));

    public int countOld(String tableName) {
        ScanRequest scanRequest = new ScanRequest().withTableName(tableName).addScanFilterEntry("AARP",
                new Condition().withAttributeValueList(scanList).withComparisonOperator(ComparisonOperator.EQ));

        ScanResult result = wrap.scan(scanRequest);
        logger.debug(result);
        logger.debug(result.getCount());
        return result.getCount();
    }




}
