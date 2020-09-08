package com.sdider.impl.handler

import com.sdider.SdiderRequest
import com.sdider.api.Request
import com.sdider.api.Response
import com.sdider.impl.request.SdiderRequestFactory
import spock.lang.Specification

class DefaultExceptionHolderTest extends Specification {
    DefaultExceptionHolder exceptionHolder
    SdiderRequestFactory requestFactory

    void setup() {
        requestFactory = Mock(SdiderRequestFactory)
        exceptionHolder = new DefaultExceptionHolder(new Exception(), Mock(Request), Mock(Response), requestFactory)
    }

    def "Targets"() {
        when:
        exceptionHolder.targets {
            request 'http://foo.bar'
            request 'http://bar.bar'
        }
        def targets = exceptionHolder.getTargets()

        then:
        2 * requestFactory.create() >> {
            Mock(SdiderRequest) {
                1 * GET(_)
            }
        }
        2 == targets.getRequests().size()
    }
}
