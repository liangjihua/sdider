package com.sdider.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

class ItemTest {
    private Item item;

    @BeforeEach
    void setUp() {
        item = mock(Item.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    }

    @Test
    void filteredConsume() {
        PipelineFilter trueFilter = when(mock(PipelineFilter.class).applicable(any())).thenReturn(true).getMock();
        PipelineFilter falseFilter = when(mock(PipelineFilter.class).applicable(any())).thenReturn(false).getMock();
        PipelineFilter trueFilter2 = when(mock(PipelineFilter.class).applicable(any())).thenReturn(true).getMock();
        when(item.getPipelineFilters()).thenReturn(Arrays.asList(trueFilter, falseFilter, trueFilter2));
        Pipeline pipeline = mock(Pipeline.class);

        item.consume(pipeline);

        verify(item).getPipelineFilters();
        verify(trueFilter).applicable(same(pipeline));
        verify(falseFilter).applicable(same(pipeline));
        verify(trueFilter2, never()).applicable(same(pipeline));
        verify(pipeline, never()).process(any());
    }

    @Test
    void unfilteredConsume() {
        PipelineFilter trueFilter = when(mock(PipelineFilter.class).applicable(any())).thenReturn(true).getMock();
        PipelineFilter trueFilter1 = when(mock(PipelineFilter.class).applicable(any())).thenReturn(true).getMock();
        PipelineFilter trueFilter2 = when(mock(PipelineFilter.class).applicable(any())).thenReturn(true).getMock();
        when(item.getPipelineFilters()).thenReturn(Arrays.asList(trueFilter, trueFilter1, trueFilter2));
        Pipeline pipeline = mock(Pipeline.class);

        item.consume(pipeline);

        verify(item).getPipelineFilters();
        verify(trueFilter).applicable(same(pipeline));
        verify(trueFilter1).applicable(same(pipeline));
        verify(trueFilter2).applicable(same(pipeline));
        verify(pipeline).process(any());
    }

    private List<PipelineFilter> buildFilter(boolean... result) {
        List<PipelineFilter> filters = new ArrayList<>(result.length);
        for (boolean b : result) {
            PipelineFilter filter = mock(PipelineFilter.class);
            when(filter.applicable(any())).thenReturn(b);
            filters.add(filter);
        }
        return filters;
    }
}