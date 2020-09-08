package com.sdider;

import com.sdider.api.Item;
import com.sdider.api.ItemContainer;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;

/**
 * SdiderItemContainer扩展{@link ItemContainer}，增加了
 * 用于快速创建并添加{@link Item}的接口。<br/>
 * @author yujiaxin
 */
public interface SdiderItemContainer extends ItemContainer {

    /**
     * 添加一个{@link Item}到结果中，给定的闭包用于配置该{@link Item}<br/>
     * @param itemConfig 用于设置{@link Item}的闭包
     */
    void item(@DelegatesTo(Item.class) Closure<?> itemConfig);
}
