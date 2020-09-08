package com.sdider.impl.item;

import com.sdider.SdiderItem;
import com.sdider.api.Pipeline;
import com.sdider.api.PipelineFilter;
import com.sdider.api.Request;
import com.sdider.impl.common.DefaultDynamicPropertiesObject;
import com.sdider.utils.ClosureUtils;
import groovy.lang.*;

import java.util.*;

/**
 * {@link SdiderItem}的默认实现。可以使用Groovy语法调用动态方法设置属性：
 * <pre>
 * def item = new DefaultItem()
 * item.foo 'bar'    //使用单个参数调用动态方法，等价于 item.foo = 'bar'
 * assert 'bar' == item.foo
 * </pre>
 * @author yujiaxin
 */
public class DefaultItem extends GroovyObjectSupport implements SdiderItem {
    private final DefaultDynamicPropertiesObject<Object> fields = new DefaultDynamicPropertiesObject<>();
    private final Request request;
    private List<PipelineFilter> pipelineFilters;

    public DefaultItem(Request request) {
        this.request = request;
    }

    @Override
    public Request getRequest() {
        return request;
    }

    @Override
    public boolean has(String name) {
        return fields.has(name);
    }

    @Override
    public Object get(String name) {
        return fields.get(name);
    }

    @Override
    public Object getProperty(String propertyName) {
        if (Objects.equals("request", propertyName)) {
            return getRequest();
        }
        if (Objects.equals("item", propertyName)) {
            return getItem();
        }
        if (Objects.equals("properties", propertyName)) {
            return getProperties();
        }
        if (has(propertyName)) {
            return get(propertyName);
        }
        throw new MissingPropertyException(propertyName, this.getClass());
    }

    public Object methodMissing(String name, Object args) {
        return fields.methodMissing(name, args);
    }

    @Override
    public void setProperty(String propertyName, Object newValue) {
        if (Objects.equals("request", propertyName) || Objects.equals("item", propertyName)
                || Objects.equals("properties", propertyName)) {
            throw new ReadOnlyPropertyException(propertyName, this.getClass());
        }
        set(propertyName, newValue);
    }

    @Override
    public void set(String name, Object value) {
        fields.set(name, value);
    }

    @Override
    public Map<String, Object> getProperties() {
        return fields.getProperties();
    }

    @Override
    public List<PipelineFilter> getPipelineFilters() {
        return pipelineFilters == null ? Collections.emptyList() : new ArrayList<>(pipelineFilters);
    }

    @Override
    public void addPipelineFilter(PipelineFilter pipelineFilter) {
        if (pipelineFilter == null) {
            throw new IllegalArgumentException("pipelineFilter is null");
        }
        if (pipelineFilters == null) {
            pipelineFilters = new LinkedList<>();
        }
        pipelineFilters.add(pipelineFilter);
    }

    @Override
    public void removePipelineFilter(PipelineFilter pipelineFilter) {
        if (pipelineFilter == null) {
            throw new IllegalArgumentException("pipelineFilter is null");
        }
        if (pipelineFilters != null) {
            pipelineFilters.remove(pipelineFilter);
        }
    }

    @Override
    public void filter(@DelegatesTo(Pipeline.class)Closure<Boolean> closure) {
        addPipelineFilter(new ClosurePipelineFilter(closure));
    }

    private static class ClosurePipelineFilter implements PipelineFilter {
        private final Closure<Boolean> closure;

        private ClosurePipelineFilter(Closure<Boolean> closure) {
            this.closure = closure;
        }


        @Override
        public boolean applicable(Pipeline pipeline) {
            return ClosureUtils.delegateCall(pipeline, closure);
        }
    }

    @Override
    public String toString() {
        return "DefaultItem{" + "fields=" + fields +
                ", request=" + request +
                '}';
    }
}
