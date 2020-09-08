package com.sdider.impl.item

import com.sdider.api.Item
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
        return new DefaultResult()
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

    def "RemoveItem"() {
        given:
        def item = Mock(Item)
        def item2 = Mock(Item)
        result.addItem(item)
        result.addItem(item2)

        when:
        result.removeItem(item)
        result.removeItem(null)

        then:
        [item2] == result.getItems()
    }
}
