package org.rocketmq.starter.core.producer;


import org.rocketmq.starter.core.RocketMQConfig;

/**
 * @author He Jialin
 */
public class RocketMQProducerConfig extends RocketMQConfig {

    private String producerGroup;

    private int timeOut = 3000;

    public String getProducerGroup() {
        return producerGroup;
    }

    public void setProducerGroup(String producerGroup) {
        this.producerGroup = producerGroup;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

}
