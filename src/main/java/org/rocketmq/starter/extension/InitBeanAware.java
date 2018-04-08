package org.rocketmq.starter.extension;

import org.springframework.beans.factory.Aware;

/**
 * 在类进行实例化后对泛型所对应的钩子进行set注入
 *
 * @author He Jialin
 */
public interface InitBeanAware<K extends InitBean> extends Aware {

    /**
     * 设置bean的钩子
     * @param initBean
     */
    void setHook(K initBean);

}
