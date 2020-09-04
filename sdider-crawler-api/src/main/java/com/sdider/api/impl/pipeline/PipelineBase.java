package com.sdider.api.impl.pipeline;

import com.sdider.api.Pipeline;

/**
 * @author yujiaxin
 */
public abstract class PipelineBase implements Pipeline {
    private final String name;

    protected PipelineBase(String name) {
        this.name = name;
    }


    @Override
    public String getName() {
        return name;
    }
}
