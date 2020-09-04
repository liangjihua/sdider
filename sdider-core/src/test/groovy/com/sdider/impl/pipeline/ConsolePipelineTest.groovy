package com.sdider.impl.pipeline

import com.sdider.crawler.Item
import com.sdider.crawler.Request
import spock.lang.Shared
import spock.lang.Specification

class ConsolePipelineTest extends Specification {
    @Shared def originPrintStream
    ByteArrayOutputStream outputStream

    def setupSpec() {
        originPrintStream = System.out
    }

    def setup() {
        outputStream = new ByteArrayOutputStream()
        PrintStream printStream = new PrintStream(outputStream)
        System.setOut(printStream)
    }

    def cleanup() {
        System.setOut(originPrintStream)
    }

    def "Process"() {
        setup:
        ConsolePipeline consolePipeline = new ConsolePipeline()
        Item item = Mock(Item, {
            getRequest() >> Mock(Request)
            getProperties() >> [name:"Monica", age: 20]
        })

        when:
        consolePipeline.process(item)

        then:
        'request: null null\r\nitem: {\r\n    name=Monica\r\n    age=20\r\n}\r\n\r\n' == outputStream.toString()

    }


    def "getName"() {
        setup:
        ConsolePipeline consolePipeline = new ConsolePipeline()

        expect:
        ConsolePipeline.DEFAULT_CONSOLE_PIPELINE_NAME == consolePipeline.getName()

        when:
        consolePipeline = new ConsolePipeline("console")
        then:
        "console" == consolePipeline.getName()
    }
}
