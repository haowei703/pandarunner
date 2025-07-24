package com.shiroha.pandarunner.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component("ArrayKeyGenerator")
public class ArrayKeyGenerator implements KeyGenerator {

    @Override
    public @NonNull Object generate(
            @NonNull Object target,
            @NonNull Method method,
            @NonNull Object... params
    ) {
        // 检查参数是否为空
        if (params.length == 0) {
            throw new IllegalArgumentException("Method requires at least one parameter");
        }

        // 提取并验证第一个参数为 List<Long>
        Object firstParam = params[0];
        if (!(firstParam instanceof List<?>)) {
            throw new IllegalArgumentException(
                    "First parameter must be List<Long>, but got: " + firstParam.getClass().getSimpleName()
            );
        }

        List<Long> ids = convertToListOfLong((List<?>) firstParam);
        if (ids.isEmpty()) {
            log.warn("Generating key for empty List<Long> (method: {})", method.getName());
            return ""; // 空列表返回空字符串作为键（避免 null）
        }

        // 排序后拼接为字符串键（确保顺序无关性）
        String key = ids.stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        log.debug("Generated cache key: {} (method: {})", key, method.getName());
        return key;
    }

    /**
     * 将 List<?> 安全转换为 List<Long>，验证每个元素类型
     */
    private List<Long> convertToListOfLong(List<?> rawList) {
        List<Long> result = new ArrayList<>(rawList.size());
        for (Object element : rawList) {
            if (element == null) {
                throw new IllegalArgumentException("List contains null elements (not allowed)");
            }
            if (element instanceof Long) {
                result.add((Long) element);
            } else if (element instanceof Number) {
                // 支持 Integer 等数字类型自动转换为 Long
                result.add(((Number) element).longValue());
            } else {
                throw new IllegalArgumentException(
                        "List element must be Long/Number, but got: " + element.getClass().getSimpleName()
                );
            }
        }
        return result;
    }
}