package com.company.aws.factory;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Component
public class LocalDynamoDbClientFactory implements DynamoDbClientFactory {

    private static final String TABLE_NAME = "dynamodb-local-table";
    private final String dynamoDbLocalPort = "8000";

    public LocalDynamoDbClientFactory() {
        // add code here for using dynamic port number
    }

    @Override
    public DynamoDbClient create() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create("RANDOM_ACCESS_KEY", "RANDOM_SELECT_ACCESS_KEY");
        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);

        DynamoDbClient client = DynamoDbClient.builder()
                .region(Region.EU_WEST_1)
                .httpClient(ApacheHttpClient.builder().build())
                .endpointOverride(URI.create("http://localhost:" + dynamoDbLocalPort))
                .credentialsProvider(credentialsProvider)
                .build();

        createTable(client);

        return client;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    private void createTable(@NotNull DynamoDbClient client) {
        List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
        AttributeDefinition idAttributeDefinition = AttributeDefinition.builder()
                .attributeName("id")
                .attributeType("S")
                .build();
        AttributeDefinition createdDateAttributeDefinition = AttributeDefinition.builder()
                .attributeName("created_date")
                .attributeType("S")
                .build();
        attributeDefinitions.add(idAttributeDefinition);
        attributeDefinitions.add(createdDateAttributeDefinition);

        List<KeySchemaElement> keySchema = new ArrayList<>();
        KeySchemaElement hashKey = KeySchemaElement.builder().attributeName("id").keyType(KeyType.HASH).build();
        KeySchemaElement rangeKey = KeySchemaElement.builder().attributeName("created_date").keyType(KeyType.RANGE).build();
        keySchema.add(hashKey);
        keySchema.add(rangeKey);

        ProvisionedThroughput provisionedThroughput = ProvisionedThroughput.builder()
                .readCapacityUnits(1L)
                .writeCapacityUnits(1L)
                .build();

        CreateTableRequest createTableRequest = CreateTableRequest.builder()
                .tableName(TABLE_NAME)
                .provisionedThroughput(provisionedThroughput)
                .attributeDefinitions(attributeDefinitions)
                .keySchema(keySchema)
                .build();

        client.createTable(createTableRequest);
    }
}
