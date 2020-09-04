package com.sdider;

import com.sdider.api.Result;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;

/**
 * SdiderResult是{@link SdiderResponse}被解析后产生的结果，包含{@link SdiderRequest}和{@link SdiderItem}
 *
 * @author yujiaxin
 */
public interface SdiderResult extends Result, SdiderRequestContainer {

    /**
     * 添加一个{@link SdiderItem}到结果中，给定的闭包用于配置该{@link SdiderItem}<br/>
     * @param itemConfig 用于设置{@link SdiderItem}的闭包
     */
    void item(@DelegatesTo(SdiderItem.class) Closure<?> itemConfig);

}
