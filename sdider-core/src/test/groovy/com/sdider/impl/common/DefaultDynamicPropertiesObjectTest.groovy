package com.sdider.impl.common

import com.sdider.AbstractDynamicPropertiesObjectTest
import com.sdider.api.common.DynamicPropertiesObject

class DefaultDynamicPropertiesObjectTest extends AbstractDynamicPropertiesObjectTest {

    def "MethodMissing"() {
        when:
        dpObject.foo('bar')

        then:
        'bar' == dpObject.get('foo')
    }

    @Override
    protected DynamicPropertiesObject create() {
        return new DefaultDynamicPropertiesObject()
    }
}
