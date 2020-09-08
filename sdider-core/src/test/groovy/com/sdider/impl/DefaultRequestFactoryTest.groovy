package com.sdider.impl

import com.sdider.RequestConfig
import spock.lang.Specification

class DefaultRequestFactoryTest extends Specification {
    DefaultRequestFactory requestFactory

    void setup() {
        requestFactory = new DefaultRequestFactory()
    }

    def "Create"() {
        expect:
        requestFactory.create().getHeaders().isEmpty()
        requestFactory.create().getProxy() == null

        when:
        requestFactory.setRequestConfig(Mock(RequestConfig) {
            getProxy() >> '127.0.0.1:1080'
            getHeaders() >> ['foo':'bar']
        })
        def request = requestFactory.create()
        def request2 = requestFactory.create()

        then:
        ['foo':'bar'] == request.getHeaders()
        '127.0.0.1:1080' == request.getProxy()
        ['foo':'bar'] == request2.getHeaders()
        '127.0.0.1:1080' == request2.getProxy()
    }

    def "GetSetRequestConfig"() {
        def requestConfig = Mock(RequestConfig)
        requestFactory.setRequestConfig(requestConfig)
        expect:
        requestConfig === requestFactory.getRequestConfig()
    }
}
