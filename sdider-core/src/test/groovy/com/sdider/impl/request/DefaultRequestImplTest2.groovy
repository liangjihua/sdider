package com.sdider.impl.request

import com.sdider.AbstractDynamicPropertiesObjectTest
import com.sdider.api.common.DynamicPropertiesObject

import static com.sdider.RequestConfig.APPLICATION_FORM_URLENCODED
import static com.sdider.RequestConfig.UA_FIREFOX

class DefaultRequestImplTest2 extends AbstractDynamicPropertiesObjectTest{
    DefaultRequestImpl request

    def setup() {
        request = dpObject as DefaultRequestImpl
    }

    def "MethodMissing"() {
        when:
        request.methodMissing('foo', ['bar']as String[])

        then:
        'bar' == request.get('foo')
    }

    def "methodMissing2"() {
        when:
        request.methodMissing('foo', ['bar', 'bar2']as String[])

        then:
        thrown(MissingMethodException)
    }

    def "GetProperty"() {
        request.url('http://foo.bar')
        request.method('GET')
        request.body('body')
        request.userAgent(UA_FIREFOX)
        request.contentType(APPLICATION_FORM_URLENCODED)
        request.proxy('127.0.0.1:1080')
        request.headers {
            accept 'text/html'
        }
        request.params {
            'foo' 'bar'
        }
        request.set('foo', 'bar')
        expect:
        'http://foo.bar' == request.url
        'GET' == request.method
        'body' == request.body
        UA_FIREFOX == request.userAgent
        APPLICATION_FORM_URLENCODED == request.contentType
        '127.0.0.1:1080' == request.proxy
        ['User-Agent': UA_FIREFOX, 'Content-Type': APPLICATION_FORM_URLENCODED, accept: 'text/html'] == request.headers
        [foo: 'bar'] == request.params
        [foo: 'bar'] == request.properties
    }

    def "getProperty2"() {
        when:
        request.foo
        then:
        thrown(MissingPropertyException)
    }
    @Override
    protected DynamicPropertiesObject create() {
        return new DefaultRequestImpl()
    }
}
