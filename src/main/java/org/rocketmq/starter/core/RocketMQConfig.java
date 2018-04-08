package org.rocketmq.starter.core;

/**
 * @author He Jialin
 */
public class RocketMQConfig {
    private Class<?> messageClass;
    private boolean orderlyMessage;

    public Class<?> getMessageClass() {
        return messageClass;
    }

    public void setMessageClass(Class<?> messageClass) {
        this.messageClass = messageClass;
    }

    public boolean isOrderlyMessage() {
        return orderlyMessage;
    }

    public void setOrderlyMessage(boolean orderlyMessage) {
        this.orderlyMessage = orderlyMessage;
    }


}
