package com.codebyscott.awstest;

import com.amazonaws.services.dynamodbv2.model.*;
import com.codebyscott.awstest.wrappers.AmazonDynamoDBClientWrap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

/**
 * Created by bradleyschwab on 4/4/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class DDBClientTest {

    @Mock
    AmazonDynamoDBClientWrap wrap;

    @Test
    public void createDDBTest() {
        DDBClient ddbClient = new DDBClient(wrap);

        ListTablesResult listTables = new ListTablesResult().withTableNames(Arrays.asList("table1","foobar"));
        when(wrap.listTables()).thenReturn(listTables);

        when(wrap.deleteTable(anyString())).thenReturn(new DeleteTableResult());

        CreateTableResult createResults = new CreateTableResult().withTableDescription(new TableDescription().withTableName("foo"));
        when(wrap.createTable(any())).thenReturn(createResults);

        when(wrap.putItem(any())).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                return null;
            }
        });
        ScanResult scanResult = new ScanResult().withCount(0);
        when(wrap.scan(any())).thenReturn(scanResult);

        ddbClient.createDDB();

    }

    @Test
    public void loadDataTest() {
        DDBClient ddbClient = new DDBClient(wrap);
        when(wrap.putItem(any())).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                return null;
            }
        });
        assertEquals("add results", 20, ddbClient.loadData("foo",20));
    }

    @Test
    public void countOldTest() {
        DDBClient ddbClient = new DDBClient(wrap);
        ScanResult scanResult = new ScanResult().withCount(0);
        when(wrap.scan(any())).thenReturn(scanResult);
        ddbClient.countOld("foo");
    }
}
