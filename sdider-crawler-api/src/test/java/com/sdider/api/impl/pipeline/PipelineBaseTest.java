package com.sdider.api.impl.pipeline;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PipelineBaseTest {

    @Test
    void getName() {
        PipelineBase pipelineBase = mock(PipelineBase.class,
                withSettings().useConstructor("pipeline").defaultAnswer(CALLS_REAL_METHODS));

        assertEquals("pipeline", pipelineBase.getName());
    }
}