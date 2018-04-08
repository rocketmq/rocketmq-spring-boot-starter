package org.rocketmq.starter.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 改注解运用在消费端的方法上，用来处理同一topic中不同的tag类型的消息
 *
 * @author He Jialin
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RocketMQMessage {

    /**
     * 订阅的tag
     * @return "*"
     */
    String tag() default "*";

    /**
     * 请求方消息类型
     * @return Object.class
     */
    Class<?> messageClass() default Object.class;
}
