package com.sdider.crawler;

import com.sdider.api.*;
import com.sdider.crawler.impl.crawler.DefaultCrawler;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.sdider.crawler.impl.crawler.CrawlFactoryCommand.INTERVAL_CONFIG_NAME;
import static com.sdider.crawler.impl.crawler.CrawlFactoryCommand.UNIT_CONFIG_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 集成测试
 * @author yujiaxin
 */
public class IntegrationTest {
    static String body = "<html><title>Hello, Sdider</title></html>";
    @Mock
    ResponseParser parser;
    @Mock Pipeline pipeline;
    @Mock ExceptionHandler handler;
    @Mock Configuration configuration;
    @Mock Request request;
    List<Pipeline> pipelines;
    AutoCloseable autoCloseable;
    static HttpServer server;

    @BeforeAll
    static void beforeAll() throws IOException {
        server = HttpServer.create(new InetSocketAddress(4444), 0).createContext("/", httpExchange -> {
            httpExchange.sendResponseHeaders(200, 0);
            OutputStream os = httpExchange.getResponseBody();
            os.write(body.getBytes(StandardCharsets.UTF_8));
            os.close();
        }).getServer();
        server.start();
    }

    @AfterAll
    static void afterAll() {
        if (server != null) {
            server.stop(1);
        }
    }

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        when(configuration.get(INTERVAL_CONFIG_NAME)).thenReturn(2);
        when(configuration.get(UNIT_CONFIG_NAME)).thenReturn(TimeUnit.MILLISECONDS);
        when(request.getUrl()).thenReturn("http://localhost:4444");
        when(request.getMethod()).thenReturn("GET");
        pipelines = Collections.singletonList(pipeline);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void test() {
        when(parser.parse(any())).thenAnswer((call) ->{
            Response response = call.getArgument(0, Response.class);
            Result result = mock(Result.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
            Item item = mock(Item.class);
            when(item.get("reason")).thenReturn(response.getReasonPhrase());
            when(item.get("body")).thenReturn(new String(response.getBody(), StandardCharsets.UTF_8));
            doAnswer(c -> {
                Pipeline pipeline = c.getArgument(0, Pipeline.class);
                pipeline.process((Item) c.getMock());
                return null;
            }).when(item).consume(any());
            when(result.getItems()).thenReturn(Collections.singletonList(item));
            return result;
        });
        StringBuilder result = new StringBuilder();
        doAnswer((call) -> {
            Item item = call.getArgument(0, Item.class);
            result.append(item.get("body"));
            System.out.println(item.get("body"));
            return null;
        }).when(pipeline).process(any());
        DefaultCrawler crawler = new DefaultCrawler(parser, pipelines, handler, configuration);
        crawler.startRequests(Collections.singletonList(request));
        crawler.run();

        assertEquals(body, result.toString());
        verify(parser).parse(any());
        verify(pipeline).process(any());
    }
}
