package org.rocketmq.starter;


import org.rocketmq.starter.exception.ConsumeException;
import org.rocketmq.starter.core.consumer.RocketMQConsumerConfig;
import org.rocketmq.starter.core.consumer.MessageContext;

/**
 * @author He Jialin
 */
public interface RocketMQConsumerListener<E> {

    void onMessage(E message, MessageContext messageContext) throws ConsumeException;

    RocketMQConsumerConfig getConsumerConfig();

}
