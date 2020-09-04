package com.sdider.api;

import java.util.List;

/**
 * 管理一组{@link Request}
 * @author yujiaxin
 */
public interface RequestContainer {
    /**
     * 将此RequestContainer中包含的{@link Request}
     * 替换为给定的{@link Request}列表。给定的列表后续发生变化，
     * 不会反映到此RequestContainer中。
     * @param requests {@link Request}列表
     */
    void setRequests(List<Request> requests);

    /**
     * 添加一个{@link Request}
     * @param request request
     */
    void addRequest(Request request);

    /**
     * 移除一个已添加至此对象的{@link Request}
     * @param request request
     */
    void removeRequest(Request request);

    /**
     * 返回一个列表，包含此对象中包含的所有{@link Request}，
     * 永远不会返回null。返回列表的变化不会影响到此对象中Request数量。
     * @return 一个包含此对象中所有 {@link Request}的列表
     */
    List<Request> getRequests();
}
