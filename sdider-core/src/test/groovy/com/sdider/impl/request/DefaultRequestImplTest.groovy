package com.sdider.impl.request

import com.sdider.RequestConfig

class DefaultRequestImplTest extends RequestConfigBaseTest {
    DefaultRequestImpl request

    void setup() {
        request = reqConfig as DefaultRequestImpl
    }

    @Override
    RequestConfig create() {
        return new DefaultRequestImpl()
    }

    def "GetMethod"() {
        expect:
        null == request.getMethod()

        when:
        request.method("GET")
        then:
        "GET" == request.getMethod()
    }

    def "GetUrl"() {
        expect:
        null == request.getUrl()

        when:
        request.url("http://foo.bar")

        then:
        'http://foo.bar' == request.getUrl()
    }

    def "GetParams"() {
        expect:
        request.getParams().isEmpty()

        when:
        request.params {
            'foo' 'bar'
        }
        def params = request.getParams()
        then:
        [foo:'bar'] == params

        and:

        when:
        params.put('name', 'monica')
        then:
        [foo:'bar'] == request.getParams()
    }

    def "GetBody"() {
        expect:
        null == request.getBody()

        when:
        request.body('foo')

        then:
        'foo' == request.getBody()
    }

    def "Method"() {
        when:
        request.method('GET')
        then:
        'GET' == request.getMethod()
        when:
        request.method(null)
        then:
        null == request.getMethod()
    }

    def "Url"() {
        when:
        request.url('http://foo.bar')
        then:
        'http://foo.bar' == request.getUrl()
        when:
        request.url(null)
        then:
        null == request.getUrl()
    }

    def "Params"() {
        when:
        request.params {
            foo 'bar'
        }
        request.params {
            name 'monica'
        }
        then:
        [foo:'bar', name: 'monica'] == request.getParams()
    }

}
