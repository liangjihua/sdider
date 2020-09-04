package com.sdider;

import com.sdider.api.Pipeline;

import java.util.List;

/**
 * PipelineContainer管理一组{@link Pipeline}，
 * 并将它们分为两种类型：启用的、未启用的。
 * @author yujiaxin
 */
public interface PipelineContainer {

    /**
     * 启用一个已添加的{@link Pipeline}，给定name用于
     * 查找{@link Pipeline#getName()}与之相同的pipeline并启用。
     * @param name name
     */
    void enable(String name);

    /**
     * 启用一个{@link Pipeline}，若给定{@link Pipeline}未被添加过，
     * 添加并启用。
     * @param pipeline pipeline
     */
    void enable(Pipeline pipeline);

    /**
     * 禁用一个已添加的{@link Pipeline}，给定name用于
     * 查找{@link Pipeline#getName()}与之相同的pipeline并禁用。
     * @param name name
     */
    void disable(String name);

    /**
     * 禁用一个{@link Pipeline}
     * @param pipeline pipeline
     */
    void disable(Pipeline pipeline);

    /**
     * 返回Container中包含的已启用的所有{@link Pipeline}s。
     * @see #enable(String)
     * @see #enable(Pipeline)
     * @return 已启用的所有{@link Pipeline}s。
     */
    List<Pipeline> getEnabledPipelines();

    /**
     * 返回以列表形式此Container中已添加的所有{@link Pipeline}s，
     * 返回的列表是安全的，与此Container不受到互相变化的影响。
     * @return 已添加的所有{@link Pipeline}s
     */
    List<Pipeline> getAllPipelines();

    /**
     * 添加一个{@link Pipeline}
     * @param pipeline pipeline
     */
    void add(Pipeline pipeline);

    /**
     * 移除一个{@link Pipeline}
     * @param pipeline pipeline
     */
    void remove(Pipeline pipeline);

    /**
     * 移除一个{@link Pipeline}
     * @param name pipeline name
     */
    void remove(String name);

    /**
     * 从已添加的{@link Pipeline}s中返回与给定name名称相同的{@link Pipeline}。
     * @param name pipeline name
     * @return name与给定name相同的 {@link Pipeline}，如果不存在返回null
     */
    Pipeline getByName(String name);

    /**
     * 返回已添加的所有{@link Pipeline}的name，永远不会返回null
     * @return 已添加的所有{@link Pipeline}的name
     */
    List<String> getAllNames();
}
