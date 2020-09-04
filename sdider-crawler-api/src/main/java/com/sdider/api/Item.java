package com.sdider.api;

import com.sdider.api.common.DynamicPropertiesObject;

import java.util.List;

/**
 * Item用于收集和传递从{@link Response}中解析出来的数据。
 * Item是一个{@link DynamicPropertiesObject}
 * @see Response
 * @see ResponseParser
 * @author yujiaxin
 */
public interface Item extends DynamicPropertiesObject<Object> {

    /**
     * 返回与此Item相关联的{@link Request}，即产生此Item
     * 的原请求，永远不会返回null
     *
     * @return 产生此Item的 {@link Request}
     */
    Request getRequest();

    /**
     * 返回一个列表，包含追加在此Item上的所有{@link PipelineFilter}，
     * 返回列表是安全的，对其所作的操作不会影响到此Item。永远不会返回null。
     *
     * @return {@link PipelineFilter}列表
     */
    List<PipelineFilter> getPipelineFilters();

    /**
     * 在此Item上追加一个用于过滤的{@link PipelineFilter}
     *
     * @param pipelineFilter 要追加的PipelineFilter
     */
    void addPipelineFilter(PipelineFilter pipelineFilter);

    /**
     * 在此Item上移除一个用于过滤的{@link PipelineFilter}
     * @param pipelineFilter 要移除的PipelineFilter
     */
    void removePipelineFilter(PipelineFilter pipelineFilter);

    /**
     * 使用指定的{@link Pipeline}消费此Item。该{@link Pipeline}
     * 必须通过附加在此Item上所有的{@link PipelineFilter}的测试，
     * 否则会被丢弃。
     * @see PipelineFilter
     * @param pipeline pipeline
     */
    default void consume(Pipeline pipeline) {
        for (PipelineFilter pipelineFilter : getPipelineFilters()) {
            if (!pipelineFilter.applicable(pipeline)) return;
        }
        pipeline.process(this);
    }
}
