package com.sdider.impl.request;

import com.sdider.api.Request;
import com.sdider.api.RequestContainer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * AbstractRequestContainer提供了支持存取{@link Request}的基本操作。
 * @author yujiaxin
 */
public abstract class AbstractRequestContainer implements RequestContainer {
    protected List<Request> requests = new LinkedList<>();

    /**
     * @implNote 使用了 {@link #addRequest(Request)}来实现功能
     */
    @Override
    public void setRequests(List<Request> requests) {
        this.requests = new LinkedList<>();
        if (requests != null) {
            for (Request request : requests) {
                addRequest(request);
            }
        }
    }

    @Override
    public void addRequest(Request request) {
        if (request != null) {
            requests.add(request);
        }
    }

    @Override
    public void removeRequest(Request request) {
        if (request != null) {
            requests.remove(request);
        }
    }

    @Override
    public List<Request> getRequests() {
        return new ArrayList<>(requests);
    }

}
