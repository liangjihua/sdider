package com.sdider.impl.pipeline;

import com.sdider.api.Item;
import com.sdider.api.impl.pipeline.PipelineBase;
import com.sdider.utils.ClosureUtils;
import groovy.lang.Closure;

/**
 *
 * @author yujiaxin
 */
@SuppressWarnings("rawtypes")
public class ClosurePipeline extends PipelineBase {
    private final Closure pipelineAction;
    public ClosurePipeline(String name, Closure pipelineAction) {
        super(name);
        this.pipelineAction = pipelineAction;
    }

    @Override
    public void process(Item item) {
        ClosureUtils.delegateRun(item, pipelineAction);
    }
}
