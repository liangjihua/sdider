package com.sdider.impl.handler

import com.sdider.crawler.Request
import com.sdider.crawler.Response
import spock.lang.Specification

class DefaultExceptionHolderTest extends Specification {
    DefaultExceptionHolder exceptionHolder

    void setup() {
        exceptionHolder = new DefaultExceptionHolder(new Exception(), Mock(Request), Mock(Response))
    }

    def "Targets"() {
        when:
        exceptionHolder.targets {
            request 'http://foo.bar'
            request {
                GET 'http://bar.foo'
            }
        }
        def targets = exceptionHolder.getTargets()

        then:
        2 == targets.getRequests().size()
        def urls = targets.getRequests()*.getUrl()
        ['http://foo.bar', 'http://bar.foo'] == urls
    }
}
