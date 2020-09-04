package com.sdider.crawler.impl.downloader;

import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleResponseConsumer;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
import org.apache.hc.core5.http.nio.support.AbstractAsyncResponseConsumer;
import org.apache.hc.core5.http.protocol.HttpContext;


/**
 * 支持压缩传输的{@link AsyncResponseConsumer}。代码copy自{@link SimpleResponseConsumer}
 * @author yujiaxin
 */
class SimpleDecompressingResponseConsumer extends AbstractAsyncResponseConsumer<SimpleHttpResponse, byte[]> {

    public SimpleDecompressingResponseConsumer() {
        super(new SimpleAsyncDecompressingEntityConsumer());
    }

    @Override
    protected SimpleHttpResponse buildResult(HttpResponse response, byte[] entity, ContentType contentType) {
        final SimpleHttpResponse simpleResponse = SimpleHttpResponse.copy(response);
        if (entity != null) {
            simpleResponse.setBody(entity, contentType);
        }
        return simpleResponse;
    }

    @Override
    public void informationResponse(HttpResponse response, HttpContext context) {

    }
}
