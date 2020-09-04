package com.sdider.impl.pipeline;

import com.sdider.PipelineContainer;
import com.sdider.api.Pipeline;
import com.sdider.impl.exception.UndefinedPipelineException;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.MissingPropertyException;

import java.util.*;

/**
 * 默认的{@link PipelineContainer}实现。对于添加进入的{@link Pipeline}，会保持它们被加入的顺序。
 * pipeline被enable时也会保持它们被enable的顺序。以上两点，当同一个pipeline被add/enable多次时，
 * 它们的顺序保持原来的顺序不变。<br/>
 * 当一个{@link Pipeline}被{@link #remove(Pipeline)}移除同时，也会在已enable的列表移除。
 * @author yujiaxin
 */
public class DefaultPipelineContainer extends GroovyObjectSupport implements PipelineContainer {
    private final Map<String, Pipeline> container = new LinkedHashMap<>();
    private final Set<Pipeline> enabledPipelines = new LinkedHashSet<>();

    @Override
    public void enable(String pipeline) {
        Pipeline p = getByName(pipeline);
        if (p == null) {
            throw new UndefinedPipelineException(pipeline);
        }
        enabledPipelines.add(p);
    }

    @Override
    public void enable(Pipeline pipeline) {
        if (!container.containsKey(pipeline.getName())) {
            add(pipeline);
        }
        enabledPipelines.add(pipeline);
    }

    @Override
    public void disable(String pipeline) {
        Pipeline p = getByName(pipeline);
        if (p != null) {
            enabledPipelines.remove(p);
        }
    }

    @Override
    public void disable(Pipeline pipeline) {
        disable(pipeline.getName());
    }

    @Override
    public List<Pipeline> getEnabledPipelines() {
        return new ArrayList<>(enabledPipelines);
    }

    @Override
    public List<Pipeline> getAllPipelines() {
        return new ArrayList<>(container.values());
    }

    @Override
    public void add(Pipeline pipeline) {
        container.put(pipeline.getName(), pipeline);
    }

    @Override
    public void remove(Pipeline pipeline) {
        remove(pipeline.getName());
    }

    @Override
    public void remove(String name) {
        disable(name);
        container.remove(name);
    }

    @Override
    public Pipeline getByName(String name) {
        return container.get(name);
    }

    @Override
    public List<String> getAllNames() {
        return new ArrayList<>(container.keySet());
    }

    public Object propertyMissing(String propertyName) {
        Pipeline pipeline = getByName(propertyName);
        if (pipeline == null) {
            throw new MissingPropertyException(propertyName, this.getClass());
        }
        return pipeline;
    }
}
