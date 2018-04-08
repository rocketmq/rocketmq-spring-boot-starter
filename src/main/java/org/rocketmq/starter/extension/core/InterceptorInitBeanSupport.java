package org.rocketmq.starter.extension.core;

import org.rocketmq.starter.extension.InterceptorInitBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * 方法拦截钩子的默认实现
 * 可通过继承此类，对插件和方法执行等细节进行覆盖和扩展
 * 继承的此类的类需要将类的实例交由Spring容器进行管理，并且配置为单例的
 *
 * @author He Jialin
 */
public class InterceptorInitBeanSupport implements InterceptorInitBean, ApplicationContextAware, InitializingBean {

    /**
     * 应用程序上下文，通过Spring自动注入
     */
    private ApplicationContext applicationContext;

    /**
     * 当前钩子挂载的所有插件
     */
    protected Map<String, InterceptorPlugin> plugins;

    /**
     * 方法执行前的插件调用，默认为循环调用
     *
     * @param args 方法执行的参数
     */
    @Override
    public boolean preHandle(Object... args) {
        for (InterceptorPlugin plugin : plugins.values()) {
            if (!plugin.preHandle(args)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 方法执行后的插件调用，默认为循环调用
     *
     * @param args 方法执行的参数
     */
    @Override
    public void nextHandle(boolean methodSuccess,Object... args) {
        plugins.values().forEach(plugin -> plugin.nextHandle(methodSuccess,args));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 加载插件实例
     *
     * @param clazz 插件类型
     * @return 插件Map, key为bean名称, value为插件实例对象
     */
    protected Map<String, InterceptorPlugin> getPlugins(Class<InterceptorPlugin> clazz) {
        return applicationContext.getBeansOfType(clazz);
    }

    @Override
    public void afterPropertiesSet() {
        plugins = getPlugins(InterceptorPlugin.class);
    }


    /**
     *
     * @see InterceptorInitBean
     *
     * 插件接口，实现类需要交由Spring容器管理
     */
    public interface InterceptorPlugin {

        boolean preHandle(Object... args);

        void nextHandle(boolean methodSuccess, Object... args);

    }

}
