package org.rocketmq.starter.core.consumer;


import org.rocketmq.starter.RocketMQConsumerListener;
import org.rocketmq.starter.annotation.RocketMQListener;
import org.rocketmq.starter.annotation.RocketMQMessage;
import org.rocketmq.starter.exception.MethodNotSupportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author He Jialin
 */
public class SimpleListenerFactory implements InitializingBean, ApplicationContextAware {


    private static final Logger logger = LoggerFactory.getLogger(SimpleListenerFactory.class);

    private Map<String, RocketMQConsumerListener> allListeners;

    private MethodResolver resolver;

    private ApplicationContext context;

    SimpleListenerFactory() {
        this.resolver = new MethodResolver();
    }

    private RocketMQConsumerListener createRocketMqConsumerListener(SubscriptionGroup subscriptionGroup) {
        RocketMQListenerMethodAdapter adapter = new RocketMQListenerMethodAdapter(subscriptionGroup);
        adapter.setInvoker(context.getBean(MethodInvoker.class));
        return adapter;
    }

    public Map<String, RocketMQConsumerListener> getAllListeners() {
        return allListeners;
    }

    @Override
    public void afterPropertiesSet() {
        allListeners = new HashMap<>();
        Map<String, SubscriptionGroup> subscriptionGroups = this.resolver.getSubscriptionGroups();
        subscriptionGroups.forEach((topic, subscriptionGroup) ->
                allListeners.put(topic, createRocketMqConsumerListener(subscriptionGroup)));

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
        this.resolver.setApplicationContext(applicationContext);
    }


    private class MethodResolver implements ApplicationContextAware {

        private ApplicationContext context;

        private Map<String, SubscriptionGroup> subscriptionGroups = new HashMap<>();

        private boolean initSubscription = false;


        Map<String, SubscriptionGroup> getSubscriptionGroups() {
            if (!initSubscription) {
                resolveListenerMethod();
            }
            return subscriptionGroups;
        }

        void resolveListenerMethod() {
            context.getBeansWithAnnotation(RocketMQListener.class).forEach((beanName, obj) -> {
                Map<Method, RocketMQMessage> annotatedMethods = MethodIntrospector.selectMethods(obj.getClass(),
                        (MethodIntrospector.MetadataLookup<RocketMQMessage>) method -> AnnotatedElementUtils
                                .findMergedAnnotation(method, RocketMQMessage.class));
                initSubscriptionGroup(annotatedMethods, obj);
            });
            this.initSubscription = true;
        }

        private void initSubscriptionGroup(Map<Method, RocketMQMessage> annotatedMethod, Object target) {
            if (!CollectionUtils.isEmpty(annotatedMethod)) {
                annotatedMethod.forEach((method, listener) -> {
                    validateMethod(method);
                    RocketMQListener rocketMQListener = method.getDeclaringClass().getAnnotation(RocketMQListener.class);
                    String topic = rocketMQListener.topic();
                    String tag = listener.tag();
                    if (subscriptionGroups.containsKey(topic)) {
                        subscriptionGroups.get(topic).putTagToGroup(tag, method);
                    } else {
                        SubscriptionGroup subscriptionGroup = new SubscriptionGroup(topic);
                        subscriptionGroup.putTagToGroup(tag, method);
                        subscriptionGroup.setTarget(target);
                        subscriptionGroups.put(topic, subscriptionGroup);
                    }

                });
            }

        }

        private void validateMethod(Method method) {
            if (method.getParameterCount() > 2) {
                throw new MethodNotSupportException("method: " + method + " 参数列表不被支持");
            }
            boolean typeSupport = Arrays.stream(method.getParameterTypes()).allMatch(parmType -> parmType.equals(method
                    .getAnnotation
                            (RocketMQMessage.class).messageClass()) || parmType.equals(MessageContext.class));
            if (!typeSupport) {
                throw new MethodNotSupportException("方法参数中含有不被支持的类型");
            }
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.context = applicationContext;
        }
    }

}
