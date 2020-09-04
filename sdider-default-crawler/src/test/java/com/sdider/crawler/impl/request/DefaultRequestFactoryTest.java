package com.sdider.crawler.impl.request;

class DefaultRequestFactoryTest {
//    private final DefaultRequestFactory factory = new DefaultRequestFactory();
//
//    @Test
//    void get() {
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Content-type", "text/html");
//        ResponseParser parser = mock(ResponseParser.class);
//        HttpRequest request = factory.get("http://foo.bar", headers, "127.0.0.1:1087", parser);
//        assertNotNull(request);
//        assertEquals("http://foo.bar", request.getUrl());
//        assertEquals("GET", request.getMethod());
//        assertNull(request.getParams());
//        assertNotNull(request.getHeaders());
//        assertEquals("text/html", request.getHeaders().get("Content-type"));
//        assertEquals("127.0.0.1:1087", request.getProxy());
//        assertNull(request.getBody());
//        assertSame(parser, request.getParser());
//        assertTrue(request.getProperties().isEmpty());
//    }
//
//    @Test
//    void post() {
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Content-type", "text/html");
//        Map<String, String> params = new HashMap<>();
//        params.put("foo", "bar");
//        ResponseParser parser = mock(ResponseParser.class);
//        HttpRequest request = factory.post("http://foo.bar", headers, params, "hello", "127.0.0.1:1087", parser);
//        assertNotNull(request);
//        assertEquals("http://foo.bar", request.getUrl());
//        assertEquals("POST", request.getMethod());
//        assertNotNull(request.getParams());
//        assertEquals("bar", request.getParams().get("foo"));
//        assertNotNull(request.getHeaders());
//        assertEquals("text/html", request.getHeaders().get("Content-type"));
//        assertEquals("127.0.0.1:1087", request.getProxy());
//        assertEquals("hello", request.getBody());
//        assertSame(parser, request.getParser());
//        assertTrue(request.getProperties().isEmpty());
//    }
//
//    @Test
//    void create() {
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Content-type", "text/html");
//        Map<String, String> params = new HashMap<>();
//        params.put("foo", "bar");
//        ResponseParser parser = mock(ResponseParser.class);
//        HttpRequest request = factory.create("PUT","http://foo.bar", headers,
//                params, "hello", "127.0.0.1:1087", parser);
//        assertNotNull(request);
//        assertEquals("http://foo.bar", request.getUrl());
//        assertEquals("PUT", request.getMethod());
//        assertNotNull(request.getParams());
//        assertEquals("bar", request.getParams().get("foo"));
//        assertNotNull(request.getHeaders());
//        assertEquals("text/html", request.getHeaders().get("Content-type"));
//        assertEquals("127.0.0.1:1087", request.getProxy());
//        assertEquals("hello", request.getBody());
//        assertSame(parser, request.getParser());
//        assertTrue(request.getProperties().isEmpty());
//    }
}