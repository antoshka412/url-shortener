package com.antonina.urlshortener;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class IntegrationTestBase {

    @Container
    public static GenericContainer cassandraContainer = new GenericContainer(DockerImageName.parse("cassandra:4.1")).withExposedPorts(6379);

    @BeforeAll
    static void startContainer() {
        cassandraContainer.start();
    }

    @DynamicPropertySource
    static void cassandraProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cassandra.contact-points", cassandraContainer::getHost);
        registry.add("spring.cassandra.port", () -> cassandraContainer.getMappedPort(9042));
        registry.add("spring.cassandra.keyspace-name", () -> "test_keyspace");
        registry.add("spring.cassandra.schema-action", () -> "CREATE_IF_NOT_EXISTS");
    }

}
