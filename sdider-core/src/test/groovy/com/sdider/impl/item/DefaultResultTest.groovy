package com.sdider.impl.item

import com.sdider.api.Item
import com.sdider.api.Pipeline
import com.sdider.api.Request
import com.sdider.impl.request.AbstractRequestContainer
import com.sdider.impl.request.AbstractRequestContainerTest

class DefaultResultTest extends AbstractRequestContainerTest {
    DefaultResult result
    Request request

    void setup() {
        result = requestContainer as DefaultResult
    }

    @Override
    protected AbstractRequestContainer create() {
        request = Mock()
        return new DefaultResult(request)
    }

    def "Item"() {
        when:
        result.item {
            set('foo', 'bar')
        }

        then:
        1 == result.items.size()
        'bar' == result.items[0].get('foo')
    }

    def "SetItems"() {
        def items = [Mock(Item), Mock(Item)]
        when:
        result.setItems(items)
        then:
        items == result.items
        items !== result.items

        when:
        items << Mock(Item)
        then:
        3 == items.size()
        2 == result.items.size()
    }

    def "setItemsNull"() {
        def items = [Mock(Item)]
        result.setItems(items)

        when:
        result.setItems(null)

        then:
        result.items.isEmpty()
    }

    def "AddItem"() {
        def item = Mock(Item)
        when:
        result.addItem(item)

        then:
        1 == result.items.size()
        item == result.items[0]
    }

    def "addItemNull"() {
        def item = Mock(Item)
        result.addItem(item)
        when:
        result.addItem(null)

        then:
        [item] == result.items
    }

    def "Consume"() {
        def pipeline = Mock(Pipeline)
        def item = Mock(Item)
        def item2 = Mock(Item)
        result.addItem(item)
        result.addItem(item2)

        when:
        result.consume([pipeline])

        then:
        1 * item.consume(pipeline)
        1 * item2.consume(pipeline)
    }

    def "Request"() {
        result = Spy(result)
        when:
        result.request 'http://foo.bar'

        then:
        1 == result.getRequests().size()
        'http://foo.bar' == result.getRequests()[0].getUrl()
        'get'.equalsIgnoreCase(result.getRequests()[0].getMethod())
        1 * result.urlClean('http://foo.bar')
    }

    def "TestRequest"() {
        result = Spy(result)
        when:
        result.request {
            POST 'http://foo.bar'
        }

        then:
        1 == result.getRequests().size()
        'http://foo.bar' == result.getRequests()[0].getUrl()
        'post'.equalsIgnoreCase(result.getRequests()[0].getMethod())
        1 * result.urlClean('http://foo.bar')
    }

    def "UrlClean"() {
        request.getUrl() >> 'http://foo.bar'

        expect:
        'http://bar.bar' == result.urlClean('http://bar.bar')
        'http://foo.bar?foo=bar&bar=foo' == result.urlClean('?foo=bar&bar=foo')
        'http://foo.bar/foo' == result.urlClean('/foo')
        'http://foo.bar/foo' == result.urlClean('foo')
    }
}
