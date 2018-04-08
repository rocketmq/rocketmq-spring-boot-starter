package org.rocketmq.starter.extension;


/**
 * 方法拦截钩子，在方法执行前后进行拦截
 *
 * @author He Jialin
 */
public interface InterceptorInitBean extends InitBean {

    /**
     * 方法执行前
     *
     * @param args 方法执行的参数
     * @return true: 正常执行
     *         false： 阻止方法继续执行
     */
    boolean preHandle(Object... args);

    /**
     * 方法执行后
     * @param methodSuccess : 方法是否执行成功
     * @param args 方法执行的参数
     *
     */
    void nextHandle(boolean methodSuccess, Object... args);

}
