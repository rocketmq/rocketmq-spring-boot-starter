package org.rocketmq.starter.core.consumer;


import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;

import lombok.Data;
import lombok.ToString;

/**
 * 消费时，当前所消费的消息的上下文信息
 *
 * @author He Jialin
 */
@ToString
@Data
public final class MessageContext {

    /**
     * 所消费消息所在的消息队列
     *
     * @see MessageQueue
     */
    private MessageQueue messageQueue;

    /**
     * 所消费的消息的扩展属性
     *
     * @see MessageExt
     */
    private MessageExt messageExt;


}
