package com.keiken.openai.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.keiken.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class OpenAiConfig {

    private final AppProperties appProperties;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(((request, body, execution) -> {
            request.getHeaders().add(
                    "Authorization", "Bearer " + appProperties.getOpenaiApi().getKey()
            );
            return execution.execute(request, body);
        }));
        return restTemplate;
    }

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder().expireAfterWrite(20, TimeUnit.MINUTES);
    }

    @Bean
    public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("summaries");
        cacheManager.setCaffeine(caffeine);

        return cacheManager;
    }
}