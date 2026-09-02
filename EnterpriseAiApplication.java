package com.acme.intelligence;

import com.acme.intelligence.config.EmbeddingProperties;
import com.acme.intelligence.config.RagProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        EmbeddingProperties.class,
        RagProperties.class
})
public class EnterpriseAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnterpriseAiApplication.class, args);
    }
}
