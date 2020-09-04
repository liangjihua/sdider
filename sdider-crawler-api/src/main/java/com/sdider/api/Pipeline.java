package com.sdider.api;

import com.sdider.api.common.NamedObject;

/**
 * Pipeline用于消费{@link Item}
 * @author yujiaxin
 */
public interface Pipeline extends NamedObject {

    void process(Item item);
}
