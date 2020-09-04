package com.sdider.impl.handler

import com.sdider.ResponseConverter
import com.sdider.SdiderResponse
import com.sdider.api.Request
import spock.lang.Specification

class ClosureExceptionHandlerTest extends Specification {
    ClosureExceptionHandler handler
    Closure closure
    Request aRequest
    SdiderResponse aResponse
    Exception aException
    Closeable closeable

    void setup() {
        closeable = Mock()
        aException = new Exception()
        aRequest = Mock()
        aResponse = Mock()
        closure = {
            closeable.close()
            assert aException == exception
            assert aRequest == request
            assert aResponse == response
        }
        handler = new ClosureExceptionHandler(Mock(ResponseConverter), closure)
    }

    def "Handle"() {
        when:
        handler.handle(aException, aRequest, aResponse)

        then:
        1 * closeable.close()
    }
}
