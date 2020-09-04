package com.sdider.crawler.impl.request;

class RequestBuilderTest {
//    @Mock
//    private ResponseParser responseParser;
//
//    private AutoCloseable autoCloseable;
//    @BeforeEach
//    void setUp() {
//        autoCloseable = MockitoAnnotations.openMocks(this);
//    }
//
//    @AfterEach
//    void tearDown() throws Exception {
//        autoCloseable.close();
//    }
//
//    @Test
//    void build() {
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Content-type", "text/html");
//        Map<String, String> params = new HashMap<>();
//        params.put("name", "monica");
//        HttpRequest request = RequestBuilder.create("http://foo.bar")
//                .header("accept", "*/*")
//                .headers(headers)
//                .method("POST")
//                .param("foo", "bar")
//                .params(params)
//                .proxy("127.0.0.1:1087")
//                .parser(responseParser)
//                .body("body").build();
//        assertEquals("http://foo.bar", request.getUrl());
//        assertEquals("POST", request.getMethod());
//        assertEquals("bar", request.getParams().get("foo"));
//        assertEquals( 2, request.getParams().size());
//        assertEquals("text/html", request.getHeaders().get("Content-type"));
//        assertEquals( 2, request.getHeaders().size());
//        assertEquals( "127.0.0.1:1087", request.getProxy());
//        assertEquals("body", request.getBody());
//        assertNotNull(request.getParser());
//        assertSame(responseParser, request.getParser());
//    }
//
//    @Test
//    void createSimpleRequest() {
//        HttpRequest request = RequestBuilder.create("http://foo.bar").build();
//        assertNotNull(request);
//        assertEquals("http://foo.bar", request.getUrl());
//        assertEquals("GET", request.getMethod());
//    }
//
//    @Test
//    void emptyBody() {
//        HttpRequest request = RequestBuilder.create("http://foo.bar").method("POST").build();
//
//        assertEquals("0", request.getHeaders().get("Content-Length"));
//    }
//
//    @Test
//    void notEmptyBody() {
//        HttpRequest request = RequestBuilder.create("http://foo.bar").method("POST")
//                .param("foo", "bar").build();
//
//        assertNull(request.getHeaders());
//
//        request = RequestBuilder.create("http://foo.bar").method("POST")
//                .body("foo").build();
//        assertNull(request.getHeaders());
//    }
}