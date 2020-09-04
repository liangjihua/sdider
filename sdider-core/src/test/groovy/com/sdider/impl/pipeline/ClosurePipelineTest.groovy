package com.sdider.impl.pipeline

import com.sdider.crawler.Item
import spock.lang.Specification

class ClosurePipelineTest extends Specification {
    ClosurePipeline pipeline
    Closeable closeable
    Closure pipelineAction

    void setup() {
        closeable = Mock()
    }

    def "Process"() {
        def item = Mock(Item)
        pipelineAction = {
            getCloseable().close()
            assert item === getDelegate()
        }
        pipeline = new ClosurePipeline('foo', pipelineAction)
        when:
        pipeline.process(item)

        then:
        1 * closeable.close()
    }
}
