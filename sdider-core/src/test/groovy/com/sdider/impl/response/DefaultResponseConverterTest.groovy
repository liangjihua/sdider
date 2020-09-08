package com.sdider.impl.response

import com.sdider.api.Response
import com.sdider.impl.request.SdiderRequestFactory
import spock.lang.Specification

class DefaultResponseConverterTest extends Specification {
    SdiderRequestFactory requestFactory
    DefaultResponseConverter converter

    void setup() {
        requestFactory = Mock()
        converter = new DefaultResponseConverter(requestFactory)
    }

    def "Convert"() {
        def response = Mock(Response) {
            getHeaders() >> [:]
        }

        when:
        def sdiderResponse = converter.convert(response)

        then:
        sdiderResponse.getExtensions().contains('css')
    }
}
