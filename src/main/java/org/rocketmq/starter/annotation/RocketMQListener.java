package org.rocketmq.starter.annotation;


import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.stereotype.Component;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DefaultMQPushConsumer
 *
 * @author He Jialin
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RocketMQListener {

    /**
     * 是否为顺序消息
     * @return true：sequence message
     * false：non-sequential messages
     */
    boolean orderly() default false;

    /**
     * 转换为DefaultMqPushConsumer后订阅的topic
     * @return DEFAULT_TOPIC
     */
    String topic() default "DEFAULT_TOPIC";

    /**
     * 消息模式，默认为集群模式
     * @see MessageModel
     * @return MessageModel.CLUSTERING
     */
    MessageModel messageModel() default MessageModel.CLUSTERING;

    /**
     * 消费者组
     * @return DEFAULT_GROUP
     */
    String consumerGroup() default "DEFAULT_GROUP";

    /**
     * 此消费者在消费时的最大线程数，如果在此处设置则使用此处设置的值
     * 否则统一使用配置文件中的值
     * @return 0
     */
    int consumeThreadMax() default 0;

    /**
     * 此消费者在消费时的最小线程数，如果在此处设置则使用此处设置的值
     * 否则统一使用配置文件中的值
     * @return 0
     */
    int consumeThreadMin() default 0;

}
