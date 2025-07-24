package com.shiroha.pandarunner.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(ApiProperties.class)
public class WebClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
