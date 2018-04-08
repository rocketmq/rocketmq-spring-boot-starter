package org.rocketmq.starter.core;


import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;

import org.rocketmq.starter.core.producer.MessageProxy;


/**
 * @author He Jialin
 */
public interface RocketMQProducer<M> {

    void send(MessageProxy<M> messageProxy) throws MQClientException, InterruptedException, RemotingException;

    void start() throws MQClientException;

    void shutdown();

}
