package com.sdider.impl.item;

import com.sdider.SdiderItem;
import com.sdider.SdiderItemContainer;
import com.sdider.api.Item;
import com.sdider.api.Request;
import com.sdider.utils.ClosureUtils;
import groovy.lang.Closure;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author yujiaxin
 */
public class DefaultItemContainer implements SdiderItemContainer {
    private List<Item> items = new LinkedList<>();
    private final Request request;

    public DefaultItemContainer(Request request) {
        this.request = request;
    }

    @Override
    public void item(Closure<?> itemConfig) {
        SdiderItem item = new DefaultItem(request);
        ClosureUtils.delegateRun(item, itemConfig);
        addItem(item);
    }

    @Override
    public void setItems(List<Item> items) {
        if (items == null) {
            this.items = new LinkedList<>();
            return;
        }
        this.items = new LinkedList<>(items);
    }

    @Override
    public void addItem(Item item) {
        if (item != null) {
            items.add(item);
        }
    }

    @Override
    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    @Override
    public void removeItem(Item item) {
        if (item != null) {
            items.remove(item);
        }
    }
}
