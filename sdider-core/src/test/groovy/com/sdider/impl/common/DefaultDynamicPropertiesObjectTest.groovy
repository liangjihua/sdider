package com.sdider.impl.common

import com.sdider.AbstractDynamicPropertiesObjectTest
import com.sdider.api.common.DynamicPropertiesObject

class DefaultDynamicPropertiesObjectTest extends AbstractDynamicPropertiesObjectTest {
    DefaultDynamicPropertiesObject defaultObject

    def setup() {
        defaultObject = dpObject as DefaultDynamicPropertiesObject
    }

    def "MethodMissing"() {
        when:
        defaultObject.foo('bar')

        then:
        'bar' == defaultObject.get('foo')

        when:
        defaultObject.foo('bar', 'foo')

        then:
        thrown(MissingMethodException)
    }

    def "methodMissing with Gstring"() {
        def bar = 'bar'
        when:
        defaultObject.foo("$bar")

        then:
        'bar' == defaultObject.get('foo')
        (defaultObject.get('foo')) instanceof String
    }

    def "getProperty"() {
        defaultObject.set('foo', 'bar')
        when:
        def result = defaultObject.getProperty("properties")

        then:
        result == defaultObject.getProperties()

        when:
        result = defaultObject.getProperty('foo')

        then:
        'bar' == result

        when:
        defaultObject.getProperty('not-exists')

        then:
        thrown(MissingPropertyException)
    }

    @Override
    protected DynamicPropertiesObject create() {
        return new DefaultDynamicPropertiesObject()
    }
}
