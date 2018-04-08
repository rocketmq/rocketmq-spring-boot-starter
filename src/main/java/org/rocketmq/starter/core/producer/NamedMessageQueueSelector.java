package org.rocketmq.starter.core.producer;

import org.apache.rocketmq.client.producer.MessageQueueSelector;

import org.springframework.beans.factory.BeanNameAware;

/**
 *
 * @author He Jialin
 */
public interface NamedMessageQueueSelector extends MessageQueueSelector, BeanNameAware {

    String getBeanName();

}
