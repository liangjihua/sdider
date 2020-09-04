package com.sdider.crawler.impl.response;

import com.sdider.api.Request;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DefaultResponseTest {
    private DefaultResponse response;
    private Request request;
    private SimpleHttpResponse httpResponse;
    private final String content = "foo";

    @BeforeEach
    void setUp() {
        httpResponse = response(content.getBytes(StandardCharsets.UTF_8));
        response = new DefaultResponse(mock(Request.class), httpResponse);
        request = mock(Request.class);
    }

    private SimpleHttpResponse response(byte[] content) {
        return SimpleHttpResponse.create(200, content);
    }

    @Test
    void getRequest() {
        response = new DefaultResponse(request, httpResponse);

        assertEquals(request, response.getRequest());
    }

//    @Test
//    void getHtml() throws IOException {
//        assertNotNull(response.getDocument());
//        assertEquals("你好", response.getDocument().text());
//
//        response = new DefaultResponse(request, httpResponse);
//        assertNull(response.getDocument());
//    }

    @Test
    void getText() {
        assertEquals(content, response.getText());

        response = new DefaultResponse(request, response(null));
        assertNull(response.getText());
    }

    @Test
    void getContentType() {
        assertEquals("text/plain; charset=ISO-8859-1", response.getContentType());

        response = new DefaultResponse(request, response(null));

        assertNull(response.getContentType());
    }

    @Test
    void getBody() {
        assertNotNull(response.getBody());
        assertArrayEquals(content.getBytes(StandardCharsets.UTF_8), response.getBody());
    }

    @Test
    void getStatusCode() {
        assertEquals(200, response.getStatusCode());
        httpResponse.setCode(401);
        response = new DefaultResponse(request, httpResponse);
        assertEquals(401, response.getStatusCode());
    }

    @Test
    void getReasonPhrase() {
        assertEquals("OK", response.getReasonPhrase());

        SimpleHttpResponse httpResponse = response(null);
        httpResponse.setReasonPhrase("foo");
        this.response = new DefaultResponse(request, httpResponse);

        assertEquals(httpResponse.getReasonPhrase(), response.getReasonPhrase());
    }

    @Test
    void getHeaders() {
        assertTrue(response.getHeaders().isEmpty());

        httpResponse.addHeader("foo", "bar");
        response = new DefaultResponse(request, httpResponse);
        assertFalse(response.getHeaders().isEmpty());

        Map<String, String> headers = response.getHeaders();
        assertThrows(UnsupportedOperationException.class, ()->headers.put("foo", "bar"));
    }
}