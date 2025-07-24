package com.shiroha.pandarunner.config;

import cn.dev33.satoken.stp.StpUtil;
import com.shiroha.pandarunner.annotation.SaLoginId;
import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class SaUserResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(@NonNull MethodParameter parameter) {
        return parameter.hasParameterAnnotation(SaLoginId.class)
                && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(
            @NonNull MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            @NonNull NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        StpUtil.checkLogin();
        return StpUtil.getLoginIdAsLong();
    }
}
