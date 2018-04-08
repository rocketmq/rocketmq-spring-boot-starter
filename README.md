# spring-boot-starter-rocketmq

[![License](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)


## Quick Start

```xml
<!--add dependency in pom.xml-->
<dependency>
    <groupId>org.rocketmq</groupId>
    <artifactId>spring-boot-starter-rocketmq</artifactId>
    <version>1.1.0.RELEASE</version>
</dependency>
```

### Consume Message


```properties
## application.properties
spring.rocketmq.name-server=127.0.0.1:9876
```

> More relevant configurations for produce:
>
> ```properties
> spring.rocketmq.producer.retry-times-when-send-async-failed=0
> spring.rocketmq.producer.send-msg-timeout=300000
> spring.rocketmq.producer.compress-msg-body-over-howmuch=4096
> spring.rocketmq.producer.max-message-size=4194304
> spring.rocketmq.producer.retry-another-broker-when-not-store-ok=false
> spring.rocketmq.producer.retry-times-when-send-failed=2


> Note:

> Maybe you need change 127.0.0.1:9876 with your real NameServer address for RocketMQ

```java
@SpringBootApplication
@EnableRocketMQ
public class RocketMQApplication {
    public static void main(String[] args) {
        SpringApplication.run(RocketMQApplication .class,args);
    }
}


@Slf4j
@Service
@RocketMQListener(topic = "topic-1")
public class MyConsumer1 {
     @RocketMQMessage(messageClass = String.class,tag = "tag-1")
     public void onMessage(String message) {
         log.info("received message: {}", message);
     }
}
    
```

