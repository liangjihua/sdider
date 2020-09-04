package com.sdider.impl.handler

import com.sdider.ResponseConverter
import com.sdider.crawler.Request
import com.sdider.crawler.Response
import spock.lang.Specification

class ExceptionHandlerBaseTest extends Specification {
    ExceptionHandlerBase handlerBase
    ResponseConverter converter

    @SuppressWarnings('GroovyAssignabilityCheck')
    void setup() {
        converter = Mock()
        handlerBase = Mock(constructorArgs:[converter], ExceptionHandlerBase) {
            handleException(_, _, _) >> {
                callRealMethod()
            }
        }
    }

    def "HandleException"() {
        Response response = Mock()
        when:
        handlerBase.handleException(new Exception(), Mock(Request), response)

        then:
        1 * converter.convert(response)
    }

    def "null response"() {
        when:
        handlerBase.handleException(new Exception(), Mock(Request), null)

        then:
        0 * converter.convert(_)
    }
}
