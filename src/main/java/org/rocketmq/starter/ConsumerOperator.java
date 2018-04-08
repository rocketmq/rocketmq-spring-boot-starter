package org.rocketmq.starter;


import org.rocketmq.starter.core.consumer.OperationResult;

/**
 *
 * 消费者容器操作接口
 *
 * @author He Jialin
 */
public interface ConsumerOperator {


    /**
     * 根据topic暂停某个消费者的消费
     * @param topic 消费者的topic
     * @return 操作结果
     */
    OperationResult suspendConsumer(String topic);

    /**
     * 根据topic恢复某个消费者的消费
     * @param topic 消费者topic
     * @return 操作结果
     */
    OperationResult resumeConsumer(String topic);
}
