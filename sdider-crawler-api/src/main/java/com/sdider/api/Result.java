package com.sdider.api;

import java.util.List;

/**
 * Results是一个抽象数据结构，用于收集解析{@link Response}所产生
 * 的{@link Item}s，和{@link Request}s。
 * @see ResponseParser
 * @see Item
 * @author yujiaxin
 */
public interface Result extends RequestContainer {

    /**
     * 将此Result中包含的{@link Item}替换为给定的{@link Item}列表。
     * 给定的列表后续发生变化，不会反映到此Results中。
     * @param items {@link Item}列表
     */
    void setItems(List<Item> items);

    /**
     * 添加一个{@link Item}到此Result中
     * @param item item
     */
    void addItem(Item item);

    /**
     * 使用给定的pipelines消费添加到此Results中的所有{@link Item}。
     * @param pipelines pipelines
     */
    void consume(List<Pipeline> pipelines);
}
