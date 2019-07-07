package com.company.aws.factory;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public interface DynamoDbClientFactory {
    DynamoDbClient create();
    String getTableName();
}
