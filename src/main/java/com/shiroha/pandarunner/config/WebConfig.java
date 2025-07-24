package com.shiroha.pandarunner.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * web配置
 *
 * @author haowei703
 * @since 2025-07-03
 */
@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // 允许访问的前端地址
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Origin", "X-Requested-With", "Content-Type", "Accept")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/public/**")
                .addResourceLocations("classpath:/public/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/public/static/");
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SaUserResolver());
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }

    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        converter.setWriteAcceptCharset(false);
        return converter;
    }

    @Bean
    public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
        ByteArrayHttpMessageConverter byteArrayConverter = new ByteArrayHttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
        supportedMediaTypes.add(MediaType.IMAGE_PNG);
        byteArrayConverter.setSupportedMediaTypes(supportedMediaTypes);
        return byteArrayConverter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
        converters.add(new ResourceHttpMessageConverter());
        converters.add(stringHttpMessageConverter());
        converters.add(byteArrayHttpMessageConverter());
    }

    @Bean
    public MultipartResolver multipartResolver() {
        StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
        multipartResolver.setStrictServletCompliance(true);
        return multipartResolver;
    }
}
