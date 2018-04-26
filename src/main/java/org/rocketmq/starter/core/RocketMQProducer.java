package org.rocketmq.starter.core;


import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import org.rocketmq.starter.core.producer.MessageProxy;


/**
 * @author He Jialin
 */
public interface RocketMQProducer<M> {

    SendResult send(Message message) throws InterruptedException, RemotingException, MQClientException, MQBrokerException;

    void send(MessageProxy<M> messageProxy) throws MQClientException, InterruptedException, RemotingException;

    void start() throws MQClientException;

    void shutdown();

}
