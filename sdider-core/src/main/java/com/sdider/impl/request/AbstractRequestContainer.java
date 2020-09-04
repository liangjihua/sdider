package com.sdider.impl.request;

import com.sdider.SdiderRequestContainer;
import com.sdider.api.Request;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * AbstractRequestContainer提供了支持存取{@link Request}的基本操作。
 * @author yujiaxin
 */
public abstract class AbstractRequestContainer implements SdiderRequestContainer {
    private List<Request> requests = new LinkedList<>();

    @Override
    public void setRequests(List<Request> requests) {
        if (requests == null) {
            this.requests = new LinkedList<>();
        } else {
            this.requests = new LinkedList<>(requests);
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
