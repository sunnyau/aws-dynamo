package com.company.aws.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDbClientConfig {

    private DynamoDbClientFactory factory;

    @Autowired
    public DynamoDbClientConfig(DynamoDbClientFactory factory) {
        this.factory = factory;
    }

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return factory.create();
    }

    @Bean
    public String dynamoDbTableName() {
        return factory.getTableName();
    }
}
