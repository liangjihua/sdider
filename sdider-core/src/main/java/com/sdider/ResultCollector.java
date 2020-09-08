package com.sdider;

import com.sdider.api.Result;
import groovy.lang.Closure;

/**
 * ResultCollector收集{@link com.sdider.api.Item}和{@link com.sdider.api.Request}
 * 并产生结果。
 * @author yujiaxin
 */
public interface ResultCollector {

    void items(Closure<?> itemConfig);

    void targets(Closure<?> requestConfig);

    Result getResult();
}
