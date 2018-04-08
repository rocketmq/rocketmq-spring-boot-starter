package org.rocketmq.starter.core.consumer;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import org.rocketmq.starter.RocketMQConsumerListener;

import java.util.List;

/**
 * 并发消费监听默认实现
 *
 * @author He Jialin
 */
public final class MessageListenerConcurrentlyImpl implements MessageListenerConcurrently {

    private final RocketMQConsumerListener listener;

    MessageListenerConcurrentlyImpl(RocketMQConsumerListener listener) {
        this.listener = listener;
    }

    /**
     * 消费消息
     *
     * @param msgs    msgs.size() &gt;= 1
     *                DefaultMQPushConsumer.consumeMessageBatchMaxSize=1，you can modify here
     *                这里只设置为1，当设置为多个时，msgs中只要有一条消息消费失败，就会整体重试
     * @param context 上下文信息
     * @return 消费状态  成功（CONSUME_SUCCESS）或者 重试 (RECONSUME_LATER)
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        ConsumeStatus status = MessageHandler.handleMessage(listener, msgs, context.getMessageQueue());
        if (status.equals(ConsumeStatus.SUCCESS)) {
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } else {
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }

}
