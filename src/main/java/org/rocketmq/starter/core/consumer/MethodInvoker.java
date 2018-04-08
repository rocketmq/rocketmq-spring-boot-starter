package org.rocketmq.starter.core.consumer;


import org.rocketmq.starter.exception.ConsumeException;
import org.rocketmq.starter.extension.InterceptorInitBean;
import org.rocketmq.starter.extension.InterceptorInitBeanAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

/**
 * 方法执行器，并实现InterceptorHookAware接口，通过set注入hook，实现方法执行前后的动态扩展
 * 该类注册到Spring容器进行管理
 *
 * @author He Jialin
 */
public class MethodInvoker implements InterceptorInitBeanAware {

    private static final Logger logger = LoggerFactory.getLogger(MethodInvoker.class);

    private InterceptorInitBean hook;

    /**
     * 对目标方法进行调用
     *
     * @param delegate 方法所在对象
     * @param method   对应方法
     * @param args     方法参数
     */
    void invoke(Object delegate, final Method method, Object... args) {
        try {
            if(!hook.preHandle(args)){
                return;
            }
        } catch (Exception e) {
            handleHookException(e);
        }

        Class<?>[] parmTypes = method.getParameterTypes();
        //检查方法中是否有MessageContext参数
        boolean hasContext = Arrays.stream(parmTypes).anyMatch(parmClazz -> parmClazz.equals(MessageContext.class));
        try {
            if (hasContext) {
                ReflectionUtils.invokeMethod(method, delegate, args);
            } else {
                ReflectionUtils.invokeMethod(method, delegate, args[0]);
            }
        }catch (Exception e){
            hook.nextHandle(false,args);
            throw new ConsumeException(e);
        }
        try {
            hook.nextHandle(true,args);
        } catch (Exception e) {
            handleHookException(e);
        }

    }

    /**
     * 对多个目标方法进行调用,调用策略为循环按顺序调用
     *
     * @param delegate 目标类
     * @param methods 目标方法
     * @param args 参数
     */
    void invoke(Object delegate, Collection<Method> methods, Object... args) {
        methods.forEach(method -> invoke(delegate, method, args));
    }



    @Override
    public void setHook(InterceptorInitBean hook) {
        this.hook = hook;
    }

    private void handleHookException(Exception e) {
        logger.error(e.getMessage(),e);
    }
}

