package com.sdider.crawler;

import com.sdider.api.Request;

/**
 * @author yujiaxin
 */
public interface Scheduler {

    void push(Request request);

    /**
     * 此Scheduler中返回并移除一个{@link Request}，
     * 若此Scheduler内没有任何元素，返回null
     * @return request or null
     */
    Request pop();

    boolean isEmpty();
}
