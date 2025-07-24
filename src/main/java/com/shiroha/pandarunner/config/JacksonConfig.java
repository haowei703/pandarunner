package com.shiroha.pandarunner.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;
import java.time.Instant;

/**
 * jackson配置类
 *
 * @author haowei703
 * @since 2025-07-04
 */
@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
        // 设置序列化时包含全部字段
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(Instant.class, InstantSerializer.INSTANCE);
        javaTimeModule.addDeserializer(Instant.class, InstantDeserializer.INSTANT);
        objectMapper.registerModule(javaTimeModule);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        return objectMapper;
    }
}
