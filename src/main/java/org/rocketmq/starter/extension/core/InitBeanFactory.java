package org.rocketmq.starter.extension.core;


import org.rocketmq.starter.extension.InterceptorInitBeanAware;
import org.rocketmq.starter.extension.InitBeanAware;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Arrays;
import java.util.Map;

/**
 * @author He Jialin
 */
public class InitBeanFactory implements InitializingBean, ApplicationContextAware {

    private ApplicationContext context;

    private void awareHook() {
        InterceptorInitBeanSupport support = new InterceptorInitBeanSupport();
        support.setApplicationContext(context);
        support.afterPropertiesSet();
        Map<String, InitBeanAware> awareMap = context.getBeansOfType(InitBeanAware.class);
        for (Map.Entry<String, InitBeanAware> awareEntry : awareMap.entrySet()) {
            InitBeanAware aware = awareEntry.getValue();
            if (Arrays.asList(aware.getClass().getInterfaces()).contains(InterceptorInitBeanAware.class)) {
                ((InterceptorInitBeanAware) aware).setHook(support);
            }
        }

    }

    @Override
    public void afterPropertiesSet() {
        awareHook();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

}
