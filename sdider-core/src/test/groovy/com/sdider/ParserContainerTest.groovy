package com.sdider

import com.sdider.api.Request
import com.sdider.api.Response
import com.sdider.api.ResponseParser
import spock.lang.Specification

class ParserContainerTest extends Specification {
    ParserContainer parserContainer
    Response response
    Request request
    ResponseParser parser

    void setup() {
        parser = Mock()
        request = Mock {
        }
        parserContainer = Mock {
            parse(_) >> {
                callRealMethod()
            }
        }
        response = Mock {
            getRequest() >> request
        }
    }

    def "Parse with custom parser"() {
        when:
        parserContainer.parse(response)

        then:
        1 * request.has(ParserContainer.PARSER_KEY) >> true
        1 * request.get(ParserContainer.PARSER_KEY) >> parser
        1 * parser.parse(response)
    }

    def "Parse with default parser"() {
        when:
        parserContainer.parse(response)

        then:
        1 * parserContainer.getMainParser() >> parser
        1 * request.has(ParserContainer.PARSER_KEY) >> false
        0 * request.get(ParserContainer.PARSER_KEY)
        1 * parser.parse(response)
    }

    def "Parse with custom string name"() {
        when:
        parserContainer.parse(response)

        then:
        1 * request.has(ParserContainer.PARSER_KEY) >> true
        1 * request.get(ParserContainer.PARSER_KEY) >> "fooParser"
        1 * parserContainer.getByName("fooParser") >> parser
        1 * parser.parse(response)
    }

    def "Parser when custom parser not found"() {
        when:
        parserContainer.parse(response)

        then:
        1 * request.has(ParserContainer.PARSER_KEY) >> true
        1 * request.get(ParserContainer.PARSER_KEY) >> "fooParser"
        1 * parserContainer.getByName("fooParser")
        1 * parserContainer.getMainParser() >> parser
        1 * parser.parse(response)
    }
}
