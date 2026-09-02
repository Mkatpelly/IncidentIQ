package com.acme.intelligence.rag;

import com.pgvector.PGvector;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.UUID;

@Repository
public class KnowledgeDocumentRepository {

    private final JdbcTemplate jdbcTemplate;

    public KnowledgeDocumentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(
            UUID id,
            String tenantId,
            String title,
            String sourceUri,
            DocumentType documentType,
            String content,
            String contentHash,
            List<Float> embedding
    ) {
        String sql = """
                INSERT INTO knowledge_documents (
                    id,
                    tenant_id,
                    title,
                    source_uri,
                    document_type,
                    content,
                    embedding,
                    content_hash
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(connection -> {
            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setObject(1, id);
            statement.setString(2, tenantId);
            statement.setString(3, title);
            statement.setString(4, sourceUri);
            statement.setString(5, documentType.name());
            statement.setString(6, content);
            statement.setObject(7, new PGvector(toFloatArray(embedding)));
            statement.setString(8, contentHash);

            return statement;
        });
    }

    public List<DocumentChunk> semanticSearch(
            String tenantId,
            List<Float> queryEmbedding,
            int topK
    ) {
        String sql = """
                SELECT
                    id,
                    tenant_id,
                    title,
                    source_uri,
                    document_type,
                    content,
                    1 - (embedding <=> ?::vector) AS similarity
                FROM knowledge_documents
                WHERE tenant_id = ?
                ORDER BY embedding <=> ?::vector
                LIMIT ?
                """;

        PGvector vector = new PGvector(toFloatArray(queryEmbedding));

        return jdbcTemplate.query(
                sql,
                new Object[]{vector, tenantId, vector, topK},
                documentChunkRowMapper()
        );
    }

    private RowMapper<DocumentChunk> documentChunkRowMapper() {
        return (resultSet, rowNumber) -> new DocumentChunk(
                resultSet.getObject("id", UUID.class),
                resultSet.getString("tenant_id"),
                resultSet.getString("title"),
                resultSet.getString("source_uri"),
                DocumentType.valueOf(
                        resultSet.getString("document_type")
                ),
                resultSet.getString("content"),
                resultSet.getDouble("similarity")
        );
    }

    private float[] toFloatArray(List<Float> values) {
        float[] result = new float[values.size()];

        for (int i = 0; i < values.size(); i++) {
            result[i] = values.get(i);
        }

        return result;
    }
}
