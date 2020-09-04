package com.sdider.impl.request

import com.sdider.SdiderRequest
import spock.lang.Specification

class DefaultRequestContainerTest extends Specification {
    DefaultRequestContainer container

    void setup() {
        container = new DefaultRequestContainer()
    }

    def "Request"() {
        when:
        container.request {
            POST 'http://foo.bar'
        }
        then:
        1 == container.getRequests().size()
        'http://foo.bar' == container.getRequests()[0].getUrl()
        SdiderRequest.METHOD_POST == container.getRequests()[0].getMethod()
    }

    def "request with urls"() {
        when:
        container.request('http://foo.bar')
        container.request('http://bar.foo')
        container.request('', null)

        then:
        2 == container.getRequests().size()
        'http://foo.bar' == container.getRequests()[0].getUrl()
        SdiderRequest.METHOD_GET == container.getRequests()[0].getMethod()
        'http://bar.foo' == container.getRequests()[1].getUrl()
        SdiderRequest.METHOD_GET == container.getRequests()[1].getMethod()
    }
}
