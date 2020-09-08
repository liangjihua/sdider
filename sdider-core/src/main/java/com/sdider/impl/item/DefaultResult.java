package com.sdider.impl.item;

import com.sdider.api.Item;
import com.sdider.api.Result;
import com.sdider.impl.request.AbstractRequestContainer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author yujiaxin
 */
public class DefaultResult extends AbstractRequestContainer implements Result {
    private List<Item> items = new LinkedList<>();

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
    public void removeItem(Item item) {
        items.remove(item);
    }

    @Override
    public List<Item> getItems() {
        return new ArrayList<>(items);
    }
}
