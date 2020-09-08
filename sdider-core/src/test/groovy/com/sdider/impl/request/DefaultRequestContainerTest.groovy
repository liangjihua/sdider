package com.sdider.impl.request

import com.sdider.SdiderRequest

class DefaultRequestContainerTest extends AbstractRequestContainerTest {
    DefaultRequestContainer container
    SdiderRequestFactory requestFactory

    void setup() {
        container = Spy(requestContainer as DefaultRequestContainer)
    }

    @Override
    protected AbstractRequestContainer create() {
        requestFactory = Mock(SdiderRequestFactory)
        return new DefaultRequestContainer(requestFactory)
    }

    def "Request"() {
        given:
        def request = Mock(SdiderRequest)
        (1.._) * request.getUrl() >> 'http://foo.bar'

        when:
        container.request {
            POST 'http://foo.bar'
        }
        then:
        1 * requestFactory.create() >> request
        1 * request.POST('http://foo.bar')
        1 * container.addRequest(request)
    }

    def "request url is empty"() {
        given:
        requestFactory.create() >> Mock(SdiderRequest) {
            getUrl() >> null
        }

        when:
        container.request {}
        container.request('')
        container.request(null as String[])

        then:
        0 * container.addRequest(_)
    }

    def "request with urls"() {
        when:
        container.request('http://foo.bar')
        container.request('http://bar.foo')
        container.request('', null)

        then:
        2 * requestFactory.create() >> {
            Mock(SdiderRequest) {
                1 * GET(_)
            }
        }
        2 * container.addRequest(_)
    }
}
