package com.sdider.api;

import java.util.List;

/**
 * 管理一组{@link Item}s
 * @author yujiaxin
 */
public interface ItemContainer {
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
     * 移除一个已添加的{@link Item}
     * @param item item
     */
    void removeItem(Item item);

    List<Item> getItems();
}
