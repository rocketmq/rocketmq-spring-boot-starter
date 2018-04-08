package org.rocketmq.starter.configuration;


import org.apache.rocketmq.client.impl.MQClientAPIImpl;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.rocketmq.starter.core.consumer.RocketMQMessageListenerContainer;
import org.rocketmq.starter.core.consumer.SimpleListenerFactory;
import org.rocketmq.starter.core.producer.RocketMQProducerContainer;
import org.rocketmq.starter.extension.core.InitBeanFactory;
import org.rocketmq.starter.core.consumer.MethodInvoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;

/**
 * 自动化配置类
 *
 * @author He Jialin
 */
@Configuration
@EnableConfigurationProperties(RocketMQProperties.class)
@ConditionalOnClass(MQClientAPIImpl.class)
@Order
public class RocketMQAutoConfiguration {

    @Autowired
    private RocketMQProperties rocketMQProperties;

    @Bean
    @ConditionalOnClass(DefaultMQProducer.class)
    @ConditionalOnMissingBean(DefaultMQProducer.class)
    @ConditionalOnProperty(prefix = "spring.rocketmq", value = {"nameServer", "producer.group"})
    public DefaultMQProducer mqProducer(RocketMQProperties rocketMQProperties) {

        RocketMQProperties.Producer producerConfig = rocketMQProperties.getProducer();
        String groupName = producerConfig.getGroup();
        Assert.hasText(groupName, "[spring.rocketmq.producer.group] must not be null");

        DefaultMQProducer producer = new DefaultMQProducer(producerConfig.getGroup());
        producer.setNamesrvAddr(rocketMQProperties.getNameServer());
        producer.setSendMsgTimeout(producerConfig.getSendMsgTimeout());
        producer.setRetryTimesWhenSendFailed(producerConfig.getRetryTimesWhenSendFailed());
        producer.setRetryTimesWhenSendAsyncFailed(producerConfig.getRetryTimesWhenSendAsyncFailed());
        producer.setMaxMessageSize(producerConfig.getMaxMessageSize());
        producer.setCompressMsgBodyOverHowmuch(producerConfig.getCompressMsgBodyOverHowmuch());
        producer.setRetryAnotherBrokerWhenNotStoreOK(producerConfig.isRetryAnotherBrokerWhenNotStoreOk());

        return producer;
    }

    @Bean
    @ConditionalOnMissingBean(SimpleListenerFactory.class)
    public InitBeanFactory initBeanFactory() {
        return new InitBeanFactory();
    }

    @Bean
    @ConditionalOnMissingBean(MethodInvoker.class)
    public MethodInvoker methodInvoker() {
        return new MethodInvoker();
    }

    @Bean
    @ConditionalOnMissingBean(RocketMQProducerContainer.class)
    public RocketMQProducerContainer mqProducerContainer() {
        return new RocketMQProducerContainer();
    }

    @Bean
    @ConditionalOnMissingBean(RocketMQMessageListenerContainer.class)
    public RocketMQMessageListenerContainer mqMessageListenerContainer() {
        return new RocketMQMessageListenerContainer(rocketMQProperties.getNameServer());
    }



}
