package com.company.aws.factory;

import com.company.aws.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class LocalDynamoDbClientTestIT {

    private static final String TABLE_NAME = "dynamodb-local-table";
    private static final String ID = "id12345";
    private Map<String,AttributeValue> record1;
    private Map<String,AttributeValue> record2;

    @Autowired
    private DynamoDbClient dynamoDbClient;

    @Before
    public void setUp() {
        record1 = createRecord(ID, LocalDateTime.of(2019,1,1,9,0,0));
        record2 = createRecord(ID, LocalDateTime.of(2019,1,1,9,30,0));
        dynamoDbClient.putItem(PutItemRequest.builder().tableName(TABLE_NAME).item(record1).build());
        dynamoDbClient.putItem(PutItemRequest.builder().tableName(TABLE_NAME).item(record2).build());
    }

    @Test
    public void shouldGetAll() {
        String keyConditionExpression = "id = :val1";

        Map<String,AttributeValue> expressAttributeValues = new HashMap<>();
        expressAttributeValues.put(":val1", AttributeValue.builder().s(ID).build());

        QueryResponse queryResponse = dynamoDbClient.query(QueryRequest.builder()
                .tableName(TABLE_NAME)
                .keyConditionExpression(keyConditionExpression)
                .expressionAttributeValues(expressAttributeValues)
                .build());

        assertNotNull(queryResponse);
        assertEquals( Integer.valueOf(2), queryResponse.count());
        assertTrue(queryResponse.items().contains(record1));
        assertTrue(queryResponse.items().contains(record2));
    }

    private static Map<String, AttributeValue> createRecord(String id, LocalDateTime createdDate) {
        Map<String,AttributeValue> attributes = new HashMap<>();
        attributes.put("id", AttributeValue.builder().s(id).build());
        attributes.put("created_date", AttributeValue.builder().s(createdDate.toString()).build());
        return attributes;
    }
}
