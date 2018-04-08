package org.rocketmq.starter.core.producer;


import org.apache.rocketmq.client.exception.MQClientException;

import org.rocketmq.starter.exception.ContatinerInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;

import java.util.Map;

/**
 * @author He Jialin
 */

public class RocketMQProducerContainer implements InitializingBean, ApplicationContextAware, SmartLifecycle {

    private static final Logger logger = LoggerFactory.getLogger(RocketMQProducerContainer.class);

    private final Object monitor = new Object();

    private volatile boolean running = false;

    private volatile boolean initialized = false;

    private ApplicationContext applicationContext;

    @Override
    public void start() {
        if (initialized) {
            Map<String, RocketMQProducerTemplate> templateMap = applicationContext.getBeansOfType
                    (RocketMQProducerTemplate.class);
            templateMap.forEach((beanName, template) -> {
                try {
                    template.start();
                } catch (ContatinerInitException e) {
                    logger.error("bean {} is already start", beanName, e);
                } catch (MQClientException e) {
                    e.printStackTrace();
                }
            });
        }
        this.running = true;
    }

    @Override
    public void stop() {
        Map<String, RocketMQProducerTemplate> templateMap = applicationContext.getBeansOfType
                (RocketMQProducerTemplate.class);
        templateMap.forEach((beanName, template) -> template.shutdown());
        this.running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        this.initialized = true;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }
}
