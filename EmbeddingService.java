package com.acme.intelligence.rag;

import java.util.List;

public interface EmbeddingService {

    List<Float> embed(String text);
}
