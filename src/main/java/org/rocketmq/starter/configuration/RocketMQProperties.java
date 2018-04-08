package org.rocketmq.starter.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * rocketmq基本配置
 *
 * @author He Jialin
 */
@SuppressWarnings("WeakerAccess")
@ConfigurationProperties(prefix = "spring.rocketmq")
@Data
public final class RocketMQProperties {

    /**
     * @see org.apache.rocketmq.client.consumer.DefaultMQPushConsumer#namesrvAddr
     */
    private String nameServer = "127.0.0.1:9876";

    /**
     * @see org.apache.rocketmq.client.consumer.DefaultMQPushConsumer#consumeThreadMin
     */
    private int consumeThreadMin = 20;

    /**
     * @see org.apache.rocketmq.client.consumer.DefaultMQPushConsumer#consumeThreadMax
     */
    private int consumeThreadMax = 64;

    private Producer producer;

    @Data
    public static class Producer {

        /**
         * name of producer
         */
        private String group;

        /**
         * millis of send message timeout
         */
        private int sendMsgTimeout = 3000;

        /**
         * Compress message body threshold, namely, message body larger than 4k will be compressed on default.
         */
        private int compressMsgBodyOverHowmuch = 1024 * 4;

        /**
         * <p> Maximum number of retry to perform internally before claiming sending failure in synchronous mode. </p>
         * This may potentially cause message duplication which is up to application developers to resolve.
         */
        private int retryTimesWhenSendFailed = 2;

        /**
         * <p> Maximum number of retry to perform internally before claiming sending failure in asynchronous mode. </p>
         * This may potentially cause message duplication which is up to application developers to resolve.
         */
        private int retryTimesWhenSendAsyncFailed = 2;

        /**
         * Indicate whether to retry another broker on sending failure internally.
         */
        private boolean retryAnotherBrokerWhenNotStoreOk = false;

        /**
         * Maximum allowed message size in bytes. // 4M
         */
        private int maxMessageSize = 1024 * 1024 * 4;

    }

}
