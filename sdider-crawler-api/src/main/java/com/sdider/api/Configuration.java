package com.sdider.api;

import com.sdider.api.common.DynamicPropertiesObject;

/**
 * Configuration管理一组以键值对的形式的配置属性，<br/>
 * Configuration继承了{@link DynamicPropertiesObject}
 *
 * @author yujiaxin
 */
public interface Configuration extends DynamicPropertiesObject<Object> {

    /**
     * 使用已包含的配置属性配置/填充给定对象。
     * @param config 待配置对象
     * @param <T> 待配置对象类型泛型
     */
    <T> void configure(T config);

    /**
     * 使用指定配置属性的值配置/填充给定对象。
     * @param name 配置属性名
     * @param config 待配置对象
     * @param <T> 待配置对象类型泛型
     */
    <T> void configure(String name, T config);
}
