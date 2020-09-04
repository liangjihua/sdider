package com.sdider

import com.sdider.api.common.DynamicPropertiesObject
import com.sdider.api.exception.NoSuchPropertyException
import spock.lang.Specification

abstract class AbstractDynamicPropertiesObjectTest extends Specification{
    DynamicPropertiesObject dpObject

    def setup() {
        dpObject = create()
    }

    /**
     * 返回一个要测试的{@link DynamicPropertiesObject}的实现的实例
     * @return 要测试的{@link DynamicPropertiesObject}的实现的实例
     */
    protected abstract DynamicPropertiesObject create();

    def has() {
        expect:
        !dpObject.has("foo")

        when:
        dpObject.set("foo", new Object())

        then:
        dpObject.has("foo")
    }


    def get() throws NoSuchFieldException {
        when:
        dpObject.set("foo", "bar")
        then:
        "bar" == dpObject.get("foo")

        when:
        dpObject.set("foo", null)
        then:
        null == dpObject.get('foo')
    }


    def getNonExistsField() {
        when:
        dpObject.get("non-exists-field")

        then:
        thrown(NoSuchPropertyException.class)
    }


    def set() throws NoSuchFieldException {
        when:
        dpObject.set("foo", "bar")
        then:
        "bar" == dpObject.get("foo")

        when:
        dpObject.set("foo", null)
        then:
        null == dpObject.get("foo")
    }


    def getAllProperties() {
        expect:
        null != dpObject.getProperties()
        dpObject.getProperties().isEmpty()

        when:
        dpObject.set("foo", "bar")
        Map<String, Object> properties = dpObject.getProperties()
        then:
        "bar" == properties.get("foo")

        when:
        properties.put("name", "monica")
        then:
        !(dpObject.has("name"))

        when:
        dpObject.set("room", "201")
        then:
        !(properties.containsKey("room"))

        when:
        Map<String, Object> properties1 = dpObject.getProperties()
        then:
        "bar" == properties1.get("foo")
        "201" == properties1.get("room")
    }
}
