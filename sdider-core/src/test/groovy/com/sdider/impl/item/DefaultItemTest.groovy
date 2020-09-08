package com.sdider.impl.item

import com.sdider.AbstractDynamicPropertiesObjectTest
import com.sdider.api.Item
import com.sdider.api.Pipeline
import com.sdider.api.PipelineFilter
import com.sdider.api.Request
import com.sdider.api.common.DynamicPropertiesObject

class DefaultItemTest extends AbstractDynamicPropertiesObjectTest {
    Request request
    DefaultItem item

    @Override
    protected DynamicPropertiesObject create() {
        request = Mock()
        return new DefaultItem(request)
    }

    def setup() {
        item = dpObject as DefaultItem
    }

    def "GetRequest"() {
        expect:
        request == item.getRequest()
    }

    def "MethodMissing"() {
        when:
        item.foo 'bar'

        then:
        'bar' == item.get('foo')
    }

    def "GetPipelineFilters"() {
        expect:
        item.getPipelineFilters().isEmpty()

        when:
        item.addPipelineFilter(Mock(PipelineFilter))
        item.addPipelineFilter(Mock(PipelineFilter))

        then:
        2 == item.getPipelineFilters().size()
    }

    def "modify getPipelineFilters list"() {
        def filters = item.getPipelineFilters()
        expect:
        filters.isEmpty()

        when:
        item.addPipelineFilter(Mock(PipelineFilter))

        then:
        filters.isEmpty()
        filters != item.getPipelineFilters()

        when:
        filters = item.getPipelineFilters()
        filters.add(Mock(PipelineFilter))

        then:
        2 == filters.size()
        1 == item.getPipelineFilters().size()
    }

    def "AddPipelineFilter"() {
        PipelineFilter filter = Mock()

        when:
        item.addPipelineFilter(filter)

        then:
        [filter] == item.getPipelineFilters()
    }

    def "addPipelineNull"() {
        when:
        item.addPipelineFilter(null)
        then:
        thrown(IllegalArgumentException)
    }

    def "RemovePipelineFilter"() {
        PipelineFilter filter = Mock()
        PipelineFilter filter2 = Mock()
        item.addPipelineFilter(filter)
        item.addPipelineFilter(filter2)

        when:
        item.removePipelineFilter(filter2)

        then:
        [filter] == item.getPipelineFilters()
    }

    def "removePipelineFilterNull"() {
        when:
        item.removePipelineFilter(null)
        then:
        thrown(IllegalArgumentException.class)
    }

    def "Filter"() {
        given:
        Pipeline pipeline = Mock(Pipeline) {
             getName() >> 'foo'
        }
        when:
        item.filter {
            name == 'foo'
        }
        def filters = item.getPipelineFilters()

        then:
        1 == filters.size()
        filters[0].applicable(pipeline)
    }

    def "getProperty"() {
        item.set('foo', 'bar')
        expect:
        request === item.getProperty("request")
        item === item.getProperty("item")
        item.getProperties() == item.getProperty("properties")
        'bar' == item.getProperty('foo')

        when:
        item.getProperty('no-exists')

        then:
        thrown(MissingPropertyException)
    }

    def "setProperty: set #propertyName to #value"() {
        when:
        item.setProperty(propertyName, value)

        then:
        thrown(ReadOnlyPropertyException)

        where:
        propertyName << ['request', 'item', 'properties']
        value << [Mock(Request), Mock(Item), [:]]
    }

    def "setProperty"() {
        when:
        item.setProperty('foo', 'bar')

        then:
        'bar' == item.get('foo')
    }

}
