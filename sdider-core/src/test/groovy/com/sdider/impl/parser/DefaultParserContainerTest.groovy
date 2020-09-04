package com.sdider.impl.parser

import com.sdider.api.ResponseParser
import spock.lang.Specification

class DefaultParserContainerTest extends Specification {
    DefaultParserContainer container
    ResponseParser parser

    void setup() {
        parser = Mock(ResponseParser)
        container = new DefaultParserContainer()
    }

    def "Add"() {
        when:
        container.add('foo', parser)
        then:
        parser === container.getByName('foo')
        when:
        container.add('foo', Mock(ResponseParser))
        then:
        thrown(IllegalArgumentException)
    }

    def "GetByName"() {
        expect:
        null == container.getByName('foo')

        when:
        container.add('foo', parser)
        then:
        parser == container.getByName('foo')
    }

    def "GetMainParser"() {
        expect:
        null == container.getMainParser()

        when:
        container.setMainParser(parser)
        then:
        parser === container.getMainParser()
    }

    def "getMainParser2"() {
        when:
        container.add(DefaultParserContainer.MAIN_PARSER_NAME, parser)
        then:
        parser === container.getMainParser()
    }

    def "SetMainParser"() {
        when:
        container.setMainParser(parser)

        then:
        parser == container.getByName(DefaultParserContainer.MAIN_PARSER_NAME)
    }

    def "getProperty"() {
        container.setMainParser(parser)
        def fooParser = Mock(ResponseParser)
        container.add('foo', fooParser)

        expect:
        parser === container.mainParser
        fooParser === container.foo
    }
}
