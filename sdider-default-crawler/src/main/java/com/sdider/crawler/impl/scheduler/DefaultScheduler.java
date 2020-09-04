package com.sdider.crawler.impl.scheduler;

import com.sdider.api.Request;
import com.sdider.crawler.Scheduler;

import java.util.Queue;

/**
 * 默认的Scheduler实现，使用给定的{@link Queue}存储和调度{@link Request}
 * @author yujiaxin
 */
public class DefaultScheduler implements Scheduler {
    private final Queue<Request> queue;

    public DefaultScheduler(Queue<Request> workQueue) {
        this.queue = workQueue;
    }

    @Override
    public void push(Request request) {
        queue.add(request);
    }

    @Override
    public Request pop() {
        return queue.poll();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
