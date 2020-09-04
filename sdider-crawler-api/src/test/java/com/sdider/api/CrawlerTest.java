package com.sdider.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CrawlerTest extends AutoAnnotationsMockTest{
    private Crawler crawler;
    @Mock private ResponseParser parser;
    private List<Pipeline> pipelines;
    @Mock private Pipeline pipeline;
    @Mock private ExceptionHandler exceptionHandler;
    @Mock private Configuration configuration;

    @BeforeEach
    void setUp() {
        pipelines = spy(Collections.singletonList(pipeline));
        crawler = mock(Crawler.class, withSettings()
                .useConstructor(parser, pipelines, exceptionHandler, configuration).defaultAnswer(CALLS_REAL_METHODS));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    void processResponse() {
        Response response = mock(Response.class);
        Result result = mock(Result.class);
        List requests = mock(List.class);
        when(parser.parse(same(response))).thenReturn(result);
        when(result.getRequests()).thenReturn(requests);

        crawler.processResponse(response);

        verify(parser).parse(same(response));
        verify(result).consume(argThat(argument -> argument.equals(pipelines)));
        verify(result).getRequests();
        verify(crawler).schedule(eq(requests));
    }

    @Test
    void processResponseWhenThrown() {
        doThrow(RuntimeException.class).when(parser).parse(any());

        crawler.processResponse(mock(Response.class));

        verify(parser).parse(any());
        verify(crawler).handleException(any(), any(), any());
    }

    @Test
    void handleException() {
        when(exceptionHandler.handleException(any(), any(), any())).thenReturn(mock(RequestContainer.class));

        crawler.handleException(new Exception(), mock(Request.class), mock(Response.class));

        verify(exceptionHandler).handleException(any(), any(), any());
        verify(crawler).schedule(anyList());
    }

    @Test
    void handleExceptionWithThrown() {
        when(exceptionHandler.handleException(any(), any(), any())).thenThrow(RuntimeException.class);

        assertDoesNotThrow(() ->crawler.handleException(new Exception(),
                mock(Request.class), null));

        verify(exceptionHandler).handleException(any(), any(), any());
    }
}