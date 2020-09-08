package com.sdider.impl.response

import com.sdider.api.Item
import com.sdider.api.Request
import com.sdider.api.Response
import com.sdider.impl.request.SdiderRequestFactory
import spock.lang.Specification

class DefaultResponseTest extends Specification {
    DefaultResponse sdiderResponse
    Response response
    Request request
    SdiderRequestFactory requestFactory

    void setup() {
        request = Mock(Request) {
            getUrl() >> 'http://foo.bar'
            getMethod() >> 'GET'
        }
        response = Mock(Response) {
            getRequest() >> request
            getHeaders() >> [:]
        }
        requestFactory = Mock(SdiderRequestFactory)
        sdiderResponse = new DefaultResponse(response, requestFactory)
    }

    def "GetResult"() {
        def result = sdiderResponse.getResult()
        expect:
        result.getRequests().isEmpty()
        result.getItems().isEmpty()
        result != sdiderResponse.getResult()

        when:
        result.addRequest(Mock(Request))

        then:
        sdiderResponse.getResult().getRequests().isEmpty()

        when:
        def request = Mock(Request)
        sdiderResponse.targets {
            addRequest(request)
        }

        then:
        [request] == sdiderResponse.getResult().getRequests()
    }

    def "GetContentType"() {
        def headers = ['Content-type': 'application/json']

        when:
        sdiderResponse = new DefaultResponse(response, requestFactory)
        def contentType = sdiderResponse.getContentType()

        then:
        1 * response.getHeaders() >> headers
        'application/json' == contentType
    }

    def "GetCharset"() {

        when:
        sdiderResponse = new DefaultResponse(response, requestFactory)
        def charset = sdiderResponse.getCharset()
        then:
        1 * response.getHeaders() >> ['Content-type': 'application/json;charset=utf-8']
        'UTF-8' == charset
    }

    def "GetText"() {
        def body = 'foo'.bytes
        1 * response.getBody() >> body
        sdiderResponse = new DefaultResponse(response, requestFactory)

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
        sdiderResponse = new DefaultResponse(response, requestFactory)
        expect:
        403 == sdiderResponse.getStatusCode()
    }

    def "GetReasonPhrase"() {
        1 * response.getReasonPhrase() >> 'ok'
        sdiderResponse = new DefaultResponse(response, requestFactory)
        expect:
        'ok' == sdiderResponse.getReasonPhrase()
    }

    def "GetHeaders"() {
        def headers = ['Content-type': 'application/json']
        response = Mock {
            1 * getHeaders() >> headers
        }
        sdiderResponse = new DefaultResponse(response, requestFactory)

        expect:
        headers == sdiderResponse.getHeaders()
    }

    def "GetExtensions"() {
        expect:
        null != sdiderResponse.getExtensions()
        sdiderResponse.getExtensions() == sdiderResponse.getExtensions()
    }

    def "Items"() {
        def item = Mock(Item)
        Closeable closeable = Mock()

        when:
        sdiderResponse.items {
            closeable.close()
            addItem(item)
        }

        then:
        1 * closeable.close()
        [item] == sdiderResponse.getResult().getItems()
    }

    def "targets"() {
        def request = Mock(Request)
        Closeable closeable = Mock()

        when:
        sdiderResponse.targets {
            closeable.close()
            addRequest(request)
        }

        then:
        1 * closeable.close()
        [request] == sdiderResponse.getResult().getRequests()
    }
}
