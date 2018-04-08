package org.rocketmq.starter.core.producer;


import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.common.message.Message;

import java.io.Serializable;

/**
 * @author He Jialin
 */
public class MessageProxy<M> implements Serializable {
    private static final long serialVersionUID = -3470788148313235550L;

    private MessageQueueSelector messageQueueSelector;

    private SendCallback sendCallback;

    private Message message;

    private Object selectorArg;

    public Object getSelectorArg() {
        return selectorArg;
    }

    public void setSelectorArg(Object selectorArg) {
        this.selectorArg = selectorArg;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public MessageQueueSelector getMessageQueueSelector() {
        return messageQueueSelector;
    }

    public void setMessageQueueSelector(MessageQueueSelector messageQueueSelector) {
        this.messageQueueSelector = messageQueueSelector;
    }

    public SendCallback getSendCallback() {
        return sendCallback;
    }

    public void setSendCallback(SendCallback sendCallback) {
        this.sendCallback = sendCallback;
    }
}
