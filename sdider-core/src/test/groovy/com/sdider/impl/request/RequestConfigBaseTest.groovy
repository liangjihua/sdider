package com.sdider.impl.request

import com.sdider.RequestConfig
import spock.lang.Specification

import static com.sdider.RequestConfig.*
import static com.sdider.constant.HttpHeader.USER_AGENT

abstract class RequestConfigBaseTest extends Specification {
    RequestConfig reqConfig

    void setup() {
        reqConfig = create()
    }

    abstract RequestConfig create()

    def "ContentType"() {
        expect:
        null == reqConfig.getContentType()

        when:
        reqConfig.contentType(APPLICATION_FORM_URLENCODED)
        then:
        APPLICATION_FORM_URLENCODED == reqConfig.getContentType()

        when:
        reqConfig.contentType(null)
        then:
        null == reqConfig.getContentType()
    }

    def "GetContentType"() {
        expect:
        null == reqConfig.getContentType()

        when:
        reqConfig.contentType(APPLICATION_JSON)
        then:
        APPLICATION_JSON == reqConfig.getContentType()
    }

    def "Headers"() {
        when:
        reqConfig.headers {
            accept 'text/html'
            'content-type' APPLICATION_FORM_URLENCODED
        }
        reqConfig.headers {
            userAgent UA_CHROME
        }

        then:
        [accept:'text/html',
         'content-type':APPLICATION_FORM_URLENCODED,
         userAgent:UA_CHROME] == reqConfig.getHeaders()
    }

    def "SetHeaders"() {
        def headers = ['userAgent': UA_CHROME]
        when:
        reqConfig.setHeaders(headers)
        then:
        headers == reqConfig.getHeaders()

        when:
        headers.accpet = 'text/html'
        then:
        [userAgent: UA_CHROME] == reqConfig.getHeaders()

        when:
        reqConfig.setHeaders(null)
        then:
        reqConfig.getHeaders().isEmpty()
    }

    def "GetHeaders"() {
        expect:
        reqConfig.getHeaders().isEmpty()

        when:
        reqConfig.setHeaders([accept:'text/html', userAgent:UA_CHROME])
        def headers = reqConfig.getHeaders()
        then:
        [accept:'text/html', userAgent:UA_CHROME] == headers

        when:
        headers.remove('accept')
        then:
        [userAgent:UA_CHROME] == headers
        [accept:'text/html', userAgent:UA_CHROME] == reqConfig.getHeaders()
    }

    def "UserAgent"() {
        when:
        reqConfig.userAgent(UA_CHROME)
        then:
        UA_CHROME == reqConfig.getUserAgent()
        UA_CHROME == reqConfig.getHeaders().get(USER_AGENT)
    }

    def "GetUserAgent"() {
        expect:
        null == reqConfig.getUserAgent()

        when:
        reqConfig.headers {
            "User-Agent" UA_CHROME
        }

        then:
        UA_CHROME == reqConfig.getUserAgent()
    }

    def "Proxy"() {
        def proxy = '127.0.0.1:1090'
        when:
        reqConfig.proxy(proxy)
        then:
        proxy == reqConfig.getProxy()

        when:
        reqConfig.proxy(null)
        then:
        null == reqConfig.getProxy()
    }
}
