package org.rocketmq.starter.core.consumer;


import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;

import org.rocketmq.starter.RocketMQConsumerListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用来生成DefaultMqPushConsumer
 *
 * @author He Jialin
 */
public class RocketMQPushConsumerFactory implements InitializingBean, ApplicationContextAware {

    private String nameSrvAddr;

    private SimpleListenerFactory listenerFactory;

    private ApplicationContext applicationContext;

    private Map<String, DefaultMQPushConsumer> pushConsumerMap;

    private List<DefaultMQPushConsumer> pushConsumers;

    private int consumeThreadMin;

    private int consumeThreadMax;


    RocketMQPushConsumerFactory(String nameSrvAddr) {
        this.nameSrvAddr = nameSrvAddr;
    }

    public Map<String, DefaultMQPushConsumer> getPushConsumerMap() {
        return pushConsumerMap;
    }


    private DefaultMQPushConsumer createDefaultMQPushConsumer(RocketMQConsumerListener rocketMQConsumerListener) {
        ConsumerConfigRocket config = rocketMQConsumerListener.getConsumerConfig();
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setNamesrvAddr(nameSrvAddr);
        defaultMQPushConsumer.setConsumerGroup(config.getConsumerGroup());
        Map<String, Class<?>> tags = config.getTags();
        StringBuilder tagBuilder = new StringBuilder();
        List<String> tmpTags = new ArrayList<>(tags.keySet());
        for (int i = 0; i < tmpTags.size(); i++) {
            if (tmpTags.contains("*") && tmpTags.size() > 1) {
                throw new IllegalArgumentException("订阅的tag不合法");
            }
            tagBuilder.append(tmpTags.get(i));
            if (tmpTags.size() > i + 1) {
                tagBuilder.append("||");
            }
        }
        try {
            defaultMQPushConsumer.subscribe(config.getTopic(), tagBuilder.toString());
            defaultMQPushConsumer.subscribe(config.getTopic(), "*");
        } catch (MQClientException e) {
            throw new IllegalArgumentException("订阅语法错误", e);
        }
        defaultMQPushConsumer.setMessageModel(config.getMessageModel());
        if (config.getConsumeThreadMax() == 0) {
            defaultMQPushConsumer.setConsumeThreadMax(this.consumeThreadMax);
        } else {
            defaultMQPushConsumer.setConsumeThreadMax(config.getConsumeThreadMax());
        }
        if (config.getConsumeThreadMin() == 0) {
            defaultMQPushConsumer.setConsumeThreadMin(this.consumeThreadMin);
        } else {
            defaultMQPushConsumer.setConsumeThreadMin(config.getConsumeThreadMin());
        }
        return defaultMQPushConsumer;
    }

    public List<DefaultMQPushConsumer> getAllMQPushConsumer() {
        return pushConsumers;
    }


    public SimpleListenerFactory getListenerFactory() {
        return listenerFactory;
    }


    @Override
    public void afterPropertiesSet() {
        pushConsumers = new ArrayList<>();
        pushConsumerMap = new HashMap<>(16);
        if (listenerFactory == null) {
            listenerFactory = new SimpleListenerFactory();
            listenerFactory.setApplicationContext(this.applicationContext);
            listenerFactory.afterPropertiesSet();
        }
        listenerFactory.getAllListeners().forEach((topic, consumerListener) -> {
            DefaultMQPushConsumer pushConsumer = createDefaultMQPushConsumer(consumerListener);
            pushConsumers.add(pushConsumer);
            pushConsumerMap.put(topic, pushConsumer);
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    public void setConsumeThreadMin(int consumeThreadMin) {
        this.consumeThreadMin = consumeThreadMin;
    }


    public void setConsumeThreadMax(int consumeThreadMax) {
        this.consumeThreadMax = consumeThreadMax;
    }
}
