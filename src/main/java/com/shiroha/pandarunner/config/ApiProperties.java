package com.shiroha.pandarunner.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "api")
public class ApiProperties {
    /**
     * API key
     */
    private String key;
}
