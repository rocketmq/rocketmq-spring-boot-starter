package org.rocketmq.starter.core.consumer;

/**
 * 消费状态的枚举
 *
 * @author He Jialin
 */
public enum ConsumeStatus {
    /**
     * 消费成功
     */
    SUCCESS,
    /**
     * 需要重试
     */
    RETRY
}
