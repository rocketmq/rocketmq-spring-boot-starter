package org.rocketmq.starter.core.producer;


import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.rocketmq.starter.core.RocketMQProducer;
import org.rocketmq.starter.exception.ContatinerInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author He Jialin
 */
public class RocketMQProducerTemplate<M> extends RocketMQProducerConfig implements RocketMQProducer<M> {

    private static final Logger logger = LoggerFactory.getLogger(RocketMQProducerTemplate.class);

    private String namesrvAddr = System.getProperty("spring.rocketmq.namesrv.addr", System.getenv("NAMESRV_ADDR"));

    private DefaultMQProducer defaultMQProducer;

    private AtomicBoolean started = new AtomicBoolean(false);


    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    @Override
    public SendResult send(Message message) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        return this.defaultMQProducer.send(message);
    }

    @Override
    public void start() throws MQClientException {
        if (started.get()) {
            throw new ContatinerInitException("this templeate is already init");
        }
        if (this.defaultMQProducer == null) {
            this.defaultMQProducer = new DefaultMQProducer();
        }
        this.defaultMQProducer.setProducerGroup(this.getProducerGroup());
        this.defaultMQProducer.setSendMsgTimeout(this.getTimeOut());
        this.defaultMQProducer.setNamesrvAddr(this.namesrvAddr);
        this.defaultMQProducer.start();
        this.started.set(true);
    }

    @Override
    public void shutdown() {
        if (started.get()) {
            this.defaultMQProducer.shutdown();
            started.set(false);
        }
    }


    @Override
    public void send(MessageProxy messageProxy) throws MQClientException, InterruptedException, RemotingException {
        SendCallback sendCallback = messageProxy.getSendCallback() == null ? new DefaultSendCallback() : messageProxy
                .getSendCallback();
        if (messageProxy.getMessage() == null) {
            throw new NullPointerException("the message is null");
        }
        if (this.isOrderlyMessage()) {
            MessageQueueSelector selector = messageProxy.getMessageQueueSelector();
            if (selector == null) {
                throw new NullPointerException("the sequential message must be configured with MessageQueueSelector.");
            }
            this.defaultMQProducer.send(messageProxy.getMessage(), selector, messageProxy.getSelectorArg(),
                    sendCallback);
        } else {
            this.defaultMQProducer.send(messageProxy.getMessage(), sendCallback);
        }
    }

    private static class DefaultSendCallback implements SendCallback {

        @Override
        public void onSuccess(SendResult sendResult) {

        }

        @Override
        public void onException(Throwable e) {

        }
    }

}
