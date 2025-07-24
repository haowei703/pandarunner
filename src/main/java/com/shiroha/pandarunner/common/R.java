package com.shiroha.pandarunner.common;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 通用响应封装类
 *
 * @param <T> 响应数据类型
 */
@Data
@Accessors(chain = true) // 支持链式调用
@Schema(description = "通用响应封装")
public class R<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "状态码", example = "200", requiredMode = Schema.RequiredMode.REQUIRED)
    private int code;

    @Schema(description = "提示信息", example = "操作成功")
    private String message;

    @Schema(description = "响应数据")
    private T data;

    private R(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> R<T> ok() {
        return ok("success", null);
    }

    /**
     * 成功响应（带消息）
     */
    public static <T> R<T> ok(String message) {
        return ok(message, null);
    }

    /**
     * 成功响应（带消息、数据）
     */
    public static <T> R<T> ok(String message, T data) {
        return new R<>(200, message, data);
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> R<T> ok(T data) {
        return ok("success", data);
    }

    /**
     * 失败响应（自定义状态码）
     */
    public static <T> R<T> fail(int code, String message) {
        return new R<>(code, message, null);
    }

    /**
     * 失败响应（默认状态码）
     */
    public static <T> R<T> fail(String message) {
        return fail(500, message);
    }
}
