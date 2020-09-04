package com.sdider.impl

import com.sdider.AbstractDynamicPropertiesObjectTest
import com.sdider.crawler.common.DynamicPropertiesObject
import spock.lang.Ignore

@Ignore("Configuration 需要调整，可能不再适用于DynamicPropertiesObject")
class DefaultConfigurationImplTest extends AbstractDynamicPropertiesObjectTest {
    DefaultConfigurationImpl configuration

    def setup() {
        configuration = dpObject as DefaultConfigurationImpl
    }

    def "MethodMissing"() {
    }

    def getNonExistsField() {
        expect:
        null == dpObject.get("non-exists-field")
    }

    @Override
    protected DynamicPropertiesObject create() {
        return new DefaultConfigurationImpl()
    }
}
