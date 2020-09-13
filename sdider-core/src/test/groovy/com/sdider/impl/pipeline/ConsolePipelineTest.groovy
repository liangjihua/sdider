package com.sdider.impl.pipeline

import com.sdider.api.Item
import com.sdider.api.Request
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
        String.format("request: null null%nitem: {%n    name=Monica%n    age=20%n}%n%n") == outputStream.toString()

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
