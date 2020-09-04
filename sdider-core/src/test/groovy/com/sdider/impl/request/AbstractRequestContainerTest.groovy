package com.sdider.impl.request

import com.sdider.api.Request
import spock.lang.Specification

abstract class AbstractRequestContainerTest extends Specification {
    AbstractRequestContainer requestContainer

    void setup() {
        requestContainer = create()
    }

    protected abstract AbstractRequestContainer create()

    def "SetRequests"() {
        def requests = [Mock(Request), Mock(Request)]
        when:
        requestContainer.setRequests(requests)

        then:
        requests == requestContainer.getRequests()
    }

    def "setRequestsNull"() {
        when:
        requestContainer.setRequests(null)

        then:
        requestContainer.getRequests().isEmpty()
    }

    def "AddRequest"() {
        def request = Mock(Request)
        when:
        requestContainer.addRequest(request)

        then:
        [request] == requestContainer.getRequests()
    }

    def "addRequestNull"() {
        def request = Mock(Request)
        when:
        requestContainer.addRequest(request)
        requestContainer.addRequest(null)
        then:
        [request] == requestContainer.getRequests()
    }

    def "RemoveRequest"() {
        def request = Mock(Request)
        def request2 = Mock(Request)
        requestContainer.addRequest(request)
        requestContainer.addRequest(request2)

        when:
        requestContainer.removeRequest(request)
        requestContainer.removeRequest(null)

        then:
        [request2] == requestContainer.getRequests()
    }

    def "removeRequestNull"() {
        def request = Mock(Request)
        def request1 = Mock(Request)
        requestContainer.setRequests(Arrays.asList(request, request1))

        when:
        requestContainer.removeRequest(null)
        then:
        [request, request1] == requestContainer.getRequests()
    }

    def "GetRequests"() {
        expect:
        requestContainer.getRequests().isEmpty()

        when:
        requestContainer.addRequest(Mock(Request))
        requestContainer.addRequest(Mock(Request))
        def requests = requestContainer.getRequests()

        then:
        2 == requests.size()
        2 == requestContainer.getRequests().size()

        when:
        requests.add(Mock(Request))

        then:
        2 == requestContainer.getRequests().size()
    }
}
