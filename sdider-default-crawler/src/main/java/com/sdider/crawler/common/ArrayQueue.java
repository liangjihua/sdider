package com.sdider.crawler.common;

import java.util.NoSuchElementException;

/**
 * 一个先进先出的有界队列。
 * 拥有以下特点：<br>
 * 1. 无锁，因此此类非线程安全。<br>
 * 2. 基于循环数组结构，所以数组中的对象无需重新排序。<br>
 * 3. 没有任何额外的中间节点生成。<br>
 * 4. 没有实现Collection接口，所以不支持迭代。<br>
 *
 * @param <T> 队列中对象的类型
 * @author yujiaxin
 */
public final class ArrayQueue<T> {
    /**
     * 用于保存对象的数组
     */
    private final Object[] items;

    /**
     * 指向当前头节点的下标
     */
    private int head;

    /**
     * 指向尾节点下一节点的下标
     */
    private int tail;

    public ArrayQueue(int maxSize) {
        items = new Object[maxSize];
        head = 0;
        tail = 0;
    }

    public void add(T item) {
        if (item == null) {
            throw new NullPointerException();
        }
        if (items[tail] != null) {
            throw new IllegalStateException("Queue Full");
        }
        items[tail] = item;
        tail++;
        if (tail == items.length) {
            tail = 0;
        }
    }

    public T pop() {
        @SuppressWarnings("unchecked")
        T item = (T) items[head];
        if (item == null) {
            throw new NoSuchElementException("Queue is empty");
        }
        items[head] = null;
        head++;
        if (head == items.length) {
            head = 0;
        }
        return item;
    }

    public int getSize() {
        if (head == tail) {
            if (items[head] == null) {
                return 0;
            } else {
                return items.length;
            }
        }
        if (tail > head) {
            return tail - head;
        }
        return items.length - (head - tail);
    }

    public boolean isEmpty() {
        return getSize() == 0;
    }
}
