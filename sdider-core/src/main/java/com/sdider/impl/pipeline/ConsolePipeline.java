package sdider.impl.pipeline;

import com.sdider.crawler.Item;
import com.sdider.crawler.Request;
import com.sdider.crawler.impl.pipeline.PipelineBase;

import java.util.Map;

/**
 * ConsolePipeline将{@link Item}打印到控制台。<br/>
 * @author yujiaxin
 */
public class ConsolePipeline extends PipelineBase {
    public static final String DEFAULT_CONSOLE_PIPELINE_NAME = "_default_console_pipeline";

    public ConsolePipeline() {
        super(DEFAULT_CONSOLE_PIPELINE_NAME);
    }

    public ConsolePipeline(String name) {
        super(name);
    }

    @Override
    public void process(Item item) {
        Request request = item.getRequest();
        System.out.printf("request: %s %s%n", request.getMethod(), request.getUrl());
        Map<String, Object> properties = item.getProperties();
        System.out.println("item: {");
        properties.forEach((k, v) -> System.out.printf("    %s=%s%n", k, v));
        System.out.println("}");
        System.out.println();
    }
}
