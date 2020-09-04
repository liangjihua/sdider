package com.sdider.impl.response

import com.sdider.crawler.Request
import com.sdider.crawler.Response
import spock.lang.Specification

class DefaultResponseTest extends Specification {
    DefaultResponse sdiderResponse
    Response response
    Request request

    void setup() {
        request = Mock(Request) {
            getUrl() >> 'http://foo.bar'
            getMethod() >> 'GET'
        }
        response = Mock(Response) {
            getRequest() >> request
            getHeaders() >> [:]
        }
        sdiderResponse = new DefaultResponse(response)
    }

    def "GetResult"() {
        expect:
        null != sdiderResponse.getResult()
        sdiderResponse.getResult() === sdiderResponse.getResult()
    }

    def "GetContentType"() {
        def headers = ['Content-type': 'application/json']

        when:
        sdiderResponse = new DefaultResponse(response)
        def contentType = sdiderResponse.getContentType()

        then:
        1 * response.getHeaders() >> headers
        'application/json' == contentType
    }

    def "GetCharset"() {

        when:
        sdiderResponse = new DefaultResponse(response)
        def charset = sdiderResponse.getCharset()
        then:
        1 * response.getHeaders() >> ['Content-type': 'application/json;charset=utf-8']
        'UTF-8' == charset
    }

    def "GetText"() {
        def body = 'foo'.bytes
        1 * response.getBody() >> body
        sdiderResponse = new DefaultResponse(response)

        expect:
        'foo' == sdiderResponse.getText()
        body == sdiderResponse.getBody()
    }

    def "GetRequest"() {
        expect:
        request == sdiderResponse.getRequest()
    }

    def "GetStatusCode"() {
        1 * response.getStatusCode() >> 403
        sdiderResponse = new DefaultResponse(response)
        expect:
        403 == sdiderResponse.getStatusCode()
    }

    def "GetReasonPhrase"() {
        1 * response.getReasonPhrase() >> 'ok'
        sdiderResponse = new DefaultResponse(response)
        expect:
        'ok' == sdiderResponse.getReasonPhrase()
    }

    def "GetHeaders"() {
        def headers = ['Content-type': 'application/json']
        response = Mock {
            1 * getHeaders() >> headers
        }
        sdiderResponse = new DefaultResponse(response)

        expect:
        headers == sdiderResponse.getHeaders()
    }

    def "GetExtensions"() {
        expect:
        null != sdiderResponse.getExtensions()
        sdiderResponse.getExtensions() == sdiderResponse.getExtensions()
    }
}
