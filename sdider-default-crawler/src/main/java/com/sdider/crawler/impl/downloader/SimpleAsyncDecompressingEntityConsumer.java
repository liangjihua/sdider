package com.sdider.crawler.impl.downloader;

import org.apache.commons.io.IOUtils;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.nio.AsyncEntityConsumer;
import org.apache.hc.core5.http.nio.entity.AbstractBinDataConsumer;
import org.apache.hc.core5.util.ByteArrayBuffer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.Function;
import java.util.zip.DeflaterInputStream;
import java.util.zip.GZIPInputStream;

/**
 * 支持压缩格式的{@link AsyncEntityConsumer}，httpclient 5.0
 * 异步模式不支持压缩传输，通过重新实现{@link AsyncEntityConsumer}
 * 支持压缩格式。实现的大多数代码copy自{@link org.apache.hc.client5.http.async.methods.SimpleAsyncEntityConsumer}
 * @author yujiaxin
 */
@SuppressWarnings("JavadocReference")
class SimpleAsyncDecompressingEntityConsumer extends AbstractBinDataConsumer implements AsyncEntityConsumer<byte[]> {
    private volatile Function<byte[], byte[]> decoder;
    private volatile FutureCallback<byte[]> resultCallback;
    private volatile byte[] content;
    private final ByteArrayBuffer buffer;

    public SimpleAsyncDecompressingEntityConsumer() {
        super();
        this.buffer = new ByteArrayBuffer(1024);
    }

    @Override
    public void streamStart(EntityDetails entityDetails, FutureCallback<byte[]> resultCallback) {
        this.resultCallback = resultCallback;
        if (entityDetails.getContentEncoding() == null) {
            this.decoder = SIMPLE_DECODER;
        } else {
            switch (entityDetails.getContentEncoding()) {
                case "gzip":
                case "x-gzip":
                    decoder = GZIP_DECODER;
                    break;
                case "deflate":
                    decoder = DEFLATE_DECODER;
                    break;
                default:
                    this.decoder = SIMPLE_DECODER;
                    break;
            }
        }
    }

    static final GzipDecoder GZIP_DECODER = new GzipDecoder();
    static class GzipDecoder implements Function<byte[], byte[]> {
        @Override
        public byte[] apply(byte[] bytes) {
            try {
                return IOUtils.toByteArray(new GZIPInputStream(new ByteArrayInputStream(bytes)));
            } catch (IOException ignored) {
                return bytes;
            }
        }
    }
    static final DeflateDecoder DEFLATE_DECODER = new DeflateDecoder();
    static class DeflateDecoder implements Function<byte[], byte[]> {
        @Override
        public byte[] apply(byte[] bytes) {
            try {
                return IOUtils.toByteArray(new DeflaterInputStream(new ByteArrayInputStream(bytes)));
            } catch (IOException ignored) {
                return bytes;
            }
        }
    }

    static final SimpleDecoder SIMPLE_DECODER = new SimpleDecoder();
    static class SimpleDecoder implements Function<byte[], byte[]> {
        @Override
        public byte[] apply(byte[] bytes) {
            return bytes;
        }
    }

    @Override
    public void failed(Exception cause) {
        if (resultCallback != null) {
            resultCallback.failed(cause);
        }
        releaseResources();
    }

    @Override
    public byte[] getContent() {
        return content;
    }

    @Override
    protected int capacityIncrement() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected void data(ByteBuffer src, boolean endOfStream) {
        if (src == null) {
            return;
        }
        if (src.hasArray()) {
            buffer.append(src.array(), src.arrayOffset() + src.position(), src.remaining());
        } else {
            while (src.hasRemaining()) {
                buffer.append(src.get());
            }
        }
    }

    @Override
    protected void completed() {
        content = decoder.apply(buffer.toByteArray());
        if (resultCallback != null) {
            resultCallback.completed(content);
        }
        releaseResources();
    }

    @Override
    public void releaseResources() {
        buffer.clear();
    }
}
