package com.sdider.impl.item

import com.sdider.api.Item
import com.sdider.api.Request
import spock.lang.Specification

class DefaultItemContainerTest extends Specification {
    DefaultItemContainer container
    Request request

    void setup() {
        request = Mock(Request)
        container = new DefaultItemContainer(request)
    }

    def "Item"() {
        Closeable closeable = Mock()

        when:
        container.item {
            closeable.close()
            foo 'bar'
        }

        then:
        1 * closeable.close()
        1 == container.getItems().size()
        'bar' == container.getItems().get(0).get('foo')
    }

    def "SetItems"() {
        def items = [Mock(Item), Mock(Item)]
        when:
        container.setItems(items)

        then:
        items == container.getItems()

        when:
        container.setItems(null)

        then:
        container.getItems().isEmpty()
    }

    def "AddItem"() {
        def item = Mock(Item)
        def item1 = Mock(Item)
        when:
        container.addItem(item)
        container.addItem(item1)
        container.addItem(null)

        then:
        [item, item1] == container.getItems()
    }

    def "GetItems"() {
        given:
        def givenItems = [Mock(Item), Mock(Item)]
        container.setItems(givenItems)

        when:
        def items = container.getItems()
        items.add(Mock(Item))

        then:
        givenItems == container.getItems()
    }

    def "getItemsWhenNoItemAdd"() {
        expect:
        container.getItems().isEmpty()
    }

    def "RemoveItem"() {
        given:
        def item = Mock(Item)
        def item2 = Mock(Item)
        container.addItem(item)
        container.addItem(item2)

        when:
        container.removeItem(item)
        container.removeItem(null)

        then:
        [item2] == container.getItems()
    }
}
