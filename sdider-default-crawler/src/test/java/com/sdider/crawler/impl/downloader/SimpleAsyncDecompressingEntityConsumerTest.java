package com.sdider.crawler.impl.downloader;

import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.EntityDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.internal.util.reflection.FieldReader;

import java.util.function.Function;
import java.util.stream.Stream;

import static com.sdider.crawler.impl.downloader.SimpleAsyncDecompressingEntityConsumer.*;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.reflection.FieldSetter.setField;

class SimpleAsyncDecompressingEntityConsumerTest {
    private SimpleAsyncDecompressingEntityConsumer entityConsumer;
    private EntityDetails entityDetails;
    @SuppressWarnings("rawtypes")
    private FutureCallback callback;
    private FieldReader decoderReader;
    private FieldReader resultCallbackReader;

    @BeforeEach
    void setUp() throws NoSuchFieldException {
        entityConsumer = spy(new SimpleAsyncDecompressingEntityConsumer());
        entityDetails = mock(EntityDetails.class);
        callback = mock(FutureCallback.class);
        decoderReader = new FieldReader(entityConsumer, SimpleAsyncDecompressingEntityConsumer.class.getDeclaredField("decoder"));
        resultCallbackReader = new FieldReader(entityConsumer, SimpleAsyncDecompressingEntityConsumer.class.getDeclaredField("resultCallback"));
    }

    @SuppressWarnings("unchecked")
    @ParameterizedTest
    @MethodSource("decoderParams")
    void streamStart(String encoding, Function<byte[], byte[]> decoder) {
        when(entityDetails.getContentEncoding()).thenReturn(encoding);
        entityConsumer.streamStart(entityDetails, callback);

        assertSame(callback, resultCallbackReader.read());
        assertSame(decoder, decoderReader.read());
    }

    static Stream<Arguments> decoderParams() {
        return Stream.of(Arguments.of("gzip", GZIP_DECODER),
                Arguments.of("x-gzip", GZIP_DECODER),
                Arguments.of("deflate", DEFLATE_DECODER),
                Arguments.of("some-encoding", SIMPLE_DECODER),
                Arguments.of(null, SIMPLE_DECODER));
    }

    @SuppressWarnings("unchecked")
    @Test
    void completed() throws NoSuchFieldException {
        when(entityDetails.getContentEncoding()).thenReturn("gzip");
        GzipDecoder decoder = spy(GZIP_DECODER);
        entityConsumer.streamStart(entityDetails, callback);
        setField(entityConsumer, SimpleAsyncDecompressingEntityConsumer.class.getDeclaredField("decoder"), decoder);

        entityConsumer.completed();

        verify(decoder).apply(any());
        verify(callback).completed(any());
    }
}