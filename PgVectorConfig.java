package com.acme.intelligence.config;

import com.pgvector.PGvector;
import org.postgresql.PGConnection;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;

@Configuration
public class PgVectorConfig {

    private final DataSource dataSource;

    public PgVectorConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void registerPgVectorType() {
        try (Connection connection = dataSource.getConnection()) {
            PGConnection pgConnection = connection.unwrap(PGConnection.class);
            pgConnection.addDataType("vector", PGvector.class);
        } catch (Exception exception) {
            throw new IllegalStateException(
                    "Unable to register PostgreSQL pgvector type.",
                    exception
            );
        }
    }
}