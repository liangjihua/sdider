package com.sdider.impl.parser

import com.sdider.ResponseConverter
import com.sdider.SdiderResponse
import com.sdider.crawler.Response
import spock.lang.Specification

class ClosureParserTest extends Specification {
    ClosureParser parser
    Closure parseAction
    ResponseConverter responseConverter
    SdiderResponse response
    Closeable closeable

    @SuppressWarnings('GroovyAssignabilityCheck')
    void setup() {
        closeable = Mock()
        response = Mock()
        responseConverter = Mock(ResponseConverter) {
            convert(_) >> response
        }
    }

    def "Parse"() {
        parseAction = {
            getCloseable().close()
            assert getResponse() === getDelegate()
        }
        parser = new ClosureParser(parseAction, responseConverter)
        when:
        def result = parser.parse(Mock(Response))
        then:
        1 * closeable.close()
        result == response.getResult()
    }
}
