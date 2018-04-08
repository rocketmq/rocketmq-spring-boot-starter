package org.rocketmq.starter.core.consumer;


import org.rocketmq.starter.RocketMQConsumerListener;
import org.rocketmq.starter.annotation.RocketMQListener;
import org.rocketmq.starter.annotation.RocketMQMessage;
import org.rocketmq.starter.exception.ConsumeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author He Jialin
 */
public final class RocketMQListenerMethodAdapter<E> implements RocketMQConsumerListener<E> {

    private static final Logger logger = LoggerFactory.getLogger(RocketMQListenerMethodAdapter.class);

    private final SubscriptionGroup subscriptionGroup;

    private ConsumerConfigRocket consumerConfig;

    private MethodInvoker invoker;

    RocketMQListenerMethodAdapter(SubscriptionGroup subscriptionGroup) {
        this.subscriptionGroup = subscriptionGroup;
        initConfig(subscriptionGroup);
    }

    private void initConfig(SubscriptionGroup subscriptionGroup) {
        RocketMQListener rocketMQListener = subscriptionGroup.getTarget().getClass().getAnnotation(RocketMQListener.class);
        consumerConfig = ConsumerConfigRocket.builder()
                .consumerGroup(rocketMQListener.consumerGroup())
                .messageModel(rocketMQListener.messageModel())
                .orderlyMessage(rocketMQListener.orderly())
                .topic(rocketMQListener.topic())
                .consumeThreadMax(rocketMQListener.consumeThreadMax())
                .consumeThreadMin(rocketMQListener.consumeThreadMin())
                .build();
        Map<String, Class<?>> tags = new HashMap<>();
        subscriptionGroup.getTagList().forEach(tag -> {
            RocketMQMessage rocketMQMessage = subscriptionGroup.getMethod(tag).getAnnotation(RocketMQMessage.class);
            tags.put(tag, rocketMQMessage.messageClass());
            consumerConfig.setMessageClass(rocketMQMessage.messageClass());
        });
        consumerConfig.setTags(tags);
    }


    @Override
    public void onMessage(E message, MessageContext context) throws ConsumeException {
        if (logger.isDebugEnabled()) {
            logger.debug("received message:{}", message);
        }
        String tag = context.getMessageExt().getTags();
        Method method = this.subscriptionGroup.getMethod(tag);
        Object delegate = this.subscriptionGroup.getTarget();
        if (method != null) {
            try {
                invoker.invoke(delegate, method, message, context);
            } catch (Exception e) {
                throw new ConsumeException(e);
            }
        } else {
            if (("*").equals(tag.trim())) {
                invoker.invoke(delegate, this.subscriptionGroup.getAllMethods(), message, context);
            } else {
                throw new ConsumeException("No way to find the corresponding tag");
            }
        }


    }

    @Override
    public ConsumerConfigRocket getConsumerConfig() {
        return this.consumerConfig;
    }


    public void setInvoker(MethodInvoker invoker) {
        this.invoker = invoker;
    }
}
