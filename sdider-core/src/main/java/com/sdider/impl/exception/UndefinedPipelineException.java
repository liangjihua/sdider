package com.sdider.impl.exception;

/**
 * @author yujiaxin
 */
public class UndefinedPipelineException extends RuntimeException{

    public UndefinedPipelineException(String pipelineName) {
        super(String.format("pipeline: %s 未定义。", pipelineName));
    }
}
