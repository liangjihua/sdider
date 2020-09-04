package com.sdider.api;

/**
 * PipelineFilter检查一个{@link Pipeline}是否适用。
 * {@link Item}使用PipelineFilter来检查一个{@link Pipeline}
 * 是否能够消费自己。
 * @author yujiaxin
 */
public interface PipelineFilter {

    /**
     * 检查给定的{@link Pipeline}是否适用。
     * @param pipeline 要检查的pipeline
     * @return 如果可用返回{@code true}；其他返回{@code false}。
     */
    boolean applicable(Pipeline pipeline);
}
