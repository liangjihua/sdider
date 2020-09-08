package com.sdider

import spock.lang.Specification

class SdiderRequestTest extends Specification {
    SdiderRequest request

    @SuppressWarnings('GroovyAssignabilityCheck')
    void setup() {
        request = Mock(SdiderRequest) {
            GET(_) >> {
                callRealMethod()
            }
            POST(_) >> {
                callRealMethod()
            }
            parser(_) >> {
                callRealMethod()
            }
        }
    }

    def "GET"() {
        when:
        request.GET('http://foo.bar')

        then:
        1 * request.method(SdiderRequest.METHOD_GET)
        1 * request.url('http://foo.bar')
    }

    def "POST"() {
        when:
        request.POST('http://foo.bar')

        then:
        1 * request.method(SdiderRequest.METHOD_POST)
        1 * request.url('http://foo.bar')
    }

    def "Parser"() {
        when:
        request.parser('foo')

        then:
        1 * request.set(ParserContainer.PARSER_KEY, 'foo')
    }
}
