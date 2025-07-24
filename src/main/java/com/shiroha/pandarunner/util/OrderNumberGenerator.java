package com.shiroha.pandarunner.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderNumberGenerator {
    // 格式化：年月日时分秒
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    // 商家ID占位长度
    private static final int MERCHANT_ID_LENGTH = 5;
    // 序列数位长度
    private static final int SEQUENCE_LENGTH = 4;
    // 每毫秒最大序列号
    private static final int MAX_SEQUENCE = (int) Math.pow(10, SEQUENCE_LENGTH) - 1;

    // 商家ID补零填充器
    private static final String MERCHANT_PAD = String.format("%0" + MERCHANT_ID_LENGTH + "d", 0);
    // 序列号补零填充器
    private static final String SEQUENCE_PAD = String.format("%0" + SEQUENCE_LENGTH + "d", 0);

    // 当前毫秒时间戳
    private static long currentTimestamp = System.currentTimeMillis();
    // 序列号计数器
    private static final AtomicInteger sequence = new AtomicInteger(0);

    /**
     * 生成唯一订单号
     * 格式：时间戳(14位)+商家ID(5位，补零)+序列号(4位，补零)
     * 例如：20230615123045000120001
     */
    public static synchronized String generateOrderNumber(Long merchantId) {
        long now = System.currentTimeMillis();

        // 时间回拨处理（简化版：等待时钟恢复）
        if (now < currentTimestamp) {
            now = waitForNextMillis(currentTimestamp);
        }

        // 时间戳变化，重置序列号
        if (now != currentTimestamp) {
            currentTimestamp = now;
            sequence.set(0);
        }

        // 序列号溢出处理（高并发场景）
        if (sequence.incrementAndGet() > MAX_SEQUENCE) {
            currentTimestamp = waitForNextMillis(currentTimestamp);
            sequence.set(0);
        }

        // 格式化时间戳
        String timestampPart = LocalDateTime.now().format(FORMATTER);

        // 格式化商家ID（补零到5位）
        String merchantPart = MERCHANT_PAD.substring(0, MERCHANT_ID_LENGTH - String.valueOf(merchantId).length()) + merchantId;

        // 格式化序列号（补零到4位）
        String sequencePart = SEQUENCE_PAD.substring(0, SEQUENCE_LENGTH - String.valueOf(sequence.get()).length()) + sequence.get();

        return timestampPart + merchantPart + sequencePart;
    }

    // 等待下一毫秒
    private static long waitForNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
