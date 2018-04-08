package org.rocketmq.starter.annotation;

import org.rocketmq.starter.configuration.RocketMQAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to enable a RocketMQ implementation.
 *
 * @author He Jialin
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RocketMQAutoConfiguration.class)
public @interface EnableRocketMQ {
}
